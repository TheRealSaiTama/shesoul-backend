from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from app.models.article import Article
from app.db.models import Article as DBArticle
from app.dependencies import get_current_user
from app.db.models import SessionLocal
from typing import List

router = APIRouter()

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@router.post("/article", response_model=Article)
async def create_article(article: Article, db: Session = Depends(get_db)):
    db_article = DBArticle(**article.dict())
    db.add(db_article)
    db.commit()
    db.refresh(db_article)
    return db_article

@router.get("/articles", response_model=List[Article])
async def get_articles(db: Session = Depends(get_db)):
    articles = db.query(DBArticle).all()
    return articles
