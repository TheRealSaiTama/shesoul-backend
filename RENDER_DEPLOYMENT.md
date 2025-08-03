# She&Soul Backend - Render Deployment Guide

This guide will help you deploy the She&Soul FastAPI backend to Render.

## Prerequisites

- GitHub account with access to this repository
- Render account (free tier available)
- PostgreSQL database (or use Render's PostgreSQL service)

## Deployment Steps

### 1. Repository Setup

The repository is already configured for Render deployment with:
- ✅ `Procfile` - Specifies how to run the application
- ✅ `requirements.txt` - Python dependencies
- ✅ `runtime.txt` - Python version specification
- ✅ Working FastAPI application in `/backend` directory

### 2. Create New Web Service on Render

1. Go to [Render Dashboard](https://dashboard.render.com/)
2. Click "New +" → "Web Service"
3. Connect your GitHub repository: `TheRealSaiTama/shesoul-backend`
4. Configure the service:

   **Basic Settings:**
   - **Name**: `shesoul-backend` (or your preferred name)
   - **Region**: Choose closest to your users
   - **Branch**: `main` (or your deployment branch)
   - **Root Directory**: Leave empty (deploy from repository root)
   - **Runtime**: `Python 3`
   - **Build Command**: `pip install -r requirements.txt`
   - **Start Command**: Leave empty (will use Procfile)

### 3. Environment Variables

Set the following environment variables in Render:

**Required:**
```
DATABASE_URL=postgresql+asyncpg://user:password@host:port/database
SECRET_KEY=your-super-secret-key-here
```

**Optional (with defaults):**
```
DEBUG=false
SMTP_HOST=smtp.zoho.in
SMTP_PORT=587
SMTP_USERNAME=support@sheandsoul.co.in
SMTP_PASSWORD=your-email-password
GEMINI_API_KEY=your-gemini-api-key
```

### 4. Database Setup

**Option A: Use Render PostgreSQL (Recommended)**
1. In Render Dashboard, create a new PostgreSQL database
2. Copy the "External Database URL" 
3. Use this URL as your `DATABASE_URL` environment variable

**Option B: Use Supabase or External Database**
1. Ensure your database is accessible from Render
2. Use the connection string format: `postgresql+asyncpg://user:password@host:port/database`

### 5. Deploy

1. Click "Create Web Service"
2. Render will automatically:
   - Install dependencies from `requirements.txt`
   - Start the application using the `Procfile`
   - Assign a URL like `https://your-service-name.onrender.com`

## Application Details

### Current Configuration

- **Framework**: FastAPI
- **ASGI Server**: Gunicorn with Uvicorn workers
- **Database**: PostgreSQL with async support (asyncpg)
- **Authentication**: JWT tokens
- **CORS**: Configured for cross-origin requests

### API Endpoints

Once deployed, your API will be available at:
- **Root**: `https://your-app.onrender.com/` - Basic info
- **Health Check**: `https://your-app.onrender.com/health` - Service status
- **API Documentation**: `https://your-app.onrender.com/docs` - Interactive Swagger UI
- **ReDoc Documentation**: `https://your-app.onrender.com/redoc` - Alternative docs

### API Routes Structure
- `/api/auth` - Authentication endpoints
- `/api/app` - Application-specific endpoints  
- `/api/chat` - Chat functionality
- `/api/articles` - Article management
- `/api/dashboard` - Dashboard data
- `/api/pcos` - PCOS-related features
- `/api/reports` - Report generation

## Production Considerations

### Security
- [ ] Update `SECRET_KEY` to a strong, unique value
- [ ] Configure `ALLOWED_ORIGINS` for CORS to specific domains
- [ ] Use environment variables for all sensitive data
- [ ] Enable SSL/HTTPS (Render provides this automatically)

### Performance
- [ ] Configure database connection pooling
- [ ] Set up monitoring and logging
- [ ] Consider using Redis for caching (if needed)
- [ ] Monitor database performance

### Monitoring
- Use Render's built-in logs and metrics
- Set up health check monitoring
- Monitor database connections and performance

## Troubleshooting

### Common Issues

**1. Database Connection Errors**
- Verify `DATABASE_URL` format: `postgresql+asyncpg://user:password@host:port/database`
- Check database accessibility from Render
- Ensure database credentials are correct

**2. Application Won't Start**
- Check Render logs for specific error messages
- Verify all required environment variables are set
- Ensure `requirements.txt` includes all dependencies

**3. Import Errors**
- The application structure is configured to work from repository root
- Imports use `backend.main:app` path

### Debug Commands

Test locally before deploying:
```bash
# Install dependencies
pip install -r requirements.txt

# Test the application
uvicorn backend.main:app --host 0.0.0.0 --port 8000

# Or with gunicorn (production-like)
gunicorn backend.main:app -w 4 -k uvicorn.workers.UvicornWorker --bind 0.0.0.0:8000
```

## Support

- Check Render's [documentation](https://render.com/docs)
- Review application logs in Render dashboard
- Test endpoints with the built-in API documentation at `/docs`

---

**Ready to deploy!** 🚀 Your She&Soul FastAPI backend is configured and ready for production on Render.