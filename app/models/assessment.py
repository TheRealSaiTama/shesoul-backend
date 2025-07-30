from pydantic import BaseModel
from typing import List, Dict

class PCOSAssesmentRequest(BaseModel):
    irregular_periods: bool
    acne: bool
    hair_loss: bool
    weight_gain: bool
    dark_patches: bool

class PCOSAssesmentResponse(BaseModel):
    risk_level: str

class BreastCancerExamRequest(BaseModel):
    symptoms: Dict[str, str]

class McqAssessmentRequest(BaseModel):
    answers: Dict[str, str]

class McqAssessmentResponse(BaseModel):
    risk_level: str