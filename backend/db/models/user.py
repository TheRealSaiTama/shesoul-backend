"""
User model for She&Soul FastAPI application
"""

from sqlalchemy import Column, Integer, String, Boolean
from sqlalchemy.orm import relationship
from core.database import Base

class User(Base):
    """User model"""
    __tablename__ = "users"
    
    id = Column(Integer, primary_key=True, index=True)
    email = Column(String, unique=True, nullable=False, index=True)
    password = Column(String, nullable=False)
    is_email_verified = Column(Boolean, default=False)
    
    # Relationship
    profile = relationship("Profile", back_populates="user", uselist=False, cascade="all, delete-orphan")
    
    def __repr__(self):
        return f"<User(id={self.id}, email='{self.email}')>" 