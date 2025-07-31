from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from app.models.chat import ChatRequest, ChatResponse
from app.dependencies import get_current_user
from app.services.gemini_service import GeminiService
from app.db.models import get_db

router = APIRouter()
gemini_service = GeminiService()

@router.post("/chat", response_model=ChatResponse)
async def chat(request: ChatRequest, user: dict = Depends(get_current_user), db: Session = Depends(get_db)):
    try:
        ai_response = gemini_service.get_gemini_response(request.message)
        
        # TODO: Add chat history to database when ChatHistory model is created
        # db_chat = ChatHistory(user_id=user['id'], message=request.message, response=ai_response)
        # db.add(db_chat)
        # db.commit()

        return ChatResponse(response=ai_response)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
