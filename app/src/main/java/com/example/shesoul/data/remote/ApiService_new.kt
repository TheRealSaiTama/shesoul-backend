package com.example.shesoul.data.remote

import com.example.shesoul.features.auth.presentation.SignUpRequest
import com.example.shesoul.features.auth.presentation.LoginRequest
import com.example.shesoul.features.auth.presentation.LoginResponse
import com.example.shesoul.features.auth.presentation.ProfileRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

data class ResendOtpRequest(val email: String)
data class ResendOtpResponse(val message: String)

data class VerifyEmailRequest(val email: String, val otp: String)
data class VerifyEmailResponse(val message: String, val token: String? = null)

// Profile basic update payload
data class ProfilePatchRequest(
    val age: Int? = null,
    val height: Double? = null,
    val weight: Double? = null,
    val name: String? = null,
    val nick_name: String? = null
)

// Response data classes for new endpoints
data class ApiResponse(val message: String, val data: Any? = null)
data class AuthResponse(val jwt: String)

interface ApiService {
    @POST("api/signup")
    suspend fun signup(@Body request: SignUpRequest): Response<LoginResponse>
    
    @POST("api/resend-otp")
    suspend fun sendOtp(@Body request: ResendOtpRequest): Response<ResendOtpResponse>
    
    @POST("api/verify-email")
    suspend fun verifyOtp(@Body request: VerifyEmailRequest): Response<VerifyEmailResponse>

    @POST("api/profile")
    suspend fun createProfile(@Body profileRequest: ProfileRequest): Response<ApiResponse>

    // Update user services
    @PUT("api/services")
    suspend fun updateUserServices(@Body services: ProfilePatchRequest): Response<ApiResponse>

    // Update menstrual data
    @PUT("api/menstrual-data")
    suspend fun updateMenstrualData(@Body data: ProfilePatchRequest): Response<ApiResponse>

    // Update language
    @PUT("api/language")
    suspend fun updateLanguage(@Body languageData: Map<String, String>): Response<ApiResponse>

    @POST("api/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    // Authenticate endpoint for JWT token (separate from login)
    @POST("api/authenticate")
    suspend fun authenticate(@Body loginRequest: LoginRequest): Response<AuthResponse>

    // Breast health logging
    @POST("api/breast-health")
    suspend fun logBreastHealth(@Body symptoms: Map<String, String>): Response<ApiResponse>

    // MCQ Assessment
    @POST("api/mcq-assessment")
    suspend fun submitMcqAssessment(@Body answers: Map<String, String>): Response<ApiResponse>

    // Menstrual assistant
    @POST("api/menstrual-assistant")
    suspend fun getMenstrualAssistant(@Body request: Map<String, String>): Response<ApiResponse>

    // Get partner data
    @GET("api/partner")
    suspend fun getPartnerData(): Response<ApiResponse>

    // Get next period prediction
    @GET("api/next-period")
    suspend fun getNextPeriod(): Response<ApiResponse>
}
