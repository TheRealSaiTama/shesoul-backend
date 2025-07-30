
from fastapi import APIRouter, HTTPException, Depends
from pydantic import BaseModel, EmailStr
from app.database import get_supabase_client
from supabase import Client

router = APIRouter()

class SignUpRequest(BaseModel):
    email: EmailStr
    password: str

class LoginRequest(BaseModel):
    email: EmailStr
    password: str

class ResendOtpRequest(BaseModel):
    email: str

class ResendOtpResponse(BaseModel):
    message: str

class VerifyEmailRequest(BaseModel):
    email: str
    otp: str

class VerifyEmailResponse(BaseModel):
    message: str
    token: str = None

class AuthResponse(BaseModel):
    access_token: str
    token_type: str = "bearer"
    expires_in: int = 3600
    refresh_token: str = None
    user: dict = None

@router.post("/signup", response_model=AuthResponse)
async def signup(request: SignUpRequest):
    try:
        # Mock response for testing
        return AuthResponse(
            access_token="mock_token_123",
            user={"id": "1", "email": request.email}
        )
    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))

@router.post("/login", response_model=AuthResponse)
async def login(request: LoginRequest):
    try:
        # Mock response for testing
        return AuthResponse(
            access_token="mock_token_123",
            user={"id": "1", "email": request.email}
        )
    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))

@router.post("/resend-otp", response_model=ResendOtpResponse)
async def resend_otp(request: ResendOtpRequest):
    try:
        return {"message": "If a user with this email exists, a new OTP has been sent."}
    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))

@router.post("/verify-email", response_model=VerifyEmailResponse)
async def verify_otp(request: VerifyEmailRequest):
    try:
        # Mock verification for testing
        return {"message": "Email verified successfully.", "token": "mock_verification_token"}
    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))
