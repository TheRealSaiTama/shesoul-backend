"""
Database configuration for She&Soul FastAPI application
"""

from sqlalchemy.ext.asyncio import create_async_engine, AsyncSession
from sqlalchemy.orm import sessionmaker, declarative_base
from sqlalchemy import create_engine
from sqlalchemy.exc import OperationalError, DisconnectionError
from fastapi import HTTPException
from core.config import settings
import logging
import time
import asyncio

logger = logging.getLogger(__name__)

# Create asynchronous engine with very conservative connection pool settings
engine = create_async_engine(
    settings.DATABASE_URL,
    echo=settings.DEBUG,
    pool_size=2,  # Very conservative for shared hosting
    max_overflow=3,  # Minimal overflow
    pool_pre_ping=True,
    pool_recycle=300,  # 5 minutes - shorter recycle time
    pool_timeout=10,  # Shorter timeout
    future=True
)

# Create synchronous engine for legacy sync operations
sync_engine = create_engine(
    settings.DATABASE_URL.replace('postgresql+asyncpg://', 'postgresql://'),
    echo=settings.DEBUG,
    pool_size=1,  # Minimal for sync operations
    max_overflow=2,  # Very limited
    pool_pre_ping=True,
    pool_recycle=300,  # 5 minutes
    pool_timeout=10
)

# Create async session factory
AsyncSessionLocal = sessionmaker(
    bind=engine,
    class_=AsyncSession,
    expire_on_commit=False,
    autocommit=False,
    autoflush=False,
)

# Create sync session factory for legacy operations
SyncSessionLocal = sessionmaker(
    bind=sync_engine,
    expire_on_commit=False,
    autocommit=False,
    autoflush=False,
)

# Create base class for models
Base = declarative_base()

# Dependency to get async database session - emergency safe version
async def get_db() -> AsyncSession:
    """Emergency safe database session - returns error if DB unavailable"""
    try:
        async with AsyncSessionLocal() as session:
            yield session
            await session.commit()
    except Exception as e:
        logger.error(f"Database unavailable: {e}")
        # Return error response instead of crashing
        raise HTTPException(
            status_code=503,
            detail="Database service temporarily unavailable - please try again later"
        )

# Dependency to get sync database session for legacy operations
def get_sync_db():
    """Dependency to get a sync database session for legacy operations"""
    max_retries = 3
    retry_delay = 1
    
    for attempt in range(max_retries):
        try:
            db = SyncSessionLocal()
            # Test the connection
            db.execute("SELECT 1")
            yield db
            break
        except (OperationalError, DisconnectionError) as e:
            logger.warning(f"Database connection attempt {attempt + 1} failed: {e}")
            if attempt < max_retries - 1:
                time.sleep(retry_delay)
                retry_delay *= 2  # Exponential backoff
            else:
                logger.error("All database connection attempts failed")
                raise
        except Exception as e:
            logger.error(f"Database session error: {e}")
            db.rollback()
            raise
        finally:
            try:
                db.close()
            except:
                pass

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