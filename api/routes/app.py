"""
Main application routes for She&Soul FastAPI application
Based on Java implementation for production readiness
"""

from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import select
from typing import Dict, Any

from core.database import get_db
from core.security import get_current_user, create_access_token
from db.models.user import User
from db.models.profile import Profile, UserType, UserServiceType
from api.schemas.auth import SignUpRequest, SignUpResponse, LoginRequest, LoginResponse, VerifyEmailRequest, ResendOtpRequest
from api.schemas.profile import (
    ProfileRequest, ProfileResponse, ProfileServiceDto, MenstrualTrackingDto,
    PartnerDataDto, CyclePredictionDto
)
from services.app_service import AppService

router = APIRouter()
app_service = AppService()

@router.post("/signup", response_model=SignUpResponse)
def signup_user(
    signup_request: SignUpRequest,
    db: Session = Depends(get_db)
):
    """
    Register a new user (simplified like Java implementation)
    Only requires email and password, profile creation is separate
    """
    try:
        user = app_service.register_user(db, signup_request)
        
        # Create JWT token
        access_token = create_access_token(data={"sub": user.email})
        
        return SignUpResponse(
            message="User registered successfully! Please verify your email.",
            user_id=user.id,
            jwt=access_token
        )
        
    except ValueError as e:
        if "already in use" in str(e):
            raise HTTPException(
                status_code=status.HTTP_409_CONFLICT,
                detail=str(e)
            )
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail=str(e)
        )
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Registration failed: {str(e)}"
        )

@router.post("/verify-email")
def verify_email(
    request: VerifyEmailRequest,
    db: Session = Depends(get_db)
):
    """
    Verify user email with OTP
    """
    try:
        app_service.verify_email(db, request.email, request.otp)
        return {"message": "Email verified successfully!"}
        
    except ValueError as e:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail=str(e)
        )
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Email verification failed: {str(e)}"
        )

@router.post("/resend-otp")
def resend_otp(
    request: ResendOtpRequest,
    db: Session = Depends(get_db)
):
    """
    Resend OTP to user email
    """
    try:
        app_service.resend_otp(db, request.email)
        return {"message": "OTP resent successfully!"}
        
    except ValueError as e:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail=str(e)
        )
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"OTP resend failed: {str(e)}"
        )

@router.post("/login", response_model=LoginResponse)
def login_user(
    login_request: LoginRequest,
    db: Session = Depends(get_db)
):
    """
    Login user and return JWT token
    """
    try:
        user = app_service.login_user(db, login_request.email, login_request.password)
        
        # Create JWT token
        access_token = create_access_token(data={"sub": user.email})
        
        return LoginResponse(
            message="Login successful!",
            user_id=user.id,
            email=user.email,
            jwt=access_token
        )
        
    except ValueError as e:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail=str(e)
        )
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Login failed: {str(e)}"
        )

@router.post("/profile", response_model=ProfileResponse)
def create_profile(
    profile_request: ProfileRequest,
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user)
):
    """
    Create user profile
    """
    try:
        return app_service.create_profile(db, profile_request, current_user)
    except ValueError as e:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail=str(e)
        )
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Profile creation failed: {str(e)}"
        )

@router.get("/profile", response_model=ProfileResponse)
def get_profile(
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user)
):
    """
    Get user profile
    """
    try:
        profile = app_service.find_profile_by_user_id(db, current_user.id)
        return ProfileResponse.from_orm(profile)
    except ValueError as e:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=str(e)
        )
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Failed to get profile: {str(e)}"
        )

@router.put("/profile/service", response_model=ProfileServiceDto)
def update_service(
    service_dto: ProfileServiceDto,
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user)
):
    """
    Update user service preferences
    """
    try:
        return app_service.update_user_service(db, current_user.id, service_dto)
    except ValueError as e:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=str(e)
        )
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Failed to update service: {str(e)}"
        )

@router.put("/profile/menstrual-data", response_model=MenstrualTrackingDto)
def update_menstrual_data(
    update_dto: MenstrualTrackingDto,
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user)
):
    """
    Update menstrual tracking data
    """
    try:
        return app_service.update_menstrual_data(db, current_user.id, update_dto)
    except ValueError as e:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=str(e)
        )
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Failed to update menstrual data: {str(e)}"
        )

@router.get("/cycle-prediction", response_model=CyclePredictionDto)
def get_cycle_prediction(
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user)
):
    """
    Get next cycle prediction
    """
    try:
        return app_service.predict_next_cycle(db, current_user.id)
    except ValueError as e:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail=str(e)
        )
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Failed to get cycle prediction: {str(e)}"
        )

@router.get("/cycle-prediction-text", response_model=str)
def get_cycle_prediction_text(
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user)
):
    """
    Get cycle prediction as formatted text
    """
    try:
        return app_service.get_cycle_prediction_as_text(db, current_user.id)
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Failed to get cycle prediction text: {str(e)}"
        )

@router.get("/partner-data", response_model=PartnerDataDto)
def get_partner_data(
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user)
):
    """
    Get partner data for the logged-in partner
    """
    try:
        return app_service.get_partner_data(db, current_user.id)
    except ValueError as e:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=str(e)
        )
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Failed to get partner data: {str(e)}"
        )

@router.post("/mcq-risk-assessment", response_model=str)
def process_mcq_risk_assessment(
    answers: Dict[str, str],
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user)
):
    """
    Process MCQ risk assessment and return risk level
    """
    try:
        return app_service.process_mcq_risk_assessment(db, current_user.id, answers)
    except ValueError as e:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=str(e)
        )
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Failed to process MCQ risk assessment: {str(e)}"
        )

@router.put("/profile/basic")
async def update_profile_basic(
    basic_info: Dict[str, Any],
    current_user: User = Depends(get_current_user),
    db: AsyncSession = Depends(get_db)
):
    """
    Update basic profile information (age, height, weight, name, nickname)
    """
    try:
        result = await db.execute(select(Profile).where(Profile.user_id == current_user.id))
        profile = result.scalar_one_or_none()
        
        if not profile:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="Profile not found"
            )
        
        # Update only provided fields
        if "age" in basic_info and basic_info["age"] is not None:
            if not isinstance(basic_info["age"], int) or basic_info["age"] < 0 or basic_info["age"] > 120:
                raise HTTPException(
                    status_code=status.HTTP_400_BAD_REQUEST,
                    detail="Age must be between 0 and 120"
                )
            profile.age = basic_info["age"]
        
        if "height" in basic_info and basic_info["height"] is not None:
            if not isinstance(basic_info["height"], (int, float)) or basic_info["height"] <= 0:
                raise HTTPException(
                    status_code=status.HTTP_400_BAD_REQUEST,
                    detail="Height must be a positive number"
                )
            profile.height = float(basic_info["height"])
        
        if "weight" in basic_info and basic_info["weight"] is not None:
            if not isinstance(basic_info["weight"], (int, float)) or basic_info["weight"] <= 0:
                raise HTTPException(
                    status_code=status.HTTP_400_BAD_REQUEST,
                    detail="Weight must be a positive number"
                )
            profile.weight = float(basic_info["weight"])
        
        if "name" in basic_info and basic_info["name"] is not None:
            if not isinstance(basic_info["name"], str) or len(basic_info["name"].strip()) == 0:
                raise HTTPException(
                    status_code=status.HTTP_400_BAD_REQUEST,
                    detail="Name must be a non-empty string"
                )
            profile.name = basic_info["name"].strip()
        
        if "nick_name" in basic_info:
            if basic_info["nick_name"] is not None and not isinstance(basic_info["nick_name"], str):
                raise HTTPException(
                    status_code=status.HTTP_400_BAD_REQUEST,
                    detail="Nickname must be a string or null"
                )
            profile.nick_name = basic_info["nick_name"]
        
        await db.commit()
        
        return {"message": "Profile updated successfully"}
        
    except HTTPException:
        raise
    except Exception as e:
        await db.rollback()
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Profile update failed: {str(e)}"
        )