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
    """Profile creation/update request schema"""
    name: str = Field(..., min_length=1, description="User name")
    nick_name: Optional[str] = Field(None, description="User nickname")
    user_type: UserType = Field(..., description="User type")
    age: Optional[int] = Field(None, ge=0, le=120, description="User age")
    height: Optional[float] = Field(None, ge=0, description="User height")
    weight: Optional[float] = Field(None, ge=0, description="User weight")
    referral_code: Optional[str] = Field(None, description="Referral code")
    referred_code: Optional[str] = Field(None, description="Referred code")
    preferred_service_type: Optional[UserServiceType] = Field(None, description="Preferred service type")
    period_length: Optional[int] = Field(None, ge=1, le=31, description="Period length in days")
    cycle_length: Optional[int] = Field(None, ge=21, le=35, description="Cycle length in days")
    last_period_start_date: Optional[date] = Field(None, description="Last period start date")
    last_period_end_date: Optional[date] = Field(None, description="Last period end date")
    device_token: Optional[str] = Field(None, description="Device token for notifications")
    language_code: Optional[str] = Field(None, description="Language code")

class ProfileResponse(BaseModel):
    """Profile response schema"""
    id: int = Field(..., description="Profile ID")
    name: str = Field(..., description="User name")
    nick_name: Optional[str] = Field(None, description="User nickname")
    user_type: UserType = Field(..., description="User type")
    age: Optional[int] = Field(None, description="User age")
    height: Optional[float] = Field(None, description="User height")
    weight: Optional[float] = Field(None, description="User weight")
    referral_code: Optional[str] = Field(None, description="Referral code")
    referred_code: Optional[str] = Field(None, description="Referred code")
    preferred_service_type: Optional[UserServiceType] = Field(None, description="Preferred service type")
    period_length: Optional[int] = Field(None, description="Period length in days")
    cycle_length: Optional[int] = Field(None, description="Cycle length in days")
    last_period_start_date: Optional[date] = Field(None, description="Last period start date")
    last_period_end_date: Optional[date] = Field(None, description="Last period end date")
    device_token: Optional[str] = Field(None, description="Device token for notifications")
    language_code: Optional[str] = Field(None, description="Language code")
    risk_assessment_mcq_data: Optional[Dict[str, Any]] = Field(None, description="Risk assessment MCQ data")
    breast_cancer_risk_level: Optional[str] = Field(None, description="Breast cancer risk level")
    medical_summary: Optional[Dict[str, Any]] = Field(None, description="Medical summary")

class ProfileServiceDto(BaseModel):
    """Profile service update schema"""
    preferred_service_type: UserServiceType = Field(..., description="Preferred service type")

class MenstrualTrackingDto(BaseModel):
    """Menstrual tracking data schema"""
    period_length: Optional[int] = Field(None, ge=1, le=31, description="Period length in days")
    cycle_length: Optional[int] = Field(None, ge=21, le=35, description="Cycle length in days")
    last_period_start_date: Optional[date] = Field(None, description="Last period start date")
    last_period_end_date: Optional[date] = Field(None, description="Last period end date")

class PartnerDataDto(BaseModel):
    """Partner data schema"""
    user_id: int = Field(..., description="User ID")
    name: str = Field(..., description="User name")
    nick_name: Optional[str] = Field(None, description="User nickname")
    age: Optional[int] = Field(None, description="User age")
    period_length: Optional[int] = Field(None, description="Period length in days")
    cycle_length: Optional[int] = Field(None, description="Cycle length in days")
    last_period_start_date: Optional[date] = Field(None, description="Last period start date")
    last_period_end_date: Optional[date] = Field(None, description="Last period end date")

class CyclePredictionDto(BaseModel):
    """Cycle prediction schema"""
    next_period_date: date = Field(..., description="Predicted next period date")
    fertile_window_start: date = Field(..., description="Fertile window start date")
    fertile_window_end: date = Field(..., description="Fertile window end date")
    ovulation_date: date = Field(..., description="Predicted ovulation date") 