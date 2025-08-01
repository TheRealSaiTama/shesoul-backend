package com.example.shesoul.data.remote

import com.example.shesoul.features.auth.presentation.SignUpRequest
import com.example.shesoul.features.auth.presentation.LoginRequest
import com.example.shesoul.features.auth.presentation.LoginResponse
import com.example.shesoul.features.auth.presentation.ProfileRequest
import com.example.shesoul.features.auth.presentation.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class ResendOtpRequest(val email: String)
data class ResendOtpResponse(val message: String)

data class VerifyEmailRequest(val email: String, val otp: String)
data class VerifyEmailResponse(val message: String, val token: String? = null)

interface ApiService {
    @POST("api/signup")
    suspend fun signup(@Body request: SignUpRequest): Response<SignUpResponse>
    
    @POST("api/resend-otp")
    suspend fun sendOtp(@Body request: ResendOtpRequest): Response<ResendOtpResponse>
    
    @POST("api/verify-email")
    suspend fun verifyOtp(@Body request: VerifyEmailRequest): Response<VerifyEmailResponse>

    @POST("api/profile")
    suspend fun createProfile(@Body profileRequest: ProfileRequest): Response<Unit>

    @POST("api/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}