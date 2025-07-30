from pydantic import BaseModel
from datetime import date

class MenstrualTrackingDto(BaseModel):
    last_period_date: date
    cycle_length: int

class CyclePredictionDto(BaseModel):
    next_period_date: date
    fertile_window_start: date
    fertile_window_end: date
