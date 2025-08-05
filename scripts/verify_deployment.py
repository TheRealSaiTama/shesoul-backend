#!/usr/bin/env python3
"""
Pre-deployment verification script for She&Soul API
This script checks configuration and potential issues before deployment.
"""

import asyncio
import logging
import sys
from typing import List, Tuple

# Configure logging
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

class DeploymentVerifier:
    def __init__(self):
        self.checks_passed = 0
        self.checks_failed = 0
        self.warnings = 0

    def log_check(self, name: str, passed: bool, message: str, warning: bool = False):
        """Log the result of a verification check"""
        if warning:
            logger.warning(f"⚠️  {name}: {message}")
            self.warnings += 1
        elif passed:
            logger.info(f"✅ {name}: {message}")
            self.checks_passed += 1
        else:
            logger.error(f"❌ {name}: {message}")
            self.checks_failed += 1

    def check_database_config(self):
        """Verify database configuration"""
        try:
            from core.config import settings
            
            # Check if DATABASE_URL is set
            if settings.DATABASE_URL:
                self.log_check("Database URL", True, "Database URL is configured")
                
                # Check if it's using asyncpg
                if "postgresql+asyncpg://" in settings.DATABASE_URL:
                    self.log_check("Database Driver", True, "Using asyncpg driver")
                else:
                    self.log_check("Database Driver", False, "Not using asyncpg driver")
                
                # Check if pooler is enabled
                if "pooler=true" in settings.DATABASE_URL:
                    self.log_check("Database Pooler", True, "Supabase pooler enabled")
                else:
                    self.log_check("Database Pooler", False, "Supabase pooler not enabled", warning=True)
                    
            else:
                self.log_check("Database URL", False, "Database URL not configured")
                
        except Exception as e:
            self.log_check("Database Config", False, f"Error checking database config: {e}")

    def check_async_imports(self):
        """Check if all required async libraries are available"""
        required_packages = [
            ("sqlalchemy.ext.asyncio", "AsyncSession"),
            ("asyncpg", None),
            ("fastapi", "FastAPI"),
            ("uvicorn", None),
        ]
        
        for package, item in required_packages:
            try:
                if item:
                    module = __import__(package, fromlist=[item])
                    getattr(module, item)
                else:
                    __import__(package)
                self.log_check(f"Package {package}", True, f"{package} is available")
            except ImportError:
                self.log_check(f"Package {package}", False, f"{package} not available")

    def check_session_configuration(self):
        """Check database session configuration"""
        try:
            from core.database import engine, AsyncSessionLocal
            
            # Check engine configuration
            pool_size = getattr(engine.pool, 'size', None)
            if pool_size and callable(pool_size):
                current_pool_size = pool_size()
                if current_pool_size >= 5:
                    self.log_check("Pool Size", True, f"Pool size is {current_pool_size}")
                else:
                    self.log_check("Pool Size", False, f"Pool size too small: {current_pool_size}", warning=True)
            
            # Check if AsyncSessionLocal is properly configured
            if AsyncSessionLocal:
                self.log_check("Session Factory", True, "AsyncSessionLocal is configured")
            else:
                self.log_check("Session Factory", False, "AsyncSessionLocal not configured")
                
        except Exception as e:
            self.log_check("Session Config", False, f"Error checking session config: {e}")

    def check_route_async_patterns(self):
        """Check if routes are using proper async patterns"""
        try:
            # Check auth routes
            import inspect
            from api.routes.auth import login, signup
            
            if inspect.iscoroutinefunction(login):
                self.log_check("Auth Login Async", True, "Login route is async")
            else:
                self.log_check("Auth Login Async", False, "Login route is not async")
                
            if inspect.iscoroutinefunction(signup):
                self.log_check("Auth Signup Async", True, "Signup route is async")
            else:
                self.log_check("Auth Signup Async", False, "Signup route is not async")
                
        except Exception as e:
            self.log_check("Route Async Check", False, f"Error checking routes: {e}")

    def check_environment_variables(self):
        """Check critical environment variables"""
        import os
        
        critical_vars = [
            "DATABASE_URL",
            "SECRET_KEY",
        ]
        
        optional_vars = [
            "DEBUG",
            "LOG_LEVEL",
            "DB_POOL_SIZE",
            "DB_MAX_OVERFLOW",
        ]
        
        for var in critical_vars:
            if os.getenv(var):
                self.log_check(f"Env Var {var}", True, f"{var} is set")
            else:
                self.log_check(f"Env Var {var}", False, f"{var} is not set")
        
        for var in optional_vars:
            if os.getenv(var):
                self.log_check(f"Optional Env {var}", True, f"{var} is set", warning=False)
            else:
                self.log_check(f"Optional Env {var}", True, f"{var} not set (using default)", warning=True)

    async def check_database_connectivity(self):
        """Test actual database connectivity"""
        try:
            from core.database import test_db_connection
            
            connected = await test_db_connection()
            if connected:
                self.log_check("DB Connectivity", True, "Database connection successful")
            else:
                self.log_check("DB Connectivity", False, "Database connection failed")
                
        except Exception as e:
            self.log_check("DB Connectivity", False, f"Error testing DB connection: {e}")

    async def run_all_checks(self):
        """Run all verification checks"""
        logger.info("🚀 Starting deployment verification...")
        logger.info("=" * 60)
        
        # Run synchronous checks
        self.check_database_config()
        self.check_async_imports()
        self.check_session_configuration()
        self.check_route_async_patterns()
        self.check_environment_variables()
        
        # Run asynchronous checks
        await self.check_database_connectivity()
        
        # Summary
        logger.info("=" * 60)
        logger.info(f"📊 Verification Summary:")
        logger.info(f"   ✅ Checks Passed: {self.checks_passed}")
        logger.info(f"   ❌ Checks Failed: {self.checks_failed}")
        logger.info(f"   ⚠️  Warnings: {self.warnings}")
        
        if self.checks_failed == 0:
            logger.info("🎉 All critical checks passed! Ready for deployment.")
            return True
        else:
            logger.error("🚨 Some checks failed. Please fix issues before deployment.")
            return False

async def main():
    """Main verification function"""
    verifier = DeploymentVerifier()
    success = await verifier.run_all_checks()
    
    if not success:
        sys.exit(1)
    
    logger.info("✨ Deployment verification completed successfully!")

if __name__ == "__main__":
    asyncio.run(main())
