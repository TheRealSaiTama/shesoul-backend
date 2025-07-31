import secrets
import string
from datetime import datetime, timedelta
from typing import Optional
from sqlalchemy.orm import Session
from app.models.otp import Otp
from app.services.email_service import EmailService

class OtpService:
    def __init__(self):
        self.email_service = EmailService()
        
    def generate_otp(self) -> str:
        """Generate a 6-digit OTP"""
        return ''.join(secrets.choice(string.digits) for _ in range(6))
    
    def store_otp(self, db: Session, email: str, otp: str) -> Otp:
        """Store OTP in database"""
        otp_entity = Otp(email=email, otp_code=otp)
        db.add(otp_entity)
        db.commit()
        db.refresh(otp_entity)
        return otp_entity
    
    def get_valid_otp(self, db: Session, email: str) -> Optional[Otp]:
        """Get the latest valid OTP for an email"""
        now = datetime.utcnow()
        return db.query(Otp).filter(
            Otp.email == email,
            Otp.used == False,
            Otp.expires_at > now
        ).order_by(Otp.created_at.desc()).first()
    
    def is_otp_valid(self, db: Session, email: str, otp_code: str) -> bool:
        """Check if OTP is valid"""
        now = datetime.utcnow()
        otp = db.query(Otp).filter(
            Otp.email == email,
            Otp.otp_code == otp_code,
            Otp.used == False,
            Otp.expires_at > now
        ).first()
        return otp is not None
    
    def mark_otp_as_used(self, db: Session, email: str) -> None:
        """Mark all OTPs for an email as used"""
        db.query(Otp).filter(Otp.email == email).update({"used": True})
        db.commit()
    
    def clear_otps(self, db: Session, email: str) -> None:
        """Delete all OTPs for an email"""
        db.query(Otp).filter(Otp.email == email).delete()
        db.commit()
    
    def cleanup_expired_otps(self, db: Session) -> None:
        """Delete expired OTPs"""
        now = datetime.utcnow()
        db.query(Otp).filter(Otp.expires_at < now).delete()
        db.commit()
    
    def send_otp_email(self, email: str, otp: str) -> bool:
        """Send OTP via email"""
        return self.email_service.send_otp_email(email, otp) 