
from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from app.db.models import get_db
from app.services.otp_service import OtpService
from app.services.user_service import UserService
from app.models.auth import SignUpRequest, LoginRequest, ResendOtpRequest, VerifyEmailRequest
from app.models.auth import AuthResponse
import secrets

router = APIRouter()
otp_service = OtpService()
user_service = UserService()

@router.post("/signup", response_model=AuthResponse)
async def signup(request: SignUpRequest, db: Session = Depends(get_db)):
    try:
        # Create user in database
        user = user_service.create_user(db, request.email, request.password)
        
        # Generate OTP
        otp = otp_service.generate_otp()
        
        # Store OTP in database
        otp_service.store_otp(db, request.email, otp)
        
        # Send OTP via email
        email_sent = otp_service.send_otp_email(request.email, otp)
        
        if not email_sent:
            raise HTTPException(status_code=500, detail="Failed to send OTP email")
        
        return AuthResponse(
            access_token="temp_token_will_be_generated_after_verification",
            token_type="bearer",
            expires_in=3600,
            refresh_token=None,
            user={"id": str(user.id), "email": user.email}
        )
        
    except ValueError as e:
        raise HTTPException(status_code=400, detail=str(e))
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@router.post("/login", response_model=AuthResponse)
async def login(request: LoginRequest, db: Session = Depends(get_db)):
    try:
        # Verify user credentials
        user = user_service.verify_user_credentials(db, request.email, request.password)
        
        if not user:
            raise HTTPException(status_code=401, detail="Invalid email or password")
        
        # Check if email is verified
        if not user.is_email_verified:
            raise HTTPException(status_code=401, detail="Please verify your email first")
        
        return AuthResponse(
            access_token="mock_token_123",
            token_type="bearer",
            expires_in=3600,
            refresh_token=None,
            user={"id": str(user.id), "email": user.email}
        )
    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@router.post("/resend-otp")
async def resend_otp(request: ResendOtpRequest, db: Session = Depends(get_db)):
    try:
        # Check if user exists
        user = user_service.get_user_by_email(db, request.email)
        if not user:
            raise HTTPException(status_code=404, detail="User not found")
        
        # Generate new OTP
        otp = otp_service.generate_otp()
        
        # Clear old OTPs for this email
        otp_service.clear_otps(db, request.email)
        
        # Store new OTP
        otp_service.store_otp(db, request.email, otp)
        
        # Send new OTP via email
        email_sent = otp_service.send_otp_email(request.email, otp)
        
        if not email_sent:
            raise HTTPException(status_code=500, detail="Failed to send OTP email")
        
        return {"message": "OTP resent successfully!"}
        
    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@router.post("/verify-email")
async def verify_email(request: VerifyEmailRequest, db: Session = Depends(get_db)):
    try:
        # Check if user exists
        user = user_service.get_user_by_email(db, request.email)
        if not user:
            raise HTTPException(status_code=404, detail="User not found")
        
        # Validate OTP
        if not otp_service.is_otp_valid(db, request.email, request.otp):
            raise HTTPException(status_code=400, detail="Invalid or expired OTP")
        
        # Mark OTP as used
        otp_service.mark_otp_as_used(db, request.email)
        
        # Mark user email as verified
        user_service.mark_email_verified(db, request.email)
        
        return {"message": "Email verified successfully!"}
        
    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
