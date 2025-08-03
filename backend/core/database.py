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
    echo=settings.DEBUG
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

from sqlalchemy.ext.asyncio import create_async_engine, AsyncSession
from sqlalchemy.orm import sessionmaker, declarative_base
from core.config import settings
import logging

logger = logging.getLogger(__name__)

# Create asynchronous engine with ultra-conservative settings for Supabase
engine = create_async_engine(
    settings.DATABASE_URL,
    echo=settings.DEBUG,
    pool_size=1,  # Minimal connections for shared database
    max_overflow=1,  # Very limited overflow
    pool_pre_ping=True,
    pool_recycle=120,  # 2 minutes - very short recycle
    pool_timeout=5,  # Very short timeout
    future=True
)

# Create async session factory
AsyncSessionLocal = sessionmaker(
    bind=engine,
    class_=AsyncSession,
    expire_on_commit=False,
    autocommit=False,
    autoflush=False,
)

# Create base class for models
Base = declarative_base()

# Dependency to get database session
async def get_db() -> AsyncSession:
    """Dependency to get an async database session"""
    async with AsyncSessionLocal() as session:
        try:
            yield session
        except Exception as e:
            logger.error(f"Database session error: {e}")
            await session.rollback()
            raise

# Test database connection
async def test_db_connection():
    """Test database connection"""
    try:
        async with engine.connect() as conn:
            await conn.execute("SELECT 1")
        logger.info("Database connection successful")
        return True
    except Exception as e:
        logger.error(f"Database connection failed: {e}")
        return False 