import smtplib
import os
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from typing import Optional

class EmailService:
    def __init__(self):
        self.smtp_host = os.getenv("SMTP_HOST", "smtp.zoho.in")
        self.smtp_port = int(os.getenv("SMTP_PORT", "587"))
        self.smtp_username = os.getenv("SMTP_USERNAME", "support@sheandsoul.co.in")
        self.smtp_password = os.getenv("SMTP_PASSWORD", "43Qk2k8dPmeP")
        
    def send_otp_email(self, to_email: str, otp: str) -> bool:
        """
        Send OTP email to the specified email address
        """
        try:
            # Create message
            msg = MIMEMultipart('alternative')
            msg['Subject'] = "Your Email Verification Code"
            msg['From'] = self.smtp_username
            msg['To'] = to_email
            
            # HTML content
            html_content = f"""
            <html>
            <body>
                <h3>Hello,</h3>
                <p>Thank you for signing up. Please use the following One-Time Password (OTP) to verify your email address:</p>
                <h2>{otp}</h2>
                <p>This OTP is valid for 10 minutes. If you did not request this, please ignore this email.</p>
                <br>
                <p>Best regards,<br>She&Soul Team</p>
            </body>
            </html>
            """
            
            # Attach HTML content
            html_part = MIMEText(html_content, 'html')
            msg.attach(html_part)
            
            # Send email
            with smtplib.SMTP(self.smtp_host, self.smtp_port) as server:
                server.starttls()
                server.login(self.smtp_username, self.smtp_password)
                server.send_message(msg)
                
            return True
            
        except Exception as e:
            print(f"Failed to send OTP email: {str(e)}")
            return False 