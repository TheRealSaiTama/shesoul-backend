from sqlalchemy import create_engine, Column, Integer, String, Date, Boolean, ForeignKey
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker, relationship
import os

Base = declarative_base()

class User(Base):
    __tablename__ = 'users'
    id = Column(Integer, primary_key=True)
    email = Column(String, unique=True, nullable=False)
    password = Column(String, nullable=False)
    profile = relationship("Profile", uselist=False, back_populates="user")

class Profile(Base):
    __tablename__ = 'profiles'
    id = Column(Integer, primary_key=True)
    name = Column(String)
    age = Column(Integer)
    language_code = Column(String)
    preferred_service_type = Column(String)
    user_id = Column(Integer, ForeignKey('users.id'))
    user = relationship("User", back_populates="profile")

class Article(Base):
    __tablename__ = 'articles'
    id = Column(Integer, primary_key=True)
    title = Column(String)
    content = Column(String)

class ChatHistory(Base):
    __tablename__ = 'chat_history'
    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey('users.id'))
    message = Column(String)
class PcosAssessment(Base):
    __tablename__ = 'pcos_assessments'
    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey('users.id'))
    irregular_periods = Column(Boolean)
    acne = Column(Boolean)
    hair_loss = Column(Boolean)
    weight_gain = Column(Boolean)
    dark_patches = Column(Boolean)
    risk_level = Column(String)

class BreastCancerExamLog(Base):
    __tablename__ = 'breast_cancer_exam_logs'
    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey('users.id'))
    symptoms = Column(String) # Storing as a JSON string

class MenstrualCycle(Base):
    __tablename__ = 'menstrual_cycles'
    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey('users.id'))
    last_period_date = Column(Date)
    cycle_length = Column(Integer)
    response = Column(String)

# Temporarily disabled for testing without database
# DATABASE_URL = os.environ.get("DATABASE_URL")
# engine = create_engine(DATABASE_URL)
# SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
# Base.metadata.create_all(bind=engine)

# Mock SessionLocal for testing
class MockSession:
    def query(self, model):
        return MockQuery()
    
    def add(self, obj):
        pass
    
    def commit(self):
        pass
    
    def refresh(self, obj):
        pass
    
    def close(self):
        pass

class MockQuery:
    def filter(self, condition):
        return self
    
    def first(self):
        return None

SessionLocal = MockSession