#!/usr/bin/env python3
"""
Simple test script to verify authentication endpoints
"""

import asyncio
import httpx
import json

# Test configuration
BASE_URL = "https://shesoul-backend.onrender.com"
# BASE_URL = "http://localhost:8000"  # For local testing

async def test_health():
    """Test health endpoint"""
    async with httpx.AsyncClient() as client:
        response = await client.get(f"{BASE_URL}/health")
        print(f"Health check: {response.status_code}")
        if response.status_code == 200:
            print(f"Response: {response.json()}")
        return response.status_code == 200

async def test_signup():
    """Test user signup"""
    async with httpx.AsyncClient() as client:
        signup_data = {
            "email": "test@example.com",
            "password": "testpassword123"
        }
        
        response = await client.post(
            f"{BASE_URL}/api/signup",
            json=signup_data,
            headers={"Content-Type": "application/json"}
        )
        
        print(f"Signup: {response.status_code}")
        if response.status_code == 200:
            print(f"Response: {response.json()}")
        else:
            print(f"Error: {response.text}")
        
        return response.status_code == 200

async def test_login():
    """Test user login"""
    async with httpx.AsyncClient() as client:
        login_data = {
            "email": "test@example.com",
            "password": "testpassword123"
        }
        
        response = await client.post(
            f"{BASE_URL}/api/login",
            json=login_data,
            headers={"Content-Type": "application/json"}
        )
        
        print(f"Login: {response.status_code}")
        if response.status_code == 200:
            print(f"Response: {response.json()}")
            return response.json().get("jwt")
        else:
            print(f"Error: {response.text}")
            return None

async def test_protected_route(token):
    """Test protected route with JWT token"""
    if not token:
        print("No token available for protected route test")
        return False
    
    async with httpx.AsyncClient() as client:
        headers = {
            "Authorization": f"Bearer {token}",
            "Content-Type": "application/json"
        }
        
        response = await client.get(f"{BASE_URL}/protected", headers=headers)
        
        print(f"Protected route: {response.status_code}")
        if response.status_code == 200:
            print(f"Response: {response.json()}")
        else:
            print(f"Error: {response.text}")
        
        return response.status_code == 200

async def main():
    """Run all tests"""
    print("Testing She&Soul API endpoints...")
    print("=" * 50)
    
    # Test health endpoint
    print("\n1. Testing health endpoint...")
    health_ok = await test_health()
    
    # Test signup
    print("\n2. Testing signup...")
    signup_ok = await test_signup()
    
    # Test login
    print("\n3. Testing login...")
    token = await test_login()
    login_ok = token is not None
    
    # Test protected route
    print("\n4. Testing protected route...")
    protected_ok = await test_protected_route(token)
    
    # Summary
    print("\n" + "=" * 50)
    print("Test Summary:")
    print(f"Health check: {'✅ PASS' if health_ok else '❌ FAIL'}")
    print(f"Signup: {'✅ PASS' if signup_ok else '❌ FAIL'}")
    print(f"Login: {'✅ PASS' if login_ok else '❌ FAIL'}")
    print(f"Protected route: {'✅ PASS' if protected_ok else '❌ FAIL'}")
    
    if all([health_ok, signup_ok, login_ok, protected_ok]):
        print("\n🎉 All tests passed! The authentication system is working correctly.")
    else:
        print("\n⚠️  Some tests failed. Check the logs above for details.")

if __name__ == "__main__":
    asyncio.run(main())