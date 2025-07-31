import os
from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker
from app.models.otp import Otp, Base as OtpBase

# Database configuration
DATABASE_URL = os.getenv("DATABASE_URL", "postgresql://postgres.ctinouusmtsuloosqdyl:One2#fourfive@aws-0-ap-south-1.pooler.supabase.com:5432/postgres")

engine = create_engine(DATABASE_URL)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

# Create all tables
Base = declarative_base()
Base.metadata.create_all(bind=engine)
OtpBase.metadata.create_all(bind=engine)

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()