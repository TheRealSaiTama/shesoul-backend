"""
Configuration settings for She&Soul FastAPI application
"""

import os
from typing import List
from pydantic import BaseModel, Field
from pydantic_settings import BaseSettings

class Settings(BaseSettings):
    """Application settings"""
    
    # Application
    APP_NAME: str = "She&Soul API"
    APP_VERSION: str = "1.0.0"
    DEBUG: bool = Field(default=False, env="DEBUG")
    
    # Database
    DATABASE_URL: str = Field(
        default="postgresql://postgres.bwksvtzocrpugdpyztbg:One2%23fourfive@aws-0-ap-south-1.pooler.supabase.com:6543/postgres",
        env="DATABASE_URL"
    )
    
    # Security
    SECRET_KEY: str = Field(
        default="ThisIsMySuperSecretKeyForSheAndSoulApplication",
        env="SECRET_KEY"
    )
    ALGORITHM: str = "HS256"
    ACCESS_TOKEN_EXPIRE_MINUTES: int = 30
    
    # CORS
    ALLOWED_ORIGINS: List[str] = ["*"]
    
    # Email
    SMTP_HOST: str = Field(default="smtp.zoho.in", env="SMTP_HOST")
    SMTP_PORT: int = Field(default=587, env="SMTP_PORT")
    SMTP_USERNAME: str = Field(default="support@sheandsoul.co.in", env="SMTP_USERNAME")
    SMTP_PASSWORD: str = Field(default="43Qk2k8dPmeP", env="SMTP_PASSWORD")
    SMTP_TLS: bool = True
    SMTP_AUTH: bool = True
    
    # Gemini AI
    GEMINI_API_KEY: str = Field(
        default="AIzaSyALByhJy-brEmLuWbOFj9TIC-2UrZY9vBY",
        env="GEMINI_API_KEY"
    )
    
    # Rate Limiting
    RATE_LIMIT_PER_MINUTE: int = 60
    
    # Logging
    LOG_LEVEL: str = "INFO"
    
    model_config = {
        "env_file": ".env",
        "case_sensitive": True
    }

# Create settings instance
settings = Settings() 