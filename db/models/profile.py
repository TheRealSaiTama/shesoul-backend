"""
Profile model for She&Soul FastAPI application
"""

from sqlalchemy import Column, Integer, String, Boolean, Date, Float, ForeignKey, Enum, Text
from sqlalchemy.orm import relationship
from sqlalchemy.dialects.postgresql import JSONB
import enum
from core.database import Base

class UserType(str, enum.Enum):
    """User type enumeration"""
    USER = "USER"
    PARTNER = "PARTNER"

class UserServiceType(str, enum.Enum):
    """User service type enumeration"""
    MENSTRUATION = "MENSTRUATION"
    BREAST_HEALTH = "BREAST_HEALTH"
    MENTAL_HEALTH = "MENTAL_HEALTH"
    PCOS = "PCOS"

class Profile(Base):
    """Profile model"""
    __tablename__ = "profiles"
    
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, nullable=False)
    nick_name = Column(String)
    
    user_type = Column(Enum(UserType), nullable=False)
    
    age = Column(Integer)
    height = Column(Float)
    weight = Column(Float)
    
    referral_code = Column(String, unique=True)
    referred_code = Column(String)
    
    # Foreign key to user
    user_id = Column(Integer, ForeignKey("users.id"), nullable=False)
    
    # Service preferences
    preferred_service_type = Column(Enum(UserServiceType))
    
    # Menstrual tracking
    period_length = Column(Integer)
    cycle_length = Column(Integer)
    last_period_start_date = Column(Date)
    last_period_end_date = Column(Date)
    
    # Device and language
    device_token = Column(Text)
    language_code = Column(String)
    
    # Risk assessment data (JSONB for PostgreSQL)
    risk_assessment_mcq_data = Column(JSONB)
    breast_cancer_risk_level = Column(String)
    
    # Medical summary (JSONB)
    medical_summary = Column(JSONB)
    
    # Relationship
    user = relationship("User", back_populates="profile")
    
    def __repr__(self):
        return f"<Profile(id={self.id}, name='{self.name}', user_type='{self.user_type}')>" 