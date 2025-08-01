"""
OTP model for She&Soul FastAPI application
"""

from sqlalchemy import Column, Integer, String, DateTime, Boolean
from core.database import Base

class Otp(Base):
    """OTP model for email verification"""
    __tablename__ = "otps"
    
    id = Column(Integer, primary_key=True, index=True)
    email = Column(String, nullable=False, index=True)
    otp_code = Column(String, nullable=False)
    used = Column(Boolean, default=False)
    expires_at = Column(DateTime, nullable=False)
    created_at = Column(DateTime, nullable=False)
    
    def __repr__(self):
        return f"<Otp(id={self.id}, email='{self.email}', used={self.used})>" 