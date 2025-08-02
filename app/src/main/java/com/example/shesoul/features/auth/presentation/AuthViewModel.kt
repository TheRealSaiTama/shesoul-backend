package com.example.shesoul.features.auth.presentation

import android.app.Application
import android.content.Intent
import androidx.lifecycle.*
import com.example.shesoul.data.auth.TokenManager
import com.example.shesoul.data.remote.RetrofitClient
import com.example.shesoul.data.remote.ResendOtpRequest
import com.example.shesoul.data.remote.VerifyEmailRequest
import com.example.shesoul.data.remote.ApiService
import com.example.shesoul.data.remote.ProfilePatchRequest
import kotlinx.coroutines.launch

sealed class SaveState {
    data object Idle : SaveState()
    data object Loading : SaveState()
    data class Success(val message: String? = null) : SaveState()
    data class Error(val message: String?) : SaveState()
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val tokenManager = TokenManager(getApplication())
    private val apiService = RetrofitClient.create(getApplication())

    private val _signupResponse = MutableLiveData<Result<LoginResponse>>()
    val signupResponse: LiveData<Result<LoginResponse>> = _signupResponse

    // Save state for async profile updates from UI steps (e.g., age, weight, height)
    private val _saveState = MutableLiveData<SaveState>(SaveState.Idle)
    val saveState: LiveData<SaveState> = _saveState

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

    private var currentUser: LoginResponse? = null

    // Temporary storage for profile data
    private var tempName: String? = null
    private var tempNickname: String? = null
    private var tempUserType: UserType? = null
    private var tempAge: Int? = null
    private var tempWeight: Double? = null
    private var tempHeight: Double? = null

    fun getAuthToken(): String? = tokenManager.getToken()

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

    /**
     * Save only age immediately to backend via PUT /api/profile/basic and update local temp state.
     * Emits SaveState for UI to react (Loading -> Success/Error).
     */
    fun saveAge(age: Int) {
        viewModelScope.launch {
            try {
                _saveState.value = SaveState.Loading
                // Update local cache
                setUserAge(age)

                // Compose minimal patch request for basic profile update
                val patch = ProfilePatchRequest(
                    age = age,
                    height = null,
                    weight = null,
                    name = null,
                    nick_name = null
                )

                val response = apiService.patchProfile(patch)
                if (response.isSuccessful) {
                    _saveState.value = SaveState.Success("Age saved")
                } else {
                    val msg = response.errorBody()?.string() ?: "Failed to save age"
                    _saveState.value = SaveState.Error(msg)
                }
            } catch (e: Exception) {
                _saveState.value = SaveState.Error(e.message)
            }
        }
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
                val response = apiService.login(loginRequest)
                if (response.isSuccessful) {
                    val loginResponse = response.body()!!
                    if (loginResponse.accessToken != null) {
                        tokenManager.saveToken(loginResponse.accessToken)
                    } else {
                        throw Exception("Access token is null")
                    }   
                    currentUser = loginResponse
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
        tokenManager.clearToken()
        currentUser = null
        _isLoggedIn.value = false
    }

    fun createProfile(userType: UserType) {
        tempUserType = userType
        viewModelScope.launch {
            try {
                val safeName = (tempName ?: "").ifBlank { "Anonymous" }
                val profileRequest = ProfileRequest(
                    name = safeName,
                    nickname = tempNickname,
                    userType = tempUserType ?: UserType.USER,
                    age = tempAge,
                    weight = tempWeight,
                    height = tempHeight,
                    // Keep null to satisfy server schema unless user explicitly selects a service later
                    preferredServiceType = null,
                    referredByCode = null
                )
                val response = apiService.createProfile(profileRequest)
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
                val response = apiService.signup(testRequest)
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
                val response = apiService.signup(request)
                println("Signup response code: ${response.code()}")
                println("Signup response body: ${response.body()}")
                println("Signup error body: ${response.errorBody()?.string()}")
                
                if (response.isSuccessful) {
                    val signupResponse = response.body()
                    val token = signupResponse?.accessToken
                    if (token != null) {
                        tokenManager.saveToken(token)
                    } else {
                        throw Exception("Signup access token is null")
                    }
                    _signupResponse.value = Result.success(signupResponse!!)
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
                val response = apiService.sendOtp(request)
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
                val response = apiService.verifyOtp(request)
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
