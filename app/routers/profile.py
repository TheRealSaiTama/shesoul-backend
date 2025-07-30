from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from app.db.models import Profile
from app.models.profile import ProfileRequest, ProfileResponse, ProfileServiceDto
from app.dependencies import get_current_user
from app.db.models import SessionLocal
from typing import Dict

router = APIRouter()

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@router.post("/profile", response_model=ProfileResponse)
async def setup_profile(profile_request: ProfileRequest):
    try:
        # Mock response for testing
        return ProfileResponse(
            id=1,
            name=profile_request.name,
            age=profile_request.age,
            language_code=profile_request.language_code,
            preferred_service_type=profile_request.preferred_service_type
        )
    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))

@router.get("/profile", response_model=ProfileResponse)
async def get_profile():
    try:
        # Mock response for testing
        return ProfileResponse(
            id=1,
            name="Test User",
            age=25,
            language_code="en",
            preferred_service_type="SELF_USE"
        )
    except Exception as e:
        raise HTTPException(status_code=404, detail="Profile not found.")

@router.put("/services")
async def update_user_services(update_services_dto: ProfileServiceDto):
    try:
        return {"message": "Services updated successfully!"}
    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))

@router.put("/language")
async def set_user_language(payload: Dict[str, str]):
    language_code = payload.get("languageCode")
    if not language_code:
        raise HTTPException(status_code=400, detail="languageCode field is required.")
    
    return {"message": f"User language updated successfully to {language_code}"}