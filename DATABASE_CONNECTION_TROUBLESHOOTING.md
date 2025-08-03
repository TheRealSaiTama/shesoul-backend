# Database Connection Troubleshooting Guide

## Issue: "Max client connections reached"

This error occurs when the application exhausts the available database connection pool. Here are the steps taken to resolve it:

### Root Causes
1. **Connection Pool Exhaustion**: Too many concurrent connections
2. **Connection Leaks**: Sessions not properly closed
3. **Async/Sync Mismatch**: Using sync database operations with async framework
4. **Supabase Connection Limits**: Pooler connection limits reached

### Solutions Implemented

#### 1. Fixed Async/Sync Database Session Mismatch
- **Problem**: Routes were using `Session` instead of `AsyncSession`
- **Solution**: Updated all routes to use `AsyncSession` with proper async/await patterns
- **Files Updated**: `api/routes/auth.py`, `core/security.py`

#### 2. Improved Connection Pool Configuration
- **Problem**: Default pool settings too restrictive for production
- **Solution**: Optimized pool settings in `core/database.py`:
  ```python
  pool_size=10          # Increased from 5
  max_overflow=20       # Increased from 10  
  pool_recycle=1800     # Increased from 300 seconds
  pool_timeout=30       # Added connection timeout
  ```

#### 3. Enhanced Session Management
- **Problem**: Database sessions not properly managed
- **Solution**: Improved `get_db()` dependency with:
  - Automatic commit on success
  - Proper rollback on errors
  - Guaranteed session cleanup
  - Error handling for session creation failures

#### 4. Added Connection Monitoring
- **Problem**: No visibility into connection pool usage
- **Solution**: Added monitoring capabilities:
  - Health check endpoints (`/health`, `/health/db`)
  - Connection pool status logging
  - Monitoring script (`scripts/monitor_db_connections.py`)

#### 5. Graceful Database Error Handling
- **Problem**: Application crashed when database unavailable
- **Solution**: Application continues with degraded functionality:
  - Startup doesn't fail on DB connection issues
  - Proper error responses for DB unavailability
  - Automatic connection cleanup on shutdown

#### 6. Production Configuration Optimization
- **Problem**: Development settings used in production
- **Solution**: Created production-optimized configuration:
  - Reduced worker count to limit concurrent connections
  - Added connection limits and timeouts
  - Optimized Procfile command

### Deployment Configuration

#### Updated Procfile
```
web: uvicorn main:app --host 0.0.0.0 --port $PORT --workers 2 --timeout-keep-alive 5 --max-requests 500
```

#### Environment Variables for Production
```env
DATABASE_URL=postgresql+asyncpg://...?pooler=true&connection_limit=1
DB_POOL_SIZE=5
DB_MAX_OVERFLOW=10
DB_POOL_TIMEOUT=20
WEB_CONCURRENCY=2
WORKERS=2
```

### Monitoring and Maintenance

#### Health Check Endpoints
- `GET /health` - Basic health with DB status
- `GET /health/db` - Detailed database health check

#### Connection Monitoring
Run the monitoring script to watch connection usage:
```bash
python scripts/monitor_db_connections.py
```

#### Logs to Watch
- Database connection pool status
- Session creation/cleanup errors
- Connection timeout warnings
- High connection usage alerts

### Prevention Strategies

1. **Connection Limits**: Always set reasonable pool limits
2. **Session Lifecycle**: Use dependency injection for session management
3. **Error Handling**: Implement graceful degradation
4. **Monitoring**: Regular health checks and alerting
5. **Testing**: Load testing to validate connection handling

### Supabase-Specific Considerations

1. **Pooler Mode**: Always use `?pooler=true` in connection string
2. **Connection Limits**: Set `connection_limit=1` for pooler connections
3. **Session Timeout**: Configure appropriate timeouts for pooler
4. **Regional Considerations**: Use appropriate pooler endpoint

### Quick Fixes for Immediate Relief

If experiencing connection issues in production:

1. **Restart the service** to clear stale connections
2. **Check Supabase dashboard** for connection metrics
3. **Reduce worker count** temporarily if high load
4. **Enable connection pooling** in database URL
5. **Monitor logs** for specific error patterns

### Long-term Optimization

1. **Database Query Optimization**: Reduce query execution time
2. **Caching Strategy**: Implement Redis for frequently accessed data
3. **Read Replicas**: Use read replicas for read-heavy operations
4. **Connection Pool Scaling**: Dynamic pool sizing based on load
5. **Database Sharding**: For very high-scale applications
