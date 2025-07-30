from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from app.db.models import Profile, Article, MenstrualCycle
from app.dependencies import get_current_user
from app.db.models import SessionLocal
from datetime import date, timedelta

router = APIRouter()

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@router.get("/dashboard/me")
async def get_my_dashboard(user: dict = Depends(get_current_user), db: Session = Depends(get_db)):
    # Fetch user profile
    profile = db.query(Profile).filter(Profile.user_id == user['id']).first()

    # Fetch upcoming cycle prediction
    today = date.today()
    last_cycle = db.query(MenstrualCycle).filter(MenstrualCycle.user_id == user['id']).order_by(MenstrualCycle.last_period_date.desc()).first()
    if last_cycle:
        next_period_date = last_cycle.last_period_date + timedelta(days=last_cycle.cycle_length)
    else:
        next_period_date = today + timedelta(days=28)

    # Fetch recent articles
    articles = db.query(Article).limit(5).all()

    return {
        "profile": profile,
        "next_period_date": next_period_date,
        "articles": articles
    }
