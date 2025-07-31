import os
from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker
from app.models.otp import Otp, Base as OtpBase

# Database configuration
DATABASE_URL = os.getenv("DATABASE_URL", "postgresql://postgres.ctinouusmtsuloosqdyl:One2#fourfive@aws-0-ap-south-1.pooler.supabase.com:5432/postgres")

# Create engine with connection retry logic
try:
    engine = create_engine(DATABASE_URL, pool_pre_ping=True, pool_recycle=300)
    SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
    
    # Test the connection
    with engine.connect() as conn:
        conn.execute("SELECT 1")
    print("Database connection successful")
    
except Exception as e:
    print(f"Database connection failed: {e}")
    # Create a mock engine for development
    engine = None
    SessionLocal = None

# Create all tables only if engine exists
if engine:
    Base = declarative_base()
    try:
        Base.metadata.create_all(bind=engine)
        OtpBase.metadata.create_all(bind=engine)
        print("Database tables created successfully")
    except Exception as e:
        print(f"Failed to create tables: {e}")
else:
    Base = declarative_base()

def get_db():
    if SessionLocal is None:
        # Return a mock session if database is not available
        class MockSession:
            def add(self, obj):
                pass
            def commit(self):
                pass
            def refresh(self, obj):
                pass
            def query(self, model):
                return MockQuery()
            def close(self):
                pass
        
        class MockQuery:
            def filter(self, *args):
                return self
            def first(self):
                return None
            def all(self):
                return []
        
        yield MockSession()
    else:
        db = SessionLocal()
        try:
            yield db
        finally:
            db.close()