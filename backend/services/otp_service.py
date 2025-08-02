"""
OTP Generation Service for She&Soul FastAPI application
Based on Java implementation
"""

import secrets
from datetime import datetime, timedelta
from typing import Optional
from sqlalchemy.orm import Session
from sqlalchemy import select, delete, update
from db.models.otp import Otp

class OtpGenerationService:
    """Service for generating and managing OTPs"""
    
    OTP_LENGTH = 6
    OTP_EXPIRY_MINUTES = 10
    
    def __init__(self):
        pass
    
    def generate_otp(self) -> str:
        """Generate a 6-digit OTP"""
        return str(secrets.randbelow(900000) + 100000)
    
    def store_otp(self, db: Session, email: str, otp: str) -> None:
        """Store OTP in database with expiration time"""
        otp_entity = Otp(
            email=email,
            otp_code=otp,
            created_at=datetime.utcnow(),
            expires_at=datetime.utcnow() + timedelta(minutes=self.OTP_EXPIRY_MINUTES),
            used=False
        )
        
        db.add(otp_entity)
        db.commit()
    
    def get_latest_otp(self, db: Session, email: str) -> Optional[str]:
        """Get the latest valid OTP for an email"""
        now = datetime.utcnow()
        
        otp_entity = db.query(Otp).filter(
            Otp.email == email,
            Otp.expires_at > now,
            Otp.used == False
        ).order_by(Otp.created_at.desc()).first()
        
        return otp_entity.otp_code if otp_entity else None
    
    def is_otp_valid(self, db: Session, email: str, otp_code: str) -> Optional[Otp]:
        """Check if the provided OTP is valid and return it if found"""
        now = datetime.utcnow()
        
        otp_entity = db.query(Otp).filter(
            Otp.email == email,
            Otp.otp_code == otp_code,
            Otp.expires_at > now,
            Otp.used == False
        ).first()
        
        return otp_entity
    
    def mark_otp_as_used(self, db: Session, otp: Otp) -> None:
        """Mark a specific OTP as used"""
        otp.used = True
        db.commit()
    
    def clear_otps(self, db: Session, email: str) -> None:
        """Clear all OTPs for an email"""
        db.query(Otp).filter(Otp.email == email).delete()
        db.commit()
    
    def cleanup_expired_otps(self, db: Session) -> None:
        """Clean up expired OTPs"""
        now = datetime.utcnow()
        db.query(Otp).filter(Otp.expires_at <= now).delete()
        db.commit()
