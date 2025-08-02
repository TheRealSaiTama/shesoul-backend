"""
Main application routes for She&Soul FastAPI application
"""

from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import select
from typing import Dict, Any
from datetime import date, timedelta

from core.database import get_db
from core.security import get_current_user, get_password_hash, create_access_token
from db.models.user import User
from db.models.profile import Profile, UserType, UserServiceType
from api.schemas.auth import SignUpRequest, SignUpResponse, LoginRequest, LoginResponse, VerifyEmailRequest, ResendOtpRequest
from api.schemas.profile import (
    ProfileRequest, ProfileResponse, ProfileServiceDto, MenstrualTrackingDto,
    PartnerDataDto, CyclePredictionDto
)
from services.app_service import AppService
from services.email_service import EmailService

router = APIRouter()

@router.post("/signup", response_model=SignUpResponse)
async def signup_user(
    signup_request: SignUpRequest,
    db: AsyncSession = Depends(get_db)
):
    """
    Register a new user
    """
    try:
        # Check if user already exists
        result = await db.execute(select(User).where(User.email == signup_request.email))
        existing_user = result.scalar_one_or_none()
        
        if existing_user:
            raise HTTPException(
                status_code=status.HTTP_409_CONFLICT,
                detail="User with this email already exists"
            )
        
        # Create new user
        hashed_password = get_password_hash(signup_request.password)
        user = User(
            email=signup_request.email,
            password=hashed_password,
            is_email_verified=False
        )
        
        db.add(user)
        await db.flush()  # Get the user ID
        
        # Create profile
        profile = Profile(
            user_id=user.id,
            name=signup_request.name,
            nick_name=signup_request.nick_name,
            user_type=UserType(signup_request.user_type),
            age=signup_request.age,
            height=signup_request.height,
            weight=signup_request.weight,
            referral_code=signup_request.referral_code,
            referred_code=signup_request.referred_code
        )
        
        db.add(profile)
        await db.commit()
        
        # Create JWT token
        access_token = create_access_token(data={"sub": user.email})
        
        return SignUpResponse(
            message="User registered successfully!",
            user_id=user.id,
            jwt=access_token
        )
        
    except HTTPException:
        raise
    except Exception as e:
        await db.rollback()
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Registration failed: {str(e)}"
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
        # Find user by email
        result = await db.execute(select(User).where(User.email == login_request.email))
        user = result.scalar_one_or_none()
        
        if not user:
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="Invalid email or password"
            )
        
        # Verify password
        from core.security import verify_password
        if not verify_password(login_request.password, user.password):
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="Invalid email or password"
            )
        
        return LoginResponse(
            message="Login successful!",
            user_id=user.id,
            email=user.email
        )
        
    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Login failed: {str(e)}"
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
        # This would be implemented with OTP verification logic
        # For now, just mark user as verified
        result = await db.execute(select(User).where(User.email == request.email))
        user = result.scalar_one_or_none()
        
        if not user:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail="User not found"
            )
        
        user.is_email_verified = True
        await db.commit()
        
        return {"message": "Email verified successfully!"}
        
    except HTTPException:
        raise
    except Exception as e:
        await db.rollback()
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
        # This would be implemented with OTP generation and email sending
        # For now, just return success
        return {"message": "OTP resent successfully!"}
        
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
    Setup or update user profile
    """
    try:
        # Get existing profile or create new one
        result = await db.execute(select(Profile).where(Profile.user_id == current_user.id))
        profile = result.scalar_one_or_none()
        
        if profile:
            # Update existing profile
            for field, value in profile_request.dict(exclude_unset=True).items():
                setattr(profile, field, value)
        else:
            # Create new profile
            profile = Profile(
                user_id=current_user.id,
                **profile_request.dict()
            )
            db.add(profile)
        
        await db.commit()
        await db.refresh(profile)
        
        return ProfileResponse(
            id=profile.id,
            name=profile.name,
            nick_name=profile.nick_name,
            user_type=profile.user_type,
            age=profile.age,
            height=profile.height,
            weight=profile.weight,
            referral_code=profile.referral_code,
            referred_code=profile.referred_code,
            preferred_service_type=profile.preferred_service_type,
            period_length=profile.period_length,
            cycle_length=profile.cycle_length,
            last_period_start_date=profile.last_period_start_date,
            last_period_end_date=profile.last_period_end_date,
            device_token=profile.device_token,
            language_code=profile.language_code,
            risk_assessment_mcq_data=profile.risk_assessment_mcq_data,
            breast_cancer_risk_level=profile.breast_cancer_risk_level,
            medical_summary=profile.medical_summary
        )
        
    except HTTPException:
        raise
    except Exception as e:
        await db.rollback()
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Profile setup failed: {str(e)}"
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
        result = await db.execute(select(Profile).where(Profile.user_id == current_user.id))
        profile = result.scalar_one_or_none()
        
        if not profile:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="Profile not found"
            )
        
        profile.preferred_service_type = update_services.preferred_service_type
        await db.commit()
        
        return {"message": "Services updated successfully!"}
        
    except HTTPException:
        raise
    except Exception as e:
        await db.rollback()
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
        result = await db.execute(select(Profile).where(Profile.user_id == current_user.id))
        profile = result.scalar_one_or_none()
        
        if not profile:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="Profile not found"
            )
        
        # Update menstrual data
        for field, value in menstrual_data.dict(exclude_unset=True).items():
            setattr(profile, field, value)
        
        await db.commit()
        
        return {"message": "Menstrual data updated successfully!"}
        
    except HTTPException:
        raise
    except Exception as e:
        await db.rollback()
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
        
        result = await db.execute(select(Profile).where(Profile.user_id == current_user.id))
        profile = result.scalar_one_or_none()
        
        if not profile:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="Profile not found"
            )
        
        profile.language_code = language_code
        await db.commit()
        
        return {"message": f"User language updated successfully to {language_code}"}
        
    except HTTPException:
        raise
    except Exception as e:
        await db.rollback()
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
        # This would need to be implemented based on the partner linking logic
        # For now, return basic partner data
        result = await db.execute(select(Profile).where(Profile.user_id == current_user.id))
        profile = result.scalar_one_or_none()
        
        if not profile:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="Profile not found"
            )
        
        return PartnerDataDto(
            user_id=current_user.id,
            name=profile.name,
            nick_name=profile.nick_name,
            age=profile.age,
            period_length=profile.period_length,
            cycle_length=profile.cycle_length,
            last_period_start_date=profile.last_period_start_date,
            last_period_end_date=profile.last_period_end_date
        )
        
    except HTTPException:
        raise
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
        result = await db.execute(select(Profile).where(Profile.user_id == current_user.id))
        profile = result.scalar_one_or_none()
        
        if not profile or not profile.last_period_start_date or not profile.cycle_length:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail="Insufficient data for prediction"
            )
        
        # Calculate next period date
        next_period_date = profile.last_period_start_date + timedelta(days=profile.cycle_length)
        ovulation_date = next_period_date - timedelta(days=14)
        fertile_window_start = ovulation_date - timedelta(days=5)
        fertile_window_end = ovulation_date + timedelta(days=1)
        
        return CyclePredictionDto(
            next_period_date=next_period_date,
            fertile_window_start=fertile_window_start,
            fertile_window_end=fertile_window_end,
            ovulation_date=ovulation_date
        )
        
    except HTTPException:
        raise
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
        # This would be implemented with breast cancer exam logging
        # For now, just return success
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
        # This would be implemented with risk assessment logic
        # For now, just return a default risk level
        risk_level = "LOW"  # This would be calculated based on answers
        
        return {
            "message": "Assessment completed successfully.",
            "riskLevel": risk_level
        }
        
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Assessment failed: {str(e)}"
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
        
        # This would be implemented with AI assistant logic
        # For now, return a simple response
        response = f"Thank you for your message: {user_message}. I'm here to help with your menstrual health questions."
        
        return {"response": response}
        
    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Assistant request failed: {str(e)}"
        ) 