from sqlalchemy.orm import Session
from app.models.user import User, Base as UserBase
from app.db.models import engine
from typing import Optional

# Create user table
UserBase.metadata.create_all(bind=engine)

class UserService:
    def __init__(self):
        pass
    
    def create_user(self, db: Session, email: str, password: str) -> User:
        """Create a new user"""
        # Check if user already exists
        existing_user = self.get_user_by_email(db, email)
        if existing_user:
            raise ValueError("User with this email already exists")
        
        # Create new user
        user = User(email=email, password=password)
        db.add(user)
        db.commit()
        db.refresh(user)
        return user
    
    def get_user_by_email(self, db: Session, email: str) -> Optional[User]:
        """Get user by email"""
        return db.query(User).filter(User.email == email).first()
    
    def get_user_by_id(self, db: Session, user_id: int) -> Optional[User]:
        """Get user by ID"""
        return db.query(User).filter(User.id == user_id).first()
    
    def verify_user_credentials(self, db: Session, email: str, password: str) -> Optional[User]:
        """Verify user credentials"""
        user = self.get_user_by_email(db, email)
        if user and user.verify_password(password):
            return user
        return None
    
    def mark_email_verified(self, db: Session, email: str) -> bool:
        """Mark user email as verified"""
        user = self.get_user_by_email(db, email)
        if user:
            user.is_email_verified = True
            db.commit()
            return True
        return False 