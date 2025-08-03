#!/usr/bin/env python3
"""
Database connection monitoring script for She&Soul
This script helps monitor database connection pool usage and identify potential issues.
"""

import asyncio
import logging
from core.database import engine, check_db_health
from core.config import settings

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

async def monitor_connections():
    """Monitor database connection pool continuously"""
    logger.info("Starting database connection monitoring...")
    
    while True:
        try:
            # Check database health
            is_healthy = await check_db_health()
            
            # Get pool statistics
            pool = engine.pool
            pool_size = pool.size()
            checked_out = pool.checkedout()
            checked_in = pool.checkedin()
            invalid = pool.invalidated()
            
            logger.info(f"""
Database Pool Status:
- Health: {'Healthy' if is_healthy else 'Unhealthy'}
- Pool Size: {pool_size}
- Checked Out: {checked_out}
- Checked In: {checked_in}
- Invalid: {invalid}
- Overflow: {pool.overflow()}
- URL: {settings.DATABASE_URL.split('@')[1] if '@' in settings.DATABASE_URL else 'Hidden'}
            """)
            
            # Alert if too many connections are checked out
            if checked_out > pool_size * 0.8:
                logger.warning(f"High connection usage: {checked_out}/{pool_size} connections in use")
            
            # Wait 30 seconds before next check
            await asyncio.sleep(30)
            
        except Exception as e:
            logger.error(f"Error monitoring connections: {e}")
            await asyncio.sleep(10)

async def test_connection_leak():
    """Test for potential connection leaks"""
    logger.info("Testing for connection leaks...")
    
    from core.database import get_db
    
    # Create multiple sessions without proper cleanup to simulate leak
    sessions = []
    try:
        for i in range(15):  # Try to create more sessions than pool size
            logger.info(f"Creating session {i+1}")
            async for session in get_db():
                sessions.append(session)
                if i < 14:  # Keep sessions open to test pool exhaustion
                    break
    except Exception as e:
        logger.error(f"Connection test failed at session {len(sessions)+1}: {e}")
    
    logger.info(f"Created {len(sessions)} sessions")
    
    # Cleanup
    for session in sessions:
        try:
            await session.close()
        except:
            pass

if __name__ == "__main__":
    import sys
    
    if len(sys.argv) > 1 and sys.argv[1] == "test":
        asyncio.run(test_connection_leak())
    else:
        asyncio.run(monitor_connections())
