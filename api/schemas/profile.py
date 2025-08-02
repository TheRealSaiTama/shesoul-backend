"""
Profile schemas for She&Soul FastAPI application
"""

from pydantic import BaseModel, Field
from typing import Optional, Dict, Any
from datetime import date
from enum import Enum

class UserType(str, Enum):
    """User type enumeration"""
    USER = "USER"
    PARTNER = "PARTNER"

class UserServiceType(str, Enum):
    """User service type enumeration"""
    MENSTRUATION = "MENSTRUATION"
    BREAST_HEALTH = "BREAST_HEALTH"
    MENTAL_HEALTH = "MENTAL_HEALTH"
    PCOS = "PCOS"

class ProfileRequest(BaseModel):
    """Profile creation/update request schema - matches Java implementation"""
    name: str = Field(..., min_length=1, description="User name")
    nickname: Optional[str] = Field(None, description="User nickname")
    user_type: UserType = Field(..., description="User type")
    age: Optional[int] = Field(None, ge=0, le=120, description="User age")
    height: Optional[float] = Field(None, ge=0, description="User height")
    weight: Optional[float] = Field(None, ge=0, description="User weight")
    preferred_service_type: Optional[UserServiceType] = Field(None, description="Preferred service type")
    referred_by_code: Optional[str] = Field(None, description="Referral code used to sign up")

class ProfileResponse(BaseModel):
    """Profile response schema - matches Java implementation"""
    id: int = Field(..., description="Profile ID")
    user_id: int = Field(..., description="User ID")
    name: str = Field(..., description="User name")
    email: str = Field(..., description="User email")
    user_type: UserType = Field(..., description="User type")
    referral_code: Optional[str] = Field(None, description="User's referral code")

class ProfileServiceDto(BaseModel):
    """Profile service update schema"""
    preferred_service_type: UserServiceType = Field(..., description="Preferred service type")

class MenstrualTrackingDto(BaseModel):
    """Menstrual tracking data schema"""
    period_length: Optional[int] = Field(None, ge=1, le=31, description="Period length in days")
    cycle_length: Optional[int] = Field(None, ge=21, le=35, description="Cycle length in days")
    last_period_start_date: Optional[date] = Field(None, description="Last period start date")
    last_period_end_date: Optional[date] = Field(None, description="Last period end date")

class CyclePredictionDto(BaseModel):
    """Cycle prediction schema - matches Java implementation"""
    next_period_start_date: Optional[date] = Field(None, description="Next period start date")
    next_period_end_date: Optional[date] = Field(None, description="Next period end date")
    next_follicular_start_date: Optional[date] = Field(None, description="Next follicular phase start")
    next_follicular_end_date: Optional[date] = Field(None, description="Next follicular phase end")
    next_ovulation_date: Optional[date] = Field(None, description="Next ovulation date")
    next_ovulation_end_date: Optional[date] = Field(None, description="Next ovulation end date")
    next_luteal_start_date: Optional[date] = Field(None, description="Next luteal phase start")
    next_luteal_end_date: Optional[date] = Field(None, description="Next luteal phase end")
    next_fertile_window_start_date: Optional[date] = Field(None, description="Next fertile window start")
    next_fertile_window_end_date: Optional[date] = Field(None, description="Next fertile window end")

class PartnerDataDto(BaseModel):
    """Partner data schema - matches Java implementation"""
    name: str = Field(..., description="Partner's linked user name")
    cycle_prediction: Optional[CyclePredictionDto] = Field(None, description="Cycle prediction data") 