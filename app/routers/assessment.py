from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from app.models.assessment import PCOSAssesmentRequest, PCOSAssesmentResponse, BreastCancerExamRequest, McqAssessmentRequest, McqAssessmentResponse
from app.db.models import PcosAssessment, BreastCancerExamLog
from app.dependencies import get_current_user
from app.db.models import SessionLocal

router = APIRouter()

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@router.post("/pcos/assess", response_model=PCOSAssesmentResponse)
async def assess_pcos(request: PCOSAssesmentRequest, user: dict = Depends(get_current_user), db: Session = Depends(get_db)):
    risk_score = sum(request.dict().values())
    risk_level = "Low"
    if risk_score >= 4:
        risk_level = "High"
    elif risk_score >= 2:
        risk_level = "Medium"

    assessment_data = request.dict()
    assessment_data["user_id"] = user['id']
    assessment_data["risk_level"] = risk_level
    
    db_assessment = PcosAssessment(**assessment_data)
    db.add(db_assessment)
    db.commit()

    return PCOSAssesmentResponse(risk_level=risk_level)

@router.post("/breast-health")
async def log_breast_cancer_self_exam(request: BreastCancerExamRequest, user: dict = Depends(get_current_user), db: Session = Depends(get_db)):
    exam_data = request.dict()
    exam_data["user_id"] = user['id']
    
    db_exam_log = BreastCancerExamLog(user_id=user['id'], symptoms=str(request.symptoms))
    db.add(db_exam_log)
    db.commit()
    
    return {"message": "Detailed self-exam log saved successfully."}

@router.post("/mcq-assessment", response_model=McqAssessmentResponse)
async def submit_mcq_risk_assessment(request: McqAssessmentRequest, user: dict = Depends(get_current_user), db: Session = Depends(get_db)):
    # This is a placeholder for a more complex risk assessment logic
    risk_level = "Low"
    if len(request.answers) > 5: # Example logic
        risk_level = "High"

    return McqAssessmentResponse(risk_level=risk_level)