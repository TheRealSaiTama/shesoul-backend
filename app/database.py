import os
from dotenv import load_dotenv
from supabase import create_client, Client

load_dotenv()

url: str = os.environ.get("SUPABASE_URL")
key: str = os.environ.get("SUPABASE_KEY")
supabase: Client = create_client(url, key)

def get_supabase_client():
    return supabase

def get_db():
    from app.db.models import SessionLocal
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()