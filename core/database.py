"""
Database configuration for She&Soul FastAPI application
"""

from sqlalchemy.ext.asyncio import create_async_engine, AsyncSession
from sqlalchemy.orm import sessionmaker, declarative_base
from sqlalchemy import create_engine
from sqlalchemy.exc import OperationalError, DisconnectionError
from core.config import settings
import logging
import time

logger = logging.getLogger(__name__)

# Create asynchronous engine with optimized connection pooling
engine = create_async_engine(
    settings.DATABASE_URL,
    echo=settings.DEBUG,
    pool_size=3,  # Reduced from 5 to avoid connection limits
    max_overflow=5,  # Reduced from 10
    pool_pre_ping=True,
    pool_recycle=300,
    pool_timeout=30,  # Add timeout
    future=True,
    drivername='postgresql+asyncpg'
)

# Create synchronous engine for legacy sync operations
sync_engine = create_engine(
    settings.DATABASE_URL.replace('postgresql+asyncpg://', 'postgresql://'),
    echo=settings.DEBUG,
    pool_size=3,
    max_overflow=5,
    pool_pre_ping=True,
    pool_recycle=300,
    pool_timeout=30,
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

# Dependency to get async database session
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