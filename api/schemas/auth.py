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
    """Sign up request schema - simplified like Java implementation"""
    email: EmailStr = Field(..., description="User email")
    password: str = Field(..., min_length=8, description="User password (minimum 8 characters)")

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
    jwt: str = Field(..., description="JWT access token")

class VerifyEmailResponse(BaseModel):
    """Email verification response schema"""
    message: str = Field(..., description="Success message")

class ResendOtpResponse(BaseModel):
    """Resend OTP response schema"""
    message: str = Field(..., description="Success message")