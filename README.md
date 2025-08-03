# She&Soul FastAPI Backend

**Cursor Auto Mode Active — Running on OpenAI GPT-4**

A modern FastAPI backend for the She&Soul application, migrated from Java Spring Boot to Python FastAPI.

## 🚀 Features

- **FastAPI Framework**: Modern, fast web framework for building APIs
- **Async Database**: PostgreSQL with SQLAlchemy async ORM
- **JWT Authentication**: Secure token-based authentication
- **CORS Support**: Cross-origin resource sharing enabled
- **Input Validation**: Pydantic models for request/response validation
- **Auto-generated Docs**: Interactive API documentation at `/docs`
- **Docker Support**: Containerized deployment
- **Health Checks**: Built-in health monitoring
- **Logging**: Structured logging with Loguru

## 📋 Prerequisites

- Python 3.11+
- PostgreSQL 15+
- Docker & Docker Compose (for containerized deployment)

## 🛠️ Installation

### Local Development

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd backend
   ```

2. **Create virtual environment**
   ```bash
   python -m venv venv
   source venv/bin/activate  # On Windows: venv\Scripts\activate
   ```

3. **Install dependencies**
   ```bash
   pip install -r requirements.txt
   ```

4. **Set up environment variables**
   ```bash
   cp .env.example .env
   # Edit .env with your configuration
   ```

5. **Run the application**
   ```bash
   uvicorn main:app --reload --host 0.0.0.0 --port 8000
   ```

### Docker Deployment

1. **Build and run with Docker Compose**
   ```bash
   docker-compose up --build
   ```

2. **Or build and run individually**
   ```bash
   docker build -t shesoul-backend .
   docker run -p 8000:8000 shesoul-backend
   ```

## 🗄️ Database Setup

The application uses PostgreSQL with async SQLAlchemy. Database tables are automatically created on startup.

### Database Configuration

Update the `DATABASE_URL` in your `.env` file:

```env
DATABASE_URL=postgresql://username:password@host:port/database
```

## 🔐 Authentication

The API uses JWT (JSON Web Tokens) for authentication.

### Getting a Token

```bash
curl -X POST "http://localhost:8000/api/authenticate" \
     -H "Content-Type: application/json" \
     -d '{"email": "user@example.com", "password": "password"}'
```

### Using the Token

```bash
curl -X GET "http://localhost:8000/api/protected" \
     -H "Authorization: Bearer <your-jwt-token>"
```

## 📚 API Documentation

Once the application is running, you can access:

- **Interactive API Docs**: http://localhost:8000/docs
- **ReDoc Documentation**: http://localhost:8000/redoc
- **Health Check**: http://localhost:8000/health

## 🔌 API Endpoints

### Authentication
- `POST /api/authenticate` - Login and get JWT token
- `POST /api/signup` - Register new user
- `POST /api/login` - Login user
- `POST /api/verify-email` - Verify email with OTP
- `POST /api/resend-otp` - Resend OTP

### User Management
- `POST /api/profile` - Setup/update user profile
- `PUT /api/services` - Update user service preferences
- `PUT /api/menstrual-data` - Update menstrual tracking data
- `PUT /api/language` - Set user language preference

### Health & Tracking
- `GET /api/partner` - Get partner data
- `GET /api/next-period` - Get next period prediction
- `POST /api/breast-health` - Log breast cancer self-exam
- `POST /api/mcq-assessment` - Submit MCQ risk assessment
- `POST /api/menstrual-assistant` - Get AI assistant response

## 🧪 Testing

Run tests with pytest:

```bash
pytest tests/
```

## 📦 Project Structure

```
backend/
├── api/
│   ├── routes/          # API route handlers
│   └── schemas/         # Pydantic models
├── core/
│   ├── config.py        # Application configuration
│   ├── database.py      # Database setup
│   └── security.py      # Authentication & security
├── db/
│   └── models/          # SQLAlchemy models
├── services/            # Business logic services
├── tests/               # Test files
├── main.py              # FastAPI application entry point
├── requirements.txt     # Python dependencies
├── Dockerfile           # Docker configuration
├── docker-compose.yml   # Docker Compose setup
└── README.md           # This file
```

## 🔧 Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DEBUG` | Enable debug mode | `false` |
| `DATABASE_URL` | PostgreSQL connection string | - |
| `SECRET_KEY` | JWT secret key | - |
| `ACCESS_TOKEN_EXPIRE_MINUTES` | JWT token expiry | `30` |
| `ALLOWED_ORIGINS` | CORS allowed origins | `["*"]` |
| `SMTP_HOST` | SMTP server host | - |
| `SMTP_PORT` | SMTP server port | `587` |
| `GEMINI_API_KEY` | Gemini AI API key | - |

## 🚀 Deployment

### Render Deployment (Recommended)

This repository is pre-configured for easy deployment to Render. See [RENDER_DEPLOYMENT.md](./RENDER_DEPLOYMENT.md) for detailed instructions.

**Quick Deploy:**
1. Fork/clone this repository
2. Create new Web Service on [Render](https://render.com)
3. Connect repository and set environment variables
4. Deploy! 🚀

The application includes:
- ✅ `Procfile` - Production server configuration
- ✅ `requirements.txt` - Python dependencies  
- ✅ `runtime.txt` - Python version specification
- ✅ Gunicorn + Uvicorn for production performance

### Production Deployment

1. **Set production environment variables**
2. **Use production database**
3. **Enable HTTPS**
4. **Set up reverse proxy (nginx)**
5. **Configure monitoring and logging**

### Docker Production

```bash
docker-compose -f docker-compose.prod.yml up -d
```

## 🔍 Monitoring

- **Health Check**: `GET /health`
- **Application Logs**: Check container logs
- **Database Monitoring**: PostgreSQL metrics

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Contact the development team

---

**Migration Notes**: This FastAPI backend was migrated from a Java Spring Boot application, maintaining the same API structure and functionality while leveraging modern Python web development practices. 