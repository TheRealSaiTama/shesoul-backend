"""
Database configuration for She&Soul FastAPI application
"""

from sqlalchemy.ext.asyncio import create_async_engine, async_sessionmaker, AsyncSession
from sqlalchemy.orm import declarative_base
from core.config import settings
import logging

logger = logging.getLogger(__name__)

# Create the engine with the correct connection pool settings
engine = create_async_engine(
    settings.DATABASE_URL,
    pool_size=5,
    max_overflow=0,
    pool_pre_ping=True,
    pool_recycle=300,
    echo=settings.DEBUG  # Use debug setting instead of hardcoded True
)

# Create a configured "Session" class
async_session_maker = async_sessionmaker(engine, expire_on_commit=False)

# Create base class for models
Base = declarative_base()

# Create the dependency that will be used in the routes
async def get_db() -> AsyncSession:
    """
    Dependency that provides a database session per request.
    This ensures proper session lifecycle management and prevents connection leaks.
    """
    async with async_session_maker() as session:
        try:
            yield session
        except Exception as e:
            logger.error(f"Database session error: {e}")
            await session.rollback()
            raise
        finally:
            await session.close()

# Add database session health check
async def check_db_health():
    """Check database health and connection pool status"""
    try:
        async with async_session_maker() as session:
            await session.execute("SELECT 1")
            await session.close()
        
        # Log connection pool stats
        pool = engine.pool
        logger.info(f"Database pool status - Size: {pool.size()}, Checked out: {pool.checkedout()}")
        return True
    except Exception as e:
        logger.error(f"Database health check failed: {e}")
        return False

# Test database connection
async def test_db_connection():
    """Test database connection with proper session management"""
    try:
        async with async_session_maker() as session:
            await session.execute("SELECT 1")
            await session.close()
        logger.info("Database connection successful")
        return True
    except Exception as e:
        logger.error(f"Database connection failed: {e}")
        return False 