#!/usr/bin/env python3
"""
Simple database connection test for She&Soul FastAPI application
"""

import asyncio
import sys
import os

# Add the current directory to Python path
sys.path.append(os.path.dirname(os.path.abspath(__file__)))

from core.database import test_db_connection, get_db
from core.config import settings
from loguru import logger

async def test_basic_operations():
    """Test basic database operations"""
    logger.info("Testing database connection...")
    
    # Test connection
    if not await test_db_connection():
        logger.error("Database connection failed!")
        return False
    
    logger.info("Database connection successful!")
    
    # Test session creation
    try:
        async for session in get_db():
            logger.info("Database session created successfully!")
            break
    except Exception as e:
        logger.error(f"Failed to create database session: {e}")
        return False
    
    return True

async def main():
    """Main test function"""
    logger.info("Starting database connection test...")
    
    success = await test_basic_operations()
    
    if success:
        logger.info("✅ All database tests passed!")
    else:
        logger.error("❌ Database tests failed!")
        sys.exit(1)

if __name__ == "__main__":
    asyncio.run(main())