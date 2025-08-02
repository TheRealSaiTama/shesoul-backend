"""
Email service for She&Soul FastAPI application
"""

import asyncio
from typing import Optional
import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from core.config import settings

class EmailService:
    """Service class for email functionality"""
    
    def __init__(self):
        self.smtp_host = settings.SMTP_HOST
        self.smtp_port = settings.SMTP_PORT
        self.smtp_username = settings.SMTP_USERNAME
        self.smtp_password = settings.SMTP_PASSWORD
        self.smtp_tls = settings.SMTP_TLS
        self.smtp_auth = settings.SMTP_AUTH
    
    def send_email(self, to_email: str, subject: str, body: str, html_body: Optional[str] = None) -> bool:
        """Send an email"""
        try:
            message = MIMEMultipart("alternative")
            message["From"] = self.smtp_username
            message["To"] = to_email
            message["Subject"] = subject
            
            # Add text and HTML parts
            text_part = MIMEText(body, "plain")
            message.attach(text_part)
            
            if html_body:
                html_part = MIMEText(html_body, "html")
                message.attach(html_part)
            
            # Send email
            with smtplib.SMTP(self.smtp_host, self.smtp_port) as server:
                if self.smtp_tls:
                    server.starttls()
                if self.smtp_auth:
                    server.login(self.smtp_username, self.smtp_password)
                server.send_message(message)
            
            return True
            
        except Exception as e:
            print(f"Failed to send email: {str(e)}")
            return False
    
    def send_otp_email(self, to_email: str, otp_code: str) -> bool:
        """Send OTP verification email"""
        subject = "She&Soul - Email Verification OTP"
        body = f"""
        Hello!
        
        Your email verification OTP is: {otp_code}
        
        This OTP will expire in 10 minutes.
        
        If you didn't request this, please ignore this email.
        
        Best regards,
        She&Soul Team
        """
        
        html_body = f"""
        <html>
        <body>
            <h2>She&Soul - Email Verification</h2>
            <p>Hello!</p>
            <p>Your email verification OTP is: <strong>{otp_code}</strong></p>
            <p>This OTP will expire in 10 minutes.</p>
            <p>If you didn't request this, please ignore this email.</p>
            <br>
            <p>Best regards,<br>She&Soul Team</p>
        </body>
        </html>
        """
        
        return self.send_email(to_email, subject, body, html_body)
    
    def send_welcome_email(self, to_email: str, user_name: str) -> bool:
        """Send welcome email to new users"""
        subject = "Welcome to She&Soul!"
        body = f"""
        Hello {user_name}!
        
        Welcome to She&Soul! We're excited to have you on board.
        
        Your account has been successfully created and verified.
        
        Best regards,
        She&Soul Team
        """
        
        html_body = f"""
        <html>
        <body>
            <h2>Welcome to She&Soul!</h2>
            <p>Hello {user_name}!</p>
            <p>Welcome to She&Soul! We're excited to have you on board.</p>
            <p>Your account has been successfully created and verified.</p>
            <br>
            <p>Best regards,<br>She&Soul Team</p>
        </body>
        </html>
        """
        
        return self.send_email(to_email, subject, body, html_body)