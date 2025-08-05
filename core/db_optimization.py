"""
Database connection optimization configuration for production deployment
"""

# Recommended production database connection settings
DATABASE_OPTIMIZATION_CONFIG = {
    # Connection Pool Settings
    "pool_size": 10,           # Number of connections to maintain in pool
    "max_overflow": 20,        # Additional connections allowed beyond pool_size
    "pool_timeout": 30,        # Seconds to wait for connection from pool
    "pool_recycle": 1800,      # Seconds before connection is recreated (30 minutes)
    "pool_pre_ping": True,     # Validate connections before use
    
    # Connection Settings
    "connect_timeout": 10,     # Seconds to wait for initial connection
    "command_timeout": 30,     # Seconds to wait for command completion
    
    # Retry Settings
    "max_retries": 3,          # Number of connection retry attempts
    "retry_delay": 1,          # Seconds between retry attempts
    
    # Health Check Settings
    "health_check_interval": 30,  # Seconds between health checks
    "max_connection_age": 3600,   # Seconds before forcing connection refresh (1 hour)
}

# Database connection URL parameters for Supabase pooler
SUPABASE_POOLER_PARAMS = {
    "pooler": "true",
    "connection_limit": "1",    # Limit per client connection
    "pool_timeout": "20",       # Pool timeout in seconds
    "statement_timeout": "30000",  # Statement timeout in milliseconds
}

# Environment-specific configurations
ENVIRONMENT_CONFIGS = {
    "production": {
        "pool_size": 8,
        "max_overflow": 15,
        "pool_timeout": 20,
        "log_level": "WARNING"
    },
    "staging": {
        "pool_size": 5,
        "max_overflow": 10,
        "pool_timeout": 15,
        "log_level": "INFO"
    },
    "development": {
        "pool_size": 3,
        "max_overflow": 5,
        "pool_timeout": 10,
        "log_level": "DEBUG"
    }
}

# Connection monitoring thresholds
MONITORING_THRESHOLDS = {
    "connection_usage_warning": 0.7,  # Warn when 70% of pool is used
    "connection_usage_critical": 0.9,  # Critical when 90% of pool is used
    "response_time_warning": 5.0,     # Warn if DB response > 5 seconds
    "response_time_critical": 10.0,   # Critical if DB response > 10 seconds
}

# Recommended production environment variables
PRODUCTION_ENV_VARS = """
# Database optimization for production
DATABASE_URL=postgresql+asyncpg://user:pass@host:port/db
DB_POOL_SIZE=8
DB_MAX_OVERFLOW=15
DB_POOL_TIMEOUT=20
DB_POOL_RECYCLE=1800
DB_POOL_PRE_PING=true

# Logging
LOG_LEVEL=WARNING
DEBUG=false

# FastAPI
WORKERS=4
WORKER_CLASS=uvicorn.workers.UvicornWorker
MAX_REQUESTS=1000
MAX_REQUESTS_JITTER=100
TIMEOUT=30
KEEP_ALIVE=2
"""
