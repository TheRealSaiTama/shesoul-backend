#!/usr/bin/env python3
"""
Run script for She&Soul FastAPI application
"""

import uvicorn
import os
import sys
from pathlib import Path

def main():
    """Main function to run the FastAPI application"""
    
    # Add the current directory to Python path
    current_dir = Path(__file__).parent
    sys.path.insert(0, str(current_dir))
    
    # Set default environment variables if not set
    os.environ.setdefault("DEBUG", "true")
    os.environ.setdefault("LOG_LEVEL", "info")
    
    # Configuration
    host = os.getenv("HOST", "0.0.0.0")
    port = int(os.getenv("PORT", "8000"))
    reload = os.getenv("DEBUG", "true").lower() == "true"
    
    print(f"🚀 Starting She&Soul FastAPI Backend...")
    print(f"📍 Host: {host}")
    print(f"🔌 Port: {port}")
    print(f"🔄 Reload: {reload}")
    print(f"📚 Docs: http://{host}:{port}/docs")
    print(f"❤️  Health: http://{host}:{port}/health")
    print("-" * 50)
    
    # Run the application
    uvicorn.run(
        "main:app",
        host=host,
        port=port,
        reload=reload,
        log_level="info",
        access_log=True
    )

if __name__ == "__main__":
    main() 