# She's Soul FastAPI Backend

This is the FastAPI backend for the She's Soul Android application.

## Features

- User authentication (signup/login)
- Profile management
- Menstrual cycle tracking
- PCOS assessment
- Breast cancer awareness
- Chat functionality
- Article management

## API Endpoints

- `GET /` - Welcome message
- `GET /health` - Health check
- `POST /api/signup` - User registration
- `POST /api/login` - User authentication
- `POST /api/profile` - Create user profile
- `POST /api/resend-otp` - Send OTP
- `POST /api/verify-email` - Verify email with OTP

## Development

```bash
# Install dependencies
pip install -r requirements.txt

# Run locally
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

## Environment Variables

- `SUPABASE_URL` - Supabase project URL
- `SUPABASE_KEY` - Supabase API key
- `SECRET_KEY` - JWT secret key
- `DATABASE_URL` - Database connection string 