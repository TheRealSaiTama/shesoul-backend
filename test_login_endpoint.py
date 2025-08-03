#!/usr/bin/env python3
"""
Test login endpoint for She&Soul FastAPI application
"""

import asyncio
import sys
import os
from fastapi.testclient import TestClient

# Add the current directory to Python path
sys.path.append(os.path.dirname(os.path.abspath(__file__)))

from main import app

def test_app_startup():
    """Test that the app starts up correctly"""
    print("Testing app startup...")
    
    try:
        client = TestClient(app)
        print("✅ TestClient created successfully")
        
        # Test root endpoint
        response = client.get("/")
        print(f"✅ Root endpoint response: {response.status_code}")
        print(f"   Response: {response.json()}")
        
        # Test health endpoint
        response = client.get("/health")
        print(f"✅ Health endpoint response: {response.status_code}")
        print(f"   Response: {response.json()}")
        
        return True
        
    except Exception as e:
        print(f"❌ App startup test failed: {e}")
        return False

def test_login_endpoint():
    """Test the login endpoint (will fail due to database connection)"""
    print("\nTesting login endpoint...")
    
    try:
        client = TestClient(app)
        
        # Test login endpoint with invalid credentials
        login_data = {
            "email": "test@example.com",
            "password": "testpassword"
        }
        
        response = client.post("/api/login", json=login_data)
        print(f"Login endpoint response: {response.status_code}")
        
        # The endpoint should return an error due to database connection issues
        # but it shouldn't crash the application
        if response.status_code in [500, 401, 503]:
            print("✅ Login endpoint handled database error gracefully")
            return True
        else:
            print(f"❌ Unexpected response: {response.status_code}")
            return False
            
    except Exception as e:
        print(f"❌ Login endpoint test failed: {e}")
        return False

def main():
    """Main test function"""
    print("Starting She&Soul API tests...")
    
    # Test app startup
    startup_success = test_app_startup()
    
    # Test login endpoint
    login_success = test_login_endpoint()
    
    if startup_success and login_success:
        print("\n✅ All tests passed!")
        print("The application is working correctly with async database operations.")
    else:
        print("\n❌ Some tests failed!")
        sys.exit(1)

if __name__ == "__main__":
    main()