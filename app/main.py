from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.db.models import engine, Base
from app.models.otp import Otp, Base as OtpBase
from app.models.user import User, Base as UserBase

app = FastAPI(
    title="She's Soul API",
    version="1.0.0"
)

# Add CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Create database tables only if engine exists
if engine:
    try:
        Base.metadata.create_all(bind=engine)
        OtpBase.metadata.create_all(bind=engine)
        UserBase.metadata.create_all(bind=engine)
        print("All database tables created successfully")
    except Exception as e:
        print(f"Failed to create database tables: {e}")
else:
    print("Database not available - running in mock mode")

# Include routers with error handling
try:
    from app.routers import auth
    app.include_router(auth.router, prefix="/api", tags=["Authentication"])
    print("Auth router loaded successfully")
except Exception as e:
    print(f"Failed to load auth router: {e}")

try:
    from app.routers import profile
    app.include_router(profile.router, prefix="/api", tags=["Profile"])
    print("Profile router loaded successfully")
except Exception as e:
    print(f"Failed to load profile router: {e}")

try:
    from app.routers import article
    app.include_router(article.router, prefix="/api", tags=["Articles"])
    print("Article router loaded successfully")
except Exception as e:
    print(f"Failed to load article router: {e}")

try:
    from app.routers import assessment
    app.include_router(assessment.router, prefix="/api", tags=["Assessment"])
    print("Assessment router loaded successfully")
except Exception as e:
    print(f"Failed to load assessment router: {e}")

try:
    from app.routers import chat
    app.include_router(chat.router, prefix="/api", tags=["Chat"])
    print("Chat router loaded successfully")
except Exception as e:
    print(f"Failed to load chat router: {e}")

try:
    from app.routers import cycle
    app.include_router(cycle.router, prefix="/api", tags=["Cycle"])
    print("Cycle router loaded successfully")
except Exception as e:
    print(f"Failed to load cycle router: {e}")

try:
    from app.routers import dashboard
    app.include_router(dashboard.router, prefix="/api", tags=["Dashboard"])
    print("Dashboard router loaded successfully")
except Exception as e:
    print(f"Failed to load dashboard router: {e}")

@app.get("/")
async def root():
    return {"message": "Welcome to She's Soul API", "status": "running"}

@app.get("/health")
async def health_check():
    return {"status": "healthy", "message": "API is running"}
