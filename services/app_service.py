"""
App service for She&Soul FastAPI application
Based on Java implementation
"""

from typing import Dict, Any, Optional
from datetime import datetime, date, timedelta
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import select
from db.models.user import User
from db.models.profile import Profile, UserType, UserServiceType
from api.schemas.auth import SignUpRequest
from api.schemas.profile import (
    ProfileRequest, ProfileResponse, ProfileServiceDto, 
    MenstrualTrackingDto, CyclePredictionDto, PartnerDataDto
)
from core.security import get_password_hash
from services.otp_service import OtpGenerationService
from services.email_service import EmailService
from services.referral_service import ReferralCodeService

class AppService:
    """Service class for application business logic - based on Java implementation"""
    
    def __init__(self):
        self.otp_service = OtpGenerationService()
        self.email_service = EmailService()
        self.referral_service = ReferralCodeService()
    
    async def register_user(self, db: AsyncSession, request: SignUpRequest) -> User:
        """
        Register a new user (simplified like Java implementation)
        Only requires email and password
        """
        # Check if user already exists
        result = await db.execute(select(User).where(User.email == request.email))
        existing_user = result.scalar_one_or_none()
        
        if existing_user:
            raise ValueError("Email already in use")
        
        # Create new user
        new_user = User(
            email=request.email,
            password=get_password_hash(request.password),
            is_email_verified=False
        )
        
        db.add(new_user)
        await db.flush()  # Get the user ID
        
        # Generate and send OTP
        otp = self.otp_service.generate_otp()
        await self.otp_service.store_otp(db, new_user.email, otp)
        await self.email_service.send_otp_email(new_user.email, otp)
        
        await db.commit()
        return new_user
    
    async def verify_email(self, db: AsyncSession, email: str, submitted_otp: str) -> None:
        """Verify user email with OTP"""
        # Find user
        result = await db.execute(select(User).where(User.email == email))
        user = result.scalar_one_or_none()
        
        if not user:
            raise ValueError(f"User not found with email: {email}")
        
        if user.is_email_verified:
            raise ValueError("Email is already verified.")
        
        # Validate OTP
        if not await self.otp_service.is_otp_valid(db, email, submitted_otp):
            raise ValueError("Invalid or expired OTP.")
        
        # Mark email as verified
        user.is_email_verified = True
        await self.otp_service.mark_otp_as_used(db, email)
        await db.commit()
    
    async def resend_otp(self, db: AsyncSession, email: str) -> None:
        """Resend OTP to user email"""
        # Find user
        result = await db.execute(select(User).where(User.email == email))
        user = result.scalar_one_or_none()
        
        if not user:
            raise ValueError(f"User not found with email: {email}")
        
        if user.is_email_verified:
            raise ValueError("Email is already verified.")
        
        # Generate and send new OTP
        otp = self.otp_service.generate_otp()
        await self.otp_service.store_otp(db, user.email, otp)
        await self.email_service.send_otp_email(user.email, otp)
    
    async def create_profile(self, db: AsyncSession, request: ProfileRequest, user: User) -> ProfileResponse:
        """
        Create user profile (separate from signup like Java implementation)
        """
        # Check if user already has profile
        result = await db.execute(select(Profile).where(Profile.user_id == user.id))
        existing_profile = result.scalar_one_or_none()
        
        if existing_profile:
            raise ValueError("User already has a profile.")
        
        # Create profile
        profile = Profile(
            user_id=user.id,
            name=request.name,
            nick_name=request.nickname,
            user_type=request.user_type
        )
        
        if request.user_type == UserType.USER:
            profile.age = request.age
            profile.height = request.height
            profile.weight = request.weight
            profile.preferred_service_type = request.preferred_service_type
            
            db.add(profile)
            await db.flush()  # Get profile ID
            
            # Generate referral code
            new_code = self.referral_service.generate_code(profile.id)
            
            # Ensure referral code is unique
            while True:
                existing_code = await db.execute(
                    select(Profile).where(Profile.referral_code == new_code)
                )
                if not existing_code.scalar_one_or_none():
                    break
                new_code = self.referral_service.generate_random_code()
            
            profile.referral_code = new_code
            
        elif request.user_type == UserType.PARTNER:
            if not request.referred_by_code:
                raise ValueError("Referral code is required for partner use.")
            
            # Validate referral code
            result = await db.execute(
                select(Profile).where(Profile.referral_code == request.referred_by_code)
            )
            if not result.scalar_one_or_none():
                raise ValueError(f"Invalid referral code: {request.referred_by_code}")
            
            profile.referred_code = request.referred_by_code
            db.add(profile)
        
        await db.commit()
        await db.refresh(profile)
        
        return ProfileResponse(
            id=profile.id,
            user_id=user.id,
            name=profile.name,
            email=user.email,
            user_type=profile.user_type,
            referral_code=profile.referral_code
        )
    
    async def login_user(self, db: AsyncSession, email: str, password: str) -> User:
        """Login user with email and password"""
        from core.security import verify_password
        
        result = await db.execute(select(User).where(User.email == email))
        user = result.scalar_one_or_none()
        
        if not user:
            raise ValueError("Invalid email or password")
        
        if not verify_password(password, user.password):
            raise ValueError("Invalid email or password")
        
        return user
    
    async def get_user_by_id(self, db: AsyncSession, user_id: int) -> User:
        """Get user by ID"""
        result = await db.execute(select(User).where(User.id == user_id))
        user = result.scalar_one_or_none()
        
        if not user:
            raise ValueError(f"User not found with ID: {user_id}")
        
        return user
    
    async def update_user_service(self, db: AsyncSession, user_id: int, service_dto: ProfileServiceDto) -> ProfileServiceDto:
        """Update user service preferences"""
        result = await db.execute(select(Profile).where(Profile.user_id == user_id))
        profile = result.scalar_one_or_none()
        
        if not profile:
            raise ValueError(f"Profile not found for user ID: {user_id}")
        
        profile.preferred_service_type = service_dto.preferred_service_type
        await db.commit()
        
        return ProfileServiceDto(preferred_service_type=profile.preferred_service_type)
    
    async def update_menstrual_data(self, db: AsyncSession, user_id: int, update_dto: MenstrualTrackingDto) -> MenstrualTrackingDto:
        """Update menstrual tracking data"""
        result = await db.execute(select(Profile).where(Profile.user_id == user_id))
        profile = result.scalar_one_or_none()
        
        if not profile:
            raise ValueError(f"Profile not found for user ID: {user_id}")
        
        # Update menstrual data
        if update_dto.last_period_start_date:
            profile.last_period_start_date = update_dto.last_period_start_date
        if update_dto.last_period_end_date:
            profile.last_period_end_date = update_dto.last_period_end_date
        if update_dto.period_length:
            profile.period_length = update_dto.period_length
        if update_dto.cycle_length:
            profile.cycle_length = update_dto.cycle_length
        
        await db.commit()
        return update_dto
    
    async def predict_next_cycle(self, db: AsyncSession, user_id: int) -> CyclePredictionDto:
        """Predict next menstrual cycle"""
        result = await db.execute(select(Profile).where(Profile.user_id == user_id))
        profile = result.scalar_one_or_none()
        
        if not profile:
            raise ValueError(f"Profile not found for user ID: {user_id}")
        
        last_period_date = profile.last_period_start_date
        cycle_length = profile.cycle_length
        period_length = profile.period_length
        
        if not last_period_date or not cycle_length or not period_length:
            raise ValueError("Insufficient data to predict next cycle.")
        
        # Calculate predictions (same logic as Java)
        next_period_start_date = last_period_date + timedelta(days=cycle_length)
        following_period_start_date = next_period_start_date + timedelta(days=cycle_length)
        next_ovulation_date = following_period_start_date - timedelta(days=14)
        
        return CyclePredictionDto(
            next_period_start_date=next_period_start_date,
            next_period_end_date=next_period_start_date + timedelta(days=period_length - 1),
            next_follicular_start_date=next_period_start_date,
            next_follicular_end_date=next_ovulation_date,
            next_ovulation_date=next_ovulation_date,
            next_ovulation_end_date=next_ovulation_date + timedelta(days=1),
            next_luteal_start_date=next_ovulation_date + timedelta(days=1),
            next_luteal_end_date=next_ovulation_date + timedelta(days=14),
            next_fertile_window_start_date=next_ovulation_date - timedelta(days=5),
            next_fertile_window_end_date=next_ovulation_date + timedelta(days=1)
        )
    
    async def get_cycle_prediction_as_text(self, db: AsyncSession, user_id: int) -> str:
        """Get cycle prediction as formatted text"""
        try:
            prediction = await self.predict_next_cycle(db, user_id)
            
            return (
                f"The user's next period is predicted to start on {prediction.next_period_start_date.strftime('%B %d, %Y')} "
                f"and end on {prediction.next_period_end_date.strftime('%B %d, %Y')}. "
                f"Their most fertile window is predicted to be between "
                f"{prediction.next_fertile_window_start_date.strftime('%B %d, %Y')} and "
                f"{prediction.next_fertile_window_end_date.strftime('%B %d, %Y')}."
            )
        except Exception:
            return "The user has not provided enough information to generate a cycle prediction. Please ask them to complete their menstrual cycle setup."
    
    async def find_profile_by_user_id(self, db: AsyncSession, user_id: int) -> Profile:
        """Find profile by user ID"""
        result = await db.execute(select(Profile).where(Profile.user_id == user_id))
        profile = result.scalar_one_or_none()
        
        if not profile:
            raise ValueError(f"Profile not found for user ID: {user_id}")
        
        return profile
    
    async def update_user_language(self, db: AsyncSession, user_id: int, language_code: str) -> Profile:
        """Update user language preference"""
        profile = await self.find_profile_by_user_id(db, user_id)
        profile.language_code = language_code
        await db.commit()
        return profile
    
    async def save_profile(self, db: AsyncSession, profile: Profile) -> Profile:
        """Save profile"""
        await db.commit()
        await db.refresh(profile)
        return profile
    
    async def get_partner_data(self, db: AsyncSession, user_id: int) -> PartnerDataDto:
        """Get partner data for the logged-in partner"""
        partner_result = await db.execute(select(Profile).where(Profile.user_id == user_id))
        partner_profile = partner_result.scalar_one_or_none()
        
        if not partner_profile or partner_profile.user_type != UserType.PARTNER:
            raise ValueError(f"No partner profile found for user ID: {user_id}")
        
        referral_code = partner_profile.referred_code
        if not referral_code:
            raise ValueError("Partner profile does not have a referral code.")
        
        # Find the user profile with this referral code
        user_result = await db.execute(
            select(Profile).where(Profile.referral_code == referral_code)
        )
        user_profile = user_result.scalar_one_or_none()
        
        if not user_profile:
            raise ValueError(f"No user profile found for referral code: {referral_code}")
        
        # Get cycle prediction for the linked user
        cycle_prediction = await self.predict_next_cycle(db, user_profile.user_id)
        
        return PartnerDataDto(
            name=user_profile.name,
            cycle_prediction=cycle_prediction
        )
    
    async def process_mcq_risk_assessment(self, db: AsyncSession, user_id: int, answers: Dict[str, str]) -> str:
        """Process MCQ risk assessment and return risk level"""
        profile = await self.find_profile_by_user_id(db, user_id)
        
        score = self._calculate_risk_score_from_mcq(answers)
        
        if score >= 10:
            risk_level = "High Risk"
        elif score >= 5:
            risk_level = "Moderate Risk"
        else:
            risk_level = "Low Risk"
        
        profile.risk_assessment_mcq_data = answers
        profile.breast_cancer_risk_level = risk_level
        
        await db.commit()
        return risk_level
    
    def _calculate_risk_score_from_mcq(self, answers: Dict[str, str]) -> int:
        """Calculate risk score from MCQ answers (same logic as Java)"""
        score = 0
        
        # Menstrual & Reproductive History
        if answers.get("menstruation_start_age") == "MENSTRUATION_START_LT_12":
            score += 1
        if answers.get("menopause_status") == "MENOPAUSE_YES_GT_55":
            score += 2
        if answers.get("pregnancy_history") == "PREGNANCY_NO":
            score += 1  # Nulliparity
        if answers.get("breastfeeding_history") == "BREASTFED_NO":
            score += 1
        if answers.get("breastfeeding_history") == "BREASTFED_NA":
            score += 1  # Same as no
        if answers.get("breastfeeding_history") == "BREASTFED_YES_GT_6MO":
            score -= 1  # Protective factor
        
        # Lifestyle & Hormonal Factors
        if answers.get("alcohol_use") == "ALCOHOL_WEEKLY":
            score += 1
        if answers.get("alcohol_use") == "ALCOHOL_DAILY":
            score += 2
        if answers.get("smoking_status") == "SMOKING_OCCASIONALLY":
            score += 1
        if answers.get("smoking_status") == "SMOKING_REGULARLY":
            score += 2
        if answers.get("hrt_use") == "HRT_YES_LT_5Y":
            score += 1
        if answers.get("hrt_use") == "HRT_YES_GT_5Y":
            score += 2
        if answers.get("oral_contraceptives_use") == "OC_YES_LT_5Y":
            score += 1
        if answers.get("oral_contraceptives_use") == "OC_YES_GT_5Y":
            score += 2
        
        # High-impact factors
        if answers.get("family_history") == "YES_FIRST_DEGREE":
            score += 5
        if answers.get("personal_history_biopsy") == "YES_ATYPICAL_HYPERPLASIA":
            score += 4
        if answers.get("age_group") == "AGE_50_PLUS":
            score += 4
        
        return max(0, score)  # Ensure score doesn't go below 0 