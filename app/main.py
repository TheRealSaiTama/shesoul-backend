from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.routers import auth, profile, article, assessment, chat, cycle, dashboard
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

# Create database tables
Base.metadata.create_all(bind=engine)
OtpBase.metadata.create_all(bind=engine)
UserBase.metadata.create_all(bind=engine)

# Include routers
app.include_router(auth.router, prefix="/api", tags=["Authentication"])
app.include_router(profile.router, prefix="/api", tags=["Profile"])
app.include_router(article.router, prefix="/api", tags=["Articles"])
app.include_router(assessment.router, prefix="/api", tags=["Assessment"])
app.include_router(chat.router, prefix="/api", tags=["Chat"])
app.include_router(cycle.router, prefix="/api", tags=["Cycle"])
app.include_router(dashboard.router, prefix="/api", tags=["Dashboard"])

@app.get("/")
async def root():
    return {"message": "Welcome to She's Soul API", "status": "running"}

@app.get("/health")
async def health_check():
    return {"status": "healthy", "message": "API is running"}
