from pydantic import BaseModel
from typing import List, Optional

class ProfileRequest(BaseModel):
    name: str
    age: int
    language_code: str
    preferred_service_type: str

class ProfileResponse(BaseModel):
    id: int
    name: str
    age: int
    language_code: str
    preferred_service_type: str

class ProfileServiceDto(BaseModel):
    preferred_service_type: str
