from fastapi import APIRouter, Depends, HTTPException
from app.models.cycle import MenstrualTrackingDto, CyclePredictionDto
from app.database import get_db
from app.dependencies import get_current_user
from datetime import date, timedelta

router = APIRouter()

@router.put("/menstrual-data")
async def update_menstrual_data(menstrual_data: MenstrualTrackingDto, user: dict = Depends(get_current_user)):
    try:
        # We'll store this data in a new 'menstrual_data' table
        menstrual_data_dict = menstrual_data.dict()
        menstrual_data_dict["user_id"] = user.id
        supabase.table("menstrual_data").upsert(menstrual_data_dict).execute()
        return {"message": "Menstrual data updated successfully!"}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@router.get("/next-period", response_model=CyclePredictionDto)
async def get_next_period(user: dict = Depends(get_current_user)):
    try:
        # Retrieve the user's latest menstrual data
        data = supabase.table("menstrual_data").select("*").eq("user_id", user.id).order("last_period_date", desc=True).limit(1).execute()
        if not data.data:
            raise HTTPException(status_code=404, detail="No menstrual data found for this user.")

        latest_data = data.data[0]
        last_period_date = date.fromisoformat(latest_data["last_period_date"])
        cycle_length = latest_data["cycle_length"]

        # Simple prediction logic (can be improved)
        next_period_date = last_period_date + timedelta(days=cycle_length)
        fertile_window_start = next_period_date - timedelta(days=19)
        fertile_window_end = next_period_date - timedelta(days=11)

        return CyclePredictionDto(
            next_period_date=next_period_date,
            fertile_window_start=fertile_window_start,
            fertile_window_end=fertile_window_end,
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
