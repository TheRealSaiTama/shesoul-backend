import google.generativeai as genai
import os

class GeminiService:
    def __init__(self):
        genai.configure(api_key=os.environ["    GEMINI_API_KEY"])
        self.model = genai.GenerativeModel('gemini-pro')

    def get_gemini_response(self, user_message: str) -> str:
        response = self.model.generate_content(user_message)
        return response.text
