"""
Referral Code Service for She&Soul FastAPI application
Based on Java implementation
"""

import hashlib
import string
import secrets

class ReferralCodeService:
    """Service for generating referral codes"""
    
    def __init__(self):
        self.alphabet = string.ascii_uppercase + string.digits
    
    def generate_code(self, profile_id: int) -> str:
        """
        Generate a referral code based on profile ID
        Similar to Java implementation logic
        """
        # Create a base string from profile ID
        base_string = f"SHESOUL{profile_id}"
        
        # Create hash
        hash_object = hashlib.md5(base_string.encode())
        hash_hex = hash_object.hexdigest()
        
        # Convert hash to referral code
        code = ""
        for i in range(0, min(8, len(hash_hex)), 2):
            # Take pairs of hex digits and convert to alphabet index
            hex_pair = hash_hex[i:i+2]
            index = int(hex_pair, 16) % len(self.alphabet)
            code += self.alphabet[index]
        
        # Ensure minimum length and add randomness if needed
        while len(code) < 6:
            code += secrets.choice(self.alphabet)
        
        return code[:8]  # Max 8 characters
    
    def generate_random_code(self, length: int = 8) -> str:
        """Generate a completely random referral code"""
        return ''.join(secrets.choice(self.alphabet) for _ in range(length))
