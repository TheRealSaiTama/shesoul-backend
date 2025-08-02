"""
Tests for main application endpoints
"""

import pytest
from fastapi.testclient import TestClient
from main import app

client = TestClient(app)

def test_root_endpoint():
    """Test root endpoint"""
    response = client.get("/")
    assert response.status_code == 200
    assert "message" in response.json()
    assert "She&Soul API" in response.json()["message"]

def test_health_check():
    """Test health check endpoint"""
    response = client.get("/health")
    assert response.status_code == 200
    assert response.json()["status"] == "healthy"
    assert response.json()["service"] == "She&Soul API"

def test_docs_endpoint():
    """Test that docs are accessible"""
    response = client.get("/docs")
    assert response.status_code == 200

def test_redoc_endpoint():
    """Test that redoc is accessible"""
    response = client.get("/redoc")
    assert response.status_code == 200 