"""
Cursor Auto Mode Active — Running on OpenAI GPT-4

She&Soul FastAPI Backend - Migrated from Java Spring Boot
"""

from fastapi import FastAPI, HTTPException, Depends
from fastapi.middleware.cors import CORSMiddleware
from fastapi.middleware.trustedhost import TrustedHostMiddleware
from contextlib import asynccontextmanager
import uvicorn
import logging
from loguru import logger
import sys

from core.config import settings
from core.database import engine, Base, test_db_connection
from api.routes import auth, app as app_router, chat, article, dashboard, pcos, report
from core.security import get_current_user

# Configure logging
logging.basicConfig(level=logging.INFO)
logger.remove()
logger.add(sys.stdout, level=logging.INFO, format="{time} | {level} | {message}")

@asynccontextmanager
async def lifespan(app: FastAPI):
    """Application lifespan events"""
    # Startup
    logger.info("Starting She&Soul FastAPI application...")
    
    # Test database connection (optional for deployment)
    try:
        if await test_db_connection():
            logger.info("Database connection successful")
        else:
            logger.warning("Database connection failed - continuing without database")
    except Exception as e:
        logger.warning(f"Database connection error - continuing without database: {e}")
    
    logger.info("Application startup complete")
    yield
    
    # Shutdown
    logger.info("Shutting down She&Soul FastAPI application...")

# Create FastAPI app
app = FastAPI(
    title="She&Soul API",
    description="FastAPI backend for She&Soul application - Migrated from Java Spring Boot",
    version="1.0.0",
    docs_url="/docs",
    redoc_url="/redoc",
    lifespan=lifespan
)

# Add CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=settings.ALLOWED_ORIGINS,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Add trusted host middleware for security
app.add_middleware(
    TrustedHostMiddleware,
    allowed_hosts=["*"]  # Configure based on your deployment environment
)

# Include API routes
app.include_router(auth.router, prefix="/api", tags=["Authentication"])
app.include_router(app_router.router, prefix="/api", tags=["App"])
app.include_router(chat.router, prefix="/api", tags=["Chat"])
app.include_router(article.router, prefix="/api", tags=["Articles"])
app.include_router(dashboard.router, prefix="/api", tags=["Dashboard"])
app.include_router(pcos.router, prefix="/api", tags=["PCOS"])
app.include_router(report.router, prefix="/api", tags=["Reports"])

@app.get("/")
async def root():
    """Root endpoint"""
    return {"message": "She&Soul API is running!", "version": "1.0.0"}

@app.get("/health")
async def health_check():
    """Health check endpoint"""
    return {"status": "healthy", "service": "She&Soul API", "version": "1.0.0"}

@app.get("/health/db")
async def health_check_db():
    """Database health check endpoint"""
    try:
        if await test_db_connection():
            return {"status": "healthy", "database": "connected"}
        else:
            return {"status": "degraded", "database": "disconnected"}
    except Exception as e:
        return {"status": "degraded", "database": "error", "message": str(e)}

@app.get("/protected")
async def protected_route(current_user = Depends(get_current_user)):
    """Protected route example"""
    return {"message": "This is a protected route", "user": current_user.email}

if __name__ == "__main__":
    uvicorn.run(
        "main:app",
        host="0.0.0.0",
        port=8000,
        reload=True,
        log_level="info"
    )