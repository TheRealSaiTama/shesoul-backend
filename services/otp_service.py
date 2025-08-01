"""
OTP Generation Service for She&Soul FastAPI application
Based on Java implementation
"""

import secrets
from datetime import datetime, timedelta
from typing import Optional
from sqlalchemy.ext.asyncio import AsyncSession
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
    
    async def store_otp(self, db: AsyncSession, email: str, otp: str) -> None:
        """Store OTP in database with expiration time"""
        otp_entity = Otp(
            email=email,
            otp_code=otp,
            created_at=datetime.utcnow(),
            expires_at=datetime.utcnow() + timedelta(minutes=self.OTP_EXPIRY_MINUTES),
            is_used=False
        )
        
        db.add(otp_entity)
        await db.commit()
    
    async def get_latest_otp(self, db: AsyncSession, email: str) -> Optional[str]:
        """Get the latest valid OTP for an email"""
        now = datetime.utcnow()
        
        result = await db.execute(
            select(Otp)
            .where(
                Otp.email == email,
                Otp.expires_at > now,
                Otp.is_used == False
            )
            .order_by(Otp.created_at.desc())
            .limit(1)
        )
        
        otp_entity = result.scalar_one_or_none()
        return otp_entity.otp_code if otp_entity else None
    
    async def is_otp_valid(self, db: AsyncSession, email: str, otp_code: str) -> bool:
        """Check if the provided OTP is valid"""
        now = datetime.utcnow()
        
        result = await db.execute(
            select(Otp)
            .where(
                Otp.email == email,
                Otp.otp_code == otp_code,
                Otp.expires_at > now,
                Otp.is_used == False
            )
        )
        
        return result.scalar_one_or_none() is not None
    
    async def mark_otp_as_used(self, db: AsyncSession, email: str) -> None:
        """Mark all OTPs for an email as used"""
        await db.execute(
            update(Otp)
            .where(Otp.email == email)
            .values(is_used=True)
        )
        await db.commit()
    
    async def clear_otps(self, db: AsyncSession, email: str) -> None:
        """Clear all OTPs for an email"""
        await db.execute(
            delete(Otp)
            .where(Otp.email == email)
        )
        await db.commit()
    
    async def cleanup_expired_otps(self, db: AsyncSession) -> None:
        """Clean up expired OTPs"""
        now = datetime.utcnow()
        await db.execute(
            delete(Otp)
            .where(Otp.expires_at <= now)
        )
        await db.commit()
