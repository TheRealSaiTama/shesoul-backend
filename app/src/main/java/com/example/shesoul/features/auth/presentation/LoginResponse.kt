package com.example.shesoul.features.auth.presentation

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("access_token") val accessToken: String? = null,
    @SerializedName("token_type") val tokenType: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("user_id") val userId: Long? = null
)