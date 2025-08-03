"""
Database configuration for She&Soul FastAPI application
"""

from sqlalchemy.ext.asyncio import create_async_engine, AsyncSession
from sqlalchemy.orm import sessionmaker, declarative_base
from fastapi import HTTPException
from core.config import settings
import logging

logger = logging.getLogger(__name__)

# Create asynchronous engine with optimized connection pool settings
engine = create_async_engine(
    settings.DATABASE_URL,
    echo=settings.DEBUG,
    pool_size=10,  # Increased from 5
    max_overflow=20,  # Increased from 10
    pool_pre_ping=True,
    pool_recycle=1800,  # Increased from 300 (30 minutes)
    pool_timeout=30,  # Added connection timeout
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
    """Dependency to get an async database session with proper error handling"""
    try:
        async with AsyncSessionLocal() as session:
            try:
                yield session
                await session.commit()
            except Exception as e:
                logger.error(f"Database session error: {e}")
                await session.rollback()
                raise
            finally:
                await session.close()
    except Exception as e:
        logger.error(f"Failed to create database session: {e}")
        # Return a mock session or raise an appropriate error
        raise HTTPException(
            status_code=500,
            detail="Database service temporarily unavailable"
        )

# Add database session health check
async def check_db_health():
    """Check database health and connection pool status"""
    try:
        async with engine.connect() as conn:
            await conn.execute("SELECT 1")
        
        # Log connection pool stats
        pool = engine.pool
        logger.info(f"Database pool status - Size: {pool.size()}, Checked out: {pool.checkedout()}, Invalid: {pool.invalidated()}")
        return True
    except Exception as e:
        logger.error(f"Database health check failed: {e}")
        return False

# Test database connection
async def test_db_connection():
    """Test database connection with better error handling"""
    try:
        async with engine.connect() as conn:
            await conn.execute("SELECT 1")
        logger.info("Database connection successful")
        return True
    except Exception as e:
        logger.error(f"Database connection failed: {e}")
        return False 