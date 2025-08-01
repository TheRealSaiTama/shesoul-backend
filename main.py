"""
Cursor Auto Mode Active — Running on OpenAI GPT-4

She&Soul FastAPI Backend - Migrated from Java Spring Boot
"""

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
import uvicorn

# Create FastAPI app
app = FastAPI(
    title="She&Soul API",
    description="FastAPI backend for She&Soul application - Migrated from Java Spring Boot",
    version="1.0.0",
    docs_url="/docs",
    redoc_url="/redoc"
)

# Add CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # Configure based on your deployment environment
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/")
async def root():
    """Root endpoint"""
    return {"message": "She&Soul API is running!", "version": "1.0.0"}

@app.get("/health")
async def health_check():
    """Health check endpoint"""
    return {"status": "healthy", "service": "She&Soul API"}

@app.get("/api/test")
async def test_endpoint():
    """Test endpoint"""
    return {"message": "API is working correctly!"}

if __name__ == "__main__":
    uvicorn.run(
        "main:app",
        host="0.0.0.0",
        port=8000,
        reload=True,
        log_level="info"
    ) 