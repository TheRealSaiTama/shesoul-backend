"""
Database configuration for She&Soul FastAPI application
"""

from sqlalchemy.ext.asyncio import create_async_engine, AsyncSession
from sqlalchemy.orm import sessionmaker, declarative_base
from core.config import settings
import logging
from loguru import logger

# Create asynchronous engine with better connection pool settings
engine = create_async_engine(
    settings.DATABASE_URL,
    echo=settings.DEBUG,
    pool_size=1,  # Reduced to minimum to avoid connection limits
    max_overflow=2,  # Reduced to minimum
    pool_pre_ping=True,
    pool_recycle=300,
    pool_timeout=10,  # Reduced timeout
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
        finally:
            await session.close()

# Test database connection
async def test_db_connection():
    """Test database connection"""
    try:
        async with engine.connect() as conn:
            await conn.execute("SELECT 1")
        logger.info("Database connection successful")
        return True
    except Exception as e:
        logger.warning(f"Database connection failed: {e}")
        return False 