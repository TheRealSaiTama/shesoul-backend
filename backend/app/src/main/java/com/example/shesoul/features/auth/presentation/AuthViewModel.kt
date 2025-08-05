package com.example.shesoul.features.auth.presentation

import android.content.Intent
import androidx.lifecycle.*
import com.example.shesoul.features.auth.presentation.SignUpRequest
import com.example.shesoul.features.auth.presentation.SignUpResponse
import com.example.shesoul.data.remote.RetrofitInstance
import com.example.shesoul.data.remote.ResendOtpRequest
import com.example.shesoul.data.remote.VerifyEmailRequest
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val _signupResponse = MutableLiveData<Result<SignUpResponse>>()
    val signupResponse: LiveData<Result<SignUpResponse>> = _signupResponse

    private val _googleSignInResult = MutableLiveData<GoogleSignInResult>()
    val googleSignInResult: LiveData<GoogleSignInResult> = _googleSignInResult

    private val _otpSendResult = MutableLiveData<Result<String>>()
    val otpSendResult: LiveData<Result<String>> = _otpSendResult

    private val _otpVerificationResult = MutableLiveData<Result<String>>()
    val otpVerificationResult: LiveData<Result<String>> = _otpVerificationResult

    private val _profileCreationResult = MutableLiveData<Result<String>>()
    val profileCreationResult: LiveData<Result<String>> = _profileCreationResult

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    private var authToken: String? = null
    private var currentUser: LoginResponse? = null

    // Temporary storage for profile data
    private var tempName: String? = null
    private var tempNickname: String? = null
    private var tempUserType: UserType? = null
    private var tempAge: Int? = null
    private var tempWeight: Double? = null
    private var tempHeight: Double? = null

    fun getAuthToken(): String? = authToken

    fun getCurrentUser(): LoginResponse? = currentUser

    fun setUserName(name: String) {
        tempName = name
    }

    fun setUserNickname(nickname: String?) {
        tempNickname = nickname
    }

    fun setUserAge(age: Int?) {
        tempAge = age
    }

    fun setUserWeight(weight: Double?) {
        tempWeight = weight
    }

    fun setUserHeight(height: Double?) {
        tempHeight = height
    }

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.login(loginRequest)
                if (response.isSuccessful) {
                    val loginResponse = response.body()!!
                    authToken = loginResponse.jwt
                    currentUser = loginResponse
                    RetrofitInstance.setAuthToken(loginResponse.jwt)
                    _isLoggedIn.value = true
                    _loginResult.value = Result.success(loginResponse)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Login failed"
                    _loginResult.value = Result.failure(Exception(errorBody))
                }
            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
            }
        }
    }

    fun logout() {
        authToken = null
        currentUser = null
        RetrofitInstance.setAuthToken(null)
        _isLoggedIn.value = false
    }

    fun createProfile(userType: UserType) {
        tempUserType = userType
        viewModelScope.launch {
            try {
                val profileRequest = ProfileRequest(
                    userId = currentUser?.userId ?: 1L,
                    name = tempName ?: "",
                    nickname = tempNickname,
                    userType = tempUserType ?: UserType.USER,
                    preferredLanguage = "en",
                    age = tempAge,
                    weight = tempWeight,
                    height = tempHeight,
                    preferredServiceType = if (tempUserType == UserType.PARTNER) UserServiceType.PARTNER_USE else UserServiceType.SELF_USE,
                    referredByCode = null
                )
                val response = RetrofitInstance.api.createProfile(profileRequest)
                if (response.isSuccessful) {
                    _profileCreationResult.value = Result.success("Profile created successfully")
                } else {
                    _profileCreationResult.value = Result.failure(Exception("Profile creation failed: ${response.errorBody()?.string()}"))
                }
            } catch (e: Exception) {
                _profileCreationResult.value = Result.failure(e)
            }
        }
    }

    fun testBackendConnection() {
        viewModelScope.launch {
            try {
                println("Testing backend connection...")
                val testRequest = SignUpRequest("test@example.com", "testpassword123")
                val response = RetrofitInstance.api.signup(testRequest)
                println("Test response code: ${response.code()}")
                println("Test response body: ${response.body()}")
                println("Test error body: ${response.errorBody()?.string()}")
            } catch (e: Exception) {
                println("Test connection failed: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun signup(request: SignUpRequest) {
        viewModelScope.launch {
            try {
                println("Attempting signup for email: ${request.email}")
                val response = RetrofitInstance.api.signup(request)
                println("Signup response code: ${response.code()}")
                println("Signup response body: ${response.body()}")
                println("Signup error body: ${response.errorBody()?.string()}")
                
                if (response.isSuccessful) {
                    _signupResponse.value = Result.success(response.body()!!)
                    sendOtpToBackend(request.email)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Signup failed with code: ${response.code()}"
                    println("Signup failed: $errorBody")
                    _signupResponse.value = Result.failure(Exception(errorBody))
                }
            } catch (e: Exception) {
                println("Signup exception: ${e.message}")
                e.printStackTrace()
                _signupResponse.value = Result.failure(e)
            }
        }
    }
    
    fun handleGoogleSignInResult(data: Intent?, googleSignInManager: GoogleSignInManager) {
        viewModelScope.launch {
            val result = googleSignInManager.handleSignInResult(data)
            _googleSignInResult.value = result
        }
    }
    
    fun sendOtpToBackend(email: String) {
        viewModelScope.launch {
            try {
                println("Sending OTP to email: $email")
                val request = ResendOtpRequest(email)
                val response = RetrofitInstance.api.sendOtp(request)
                if (response.isSuccessful) {
                    println("OTP sent successfully to $email")
                    _otpSendResult.value = Result.success("OTP sent to $email")
                } else {
                    println("Failed to send OTP: ${response.errorBody()?.string()}")
                    _otpSendResult.value = Result.failure(Exception("Failed to send OTP: ${response.errorBody()?.string()}"))
                }
            } catch (e: Exception) {
                println("Exception while sending OTP: ${e.message}")
                _otpSendResult.value = Result.failure(e)
            }
        }
    }
    
    fun verifyOtp(email: String, otp: String) {
        viewModelScope.launch {
            try {
                println("Verifying OTP: $otp for email: $email")
                val request = VerifyEmailRequest(email, otp)
                val response = RetrofitInstance.api.verifyOtp(request)
                if (response.isSuccessful) {
                    println("OTP verification successful")
                    _otpVerificationResult.value = Result.success("OTP verified successfully")
                } else {
                    println("OTP verification failed: ${response.errorBody()?.string()}")
                    _otpVerificationResult.value = Result.failure(Exception("Invalid OTP. Please try again."))
                }
            } catch (e: Exception) {
                println("Exception while verifying OTP: ${e.message}")
                _otpVerificationResult.value = Result.failure(e)
            }
        }
    }
}
