"""
Authentication routes for She&Soul FastAPI application
"""

from fastapi import APIRouter, Depends, HTTPException, status
from fastapi.security import HTTPBearer
from sqlalchemy.orm import Session

from core.database import get_sync_db
from core.security import verify_password, create_access_token, get_current_user, get_password_hash
from db.models.user import User
# Assuming AuthResponse is defined in api.schemas.auth
from api.schemas.auth import LoginRequest, AuthResponse, SignUpRequest

router = APIRouter()

@router.post("/signup", response_model=AuthResponse)
def signup(
    signup_request: SignUpRequest,
    db: Session = Depends(get_sync_db)
):
    """
    Register a new user
    """
    try:
        # Check if user already exists
        existing_user = db.query(User).filter(User.email == signup_request.email).first()
        
        if existing_user:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail="Email already registered"
            )
        
        # Create new user
        new_user = User(
            email=signup_request.email,
            password=get_password_hash(signup_request.password),
            is_email_verified=False
        )
        
        db.add(new_user)
        db.commit()
        db.refresh(new_user)
        
        # Create access token
        access_token = create_access_token(data={"sub": new_user.email})
        
        return AuthResponse(
            access_token=access_token,
            token_type="bearer"
        )
        
    except HTTPException:
        raise
    except Exception as e:
        db.rollback()
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Signup failed: {str(e)}"
        )

# FIX: Changed the endpoint path from "/authenticate" to "/login"
@router.post("/login", response_model=AuthResponse)
def login(
    login_request: LoginRequest,
    db: Session = Depends(get_sync_db)
):
    """
    Authenticate user and return JWT token
    """
    try:
        # Find user by email
        user = db.query(User).filter(User.email == login_request.email).first()
        
        if not user:
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="Incorrect email or password"
            )
        
        # Verify password
        if not verify_password(login_request.password, user.password):
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="Incorrect email or password"
            )
        
        # Create access token
        access_token = create_access_token(data={"sub": user.email})
        
        return AuthResponse(
            access_token=access_token,
            token_type="bearer"
        )
        
    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Authentication failed: {str(e)}"
        )
