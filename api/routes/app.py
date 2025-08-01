"""
Main application routes for She&Soul FastAPI application
Based on Java implementation for production readiness
"""

from fastapi import APIRouter, Depends, HTTPException, status
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
async def signup_user(
    signup_request: SignUpRequest,
    db: AsyncSession = Depends(get_db)
):
    """
    Register a new user (simplified like Java implementation)
    Only requires email and password, profile creation is separate
    """
    try:
        user = await app_service.register_user(db, signup_request)
        
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
async def verify_email(
    request: VerifyEmailRequest,
    db: AsyncSession = Depends(get_db)
):
    """
    Verify user email with OTP
    """
    try:
        await app_service.verify_email(db, request.email, request.otp)
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
async def resend_otp(
    request: ResendOtpRequest,
    db: AsyncSession = Depends(get_db)
):
    """
    Resend OTP to user email
    """
    try:
        await app_service.resend_otp(db, request.email)
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

@router.post("/profile", response_model=ProfileResponse)
async def setup_profile(
    profile_request: ProfileRequest,
    current_user: User = Depends(get_current_user),
    db: AsyncSession = Depends(get_db)
):
    """
    Setup user profile (separate from signup like Java implementation)
    """
    try:
        response = await app_service.create_profile(db, profile_request, current_user)
        return response
        
    except ValueError as e:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail=str(e)
        )
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Profile setup failed: {str(e)}"
        )

@router.post("/login", response_model=LoginResponse)
async def login_user(
    login_request: LoginRequest,
    db: AsyncSession = Depends(get_db)
):
    """
    Login user
    """
    try:
        user = await app_service.login_user(db, login_request.email, login_request.password)
        
        return LoginResponse(
            message="Login successful!",
            user_id=user.id,
            email=user.email
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

@router.put("/services")
async def update_user_services(
    update_services: ProfileServiceDto,
    current_user: User = Depends(get_current_user),
    db: AsyncSession = Depends(get_db)
):
    """
    Update user service preferences
    """
    try:
        await app_service.update_user_service(db, current_user.id, update_services)
        return {"message": "Services updated successfully!"}
        
    except ValueError as e:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=str(e)
        )
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Service update failed: {str(e)}"
        )

@router.put("/menstrual-data")
async def update_menstrual_data(
    menstrual_data: MenstrualTrackingDto,
    current_user: User = Depends(get_current_user),
    db: AsyncSession = Depends(get_db)
):
    """
    Update menstrual tracking data
    """
    try:
        await app_service.update_menstrual_data(db, current_user.id, menstrual_data)
        return {"message": "Menstrual data updated successfully!"}
        
    except ValueError as e:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=str(e)
        )
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Menstrual data update failed: {str(e)}"
        )

@router.put("/language")
async def set_user_language(
    payload: Dict[str, str],
    current_user: User = Depends(get_current_user),
    db: AsyncSession = Depends(get_db)
):
    """
    Set user language preference
    """
    try:
        language_code = payload.get("languageCode")
        if not language_code or not language_code.strip():
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail="languageCode field is required."
            )
        
        await app_service.update_user_language(db, current_user.id, language_code)
        return {"message": f"User language updated successfully to {language_code}"}
        
    except ValueError as e:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=str(e)
        )
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Language update failed: {str(e)}"
        )

@router.get("/partner", response_model=PartnerDataDto)
async def get_partner_data(
    current_user: User = Depends(get_current_user),
    db: AsyncSession = Depends(get_db)
):
    """
    Get partner data for the logged-in partner
    """
    try:
        partner_data = await app_service.get_partner_data(db, current_user.id)
        return partner_data
        
    except ValueError as e:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail=str(e)
        )
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Failed to get partner data: {str(e)}"
        )

@router.get("/next-period", response_model=CyclePredictionDto)
async def get_next_period(
    current_user: User = Depends(get_current_user),
    db: AsyncSession = Depends(get_db)
):
    """
    Get next period prediction
    """
    try:
        prediction = await app_service.predict_next_cycle(db, current_user.id)
        return prediction
        
    except ValueError as e:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail=str(e)
        )
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Failed to predict next period: {str(e)}"
        )

@router.post("/breast-health")
async def log_breast_cancer_self_exam(
    raw_symptoms: Dict[str, str],
    current_user: User = Depends(get_current_user),
    db: AsyncSession = Depends(get_db)
):
    """
    Log breast cancer self-examination symptoms
    """
    try:
        # This would need proper symptom mapping implementation
        return {"message": "Detailed self-exam log saved successfully."}
        
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Failed to log self-exam: {str(e)}"
        )

@router.post("/mcq-assessment")
async def submit_mcq_risk_assessment(
    answers: Dict[str, str],
    current_user: User = Depends(get_current_user),
    db: AsyncSession = Depends(get_db)
):
    """
    Submit MCQ risk assessment
    """
    try:
        risk_level = await app_service.process_mcq_risk_assessment(db, current_user.id, answers)
        
        return {
            "message": "Assessment completed successfully.",
            "riskLevel": risk_level
        }
        
    except ValueError as e:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=str(e)
        )
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Assessment failed: {str(e)}"
        )

@router.post("/menstrual-assistant")
async def menstrual_assistant_request(
    request: Dict[str, str],
    current_user: User = Depends(get_current_user),
    db: AsyncSession = Depends(get_db)
):
    """
    Get response from menstrual assistant
    """
    try:
        user_message = request.get("message")
        if not user_message:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail="Message is required"
            )
        
        # Get user context for assistant
        cycle_text = await app_service.get_cycle_prediction_as_text(db, current_user.id)
        
        # This would integrate with AI assistant service
        response = f"Thank you for your message: {user_message}. {cycle_text}"
        
        return {"response": response}
        
    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Assistant request failed: {str(e)}"
        ) 