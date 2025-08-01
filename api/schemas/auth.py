"""
Authentication schemas for She&Soul FastAPI application
"""

from pydantic import BaseModel, EmailStr, Field
from typing import Optional
from datetime import datetime

class LoginRequest(BaseModel):
    """Login request schema"""
    email: EmailStr = Field(..., description="User email")
    password: str = Field(..., min_length=6, description="User password")

class SignUpRequest(BaseModel):
    """Sign up request schema"""
    email: EmailStr = Field(..., description="User email")
    password: str = Field(..., min_length=6, description="User password")
    name: str = Field(..., min_length=1, description="User name")
    nick_name: Optional[str] = Field(None, description="User nickname")
    user_type: str = Field(..., description="User type (USER or PARTNER)")
    age: Optional[int] = Field(None, ge=0, le=120, description="User age")
    height: Optional[float] = Field(None, ge=0, description="User height")
    weight: Optional[float] = Field(None, ge=0, description="User weight")
    referral_code: Optional[str] = Field(None, description="Referral code")
    referred_code: Optional[str] = Field(None, description="Referred code")

class VerifyEmailRequest(BaseModel):
    """Email verification request schema"""
    email: EmailStr = Field(..., description="User email")
    otp: str = Field(..., min_length=6, max_length=6, description="OTP code")

class ResendOtpRequest(BaseModel):
    """Resend OTP request schema"""
    email: EmailStr = Field(..., description="User email")

class AuthResponse(BaseModel):
    """Authentication response schema"""
    access_token: str = Field(..., description="JWT access token")
    token_type: str = Field(default="bearer", description="Token type")

class SignUpResponse(BaseModel):
    """Sign up response schema"""
    message: str = Field(..., description="Success message")
    user_id: int = Field(..., description="User ID")
    jwt: str = Field(..., description="JWT access token")

class LoginResponse(BaseModel):
    """Login response schema"""
    message: str = Field(..., description="Success message")
    user_id: int = Field(..., description="User ID")
    email: str = Field(..., description="User email")

class VerifyEmailResponse(BaseModel):
    """Email verification response schema"""
    message: str = Field(..., description="Success message")

class ResendOtpResponse(BaseModel):
    """Resend OTP response schema"""
    message: str = Field(..., description="Success message") 