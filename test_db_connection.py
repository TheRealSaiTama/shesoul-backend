#!/usr/bin/env python3
"""
Database connection test script for She&Soul FastAPI application
"""

import asyncio
import sys
import os

# Add the current directory to Python path
sys.path.append(os.path.dirname(os.path.abspath(__file__)))

from core.database import test_db_connection, engine, sync_engine
from core.config import settings
import logging

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

async def test_async_connection():
    """Test async database connection"""
    print("Testing async database connection...")
    try:
        async with engine.connect() as conn:
            result = await conn.execute("SELECT 1 as test")
            row = result.fetchone()
            print(f"✅ Async connection successful: {row[0]}")
            return True
    except Exception as e:
        print(f"❌ Async connection failed: {e}")
        return False

def test_sync_connection():
    """Test sync database connection"""
    print("Testing sync database connection...")
    try:
        with sync_engine.connect() as conn:
            result = conn.execute("SELECT 1 as test")
            row = result.fetchone()
            print(f"✅ Sync connection successful: {row[0]}")
            return True
    except Exception as e:
        print(f"❌ Sync connection failed: {e}")
        return False

async def main():
    """Main test function"""
    print("=" * 50)
    print("She&Soul Database Connection Test")
    print("=" * 50)
    
    print(f"Database URL: {settings.DATABASE_URL}")
    print(f"Debug mode: {settings.DEBUG}")
    print()
    
    # Test async connection
    async_success = await test_async_connection()
    print()
    
    # Test sync connection
    sync_success = test_sync_connection()
    print()
    
    # Test the test_db_connection function
    print("Testing test_db_connection function...")
    test_success = await test_db_connection()
    if test_success:
        print("✅ test_db_connection function successful")
    else:
        print("❌ test_db_connection function failed")
    
    print()
    print("=" * 50)
    if async_success and sync_success and test_success:
        print("🎉 All database tests passed!")
        return 0
    else:
        print("💥 Some database tests failed!")
        return 1

if __name__ == "__main__":
    exit_code = asyncio.run(main())
    sys.exit(exit_code)