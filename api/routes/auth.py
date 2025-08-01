"""
Authentication routes for She&Soul FastAPI application
"""

from fastapi import APIRouter, Depends, HTTPException, status
from fastapi.security import HTTPBearer
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import select

from core.database import get_db
from core.security import verify_password, create_access_token, get_current_user
from db.models.user import User
from api.schemas.auth import LoginRequest, AuthResponse

router = APIRouter()

@router.post("/authenticate", response_model=AuthResponse)
async def authenticate(
    login_request: LoginRequest,
    db: AsyncSession = Depends(get_db)
):
    """
    Authenticate user and return JWT token
    """
    try:
        # Find user by email
        result = await db.execute(select(User).where(User.email == login_request.email))
        user = result.scalar_one_or_none()
        
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