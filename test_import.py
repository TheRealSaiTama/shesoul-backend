#!/usr/bin/env python3
"""
Quick test to verify our imports work
"""

try:
    from api.routes.auth import router
    print("✅ auth.py imports successfully")
except Exception as e:
    print(f"❌ auth.py import error: {e}")

try:
    from api.schemas.auth import SignUpRequest, LoginRequest
    print("✅ auth schemas import successfully")
except Exception as e:
    print(f"❌ auth schemas import error: {e}")

try:
    from core.database import get_db
    print("✅ database imports successfully")
except Exception as e:
    print(f"❌ database import error: {e}")

try:
    from core.security import get_password_hash, verify_password
    print("✅ security imports successfully")
except Exception as e:
    print(f"❌ security import error: {e}")

print("🧪 Import test completed")
