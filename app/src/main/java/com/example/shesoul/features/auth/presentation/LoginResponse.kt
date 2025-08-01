package com.example.shesoul.features.auth.presentation

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("jwt") val jwt: String,
    @SerializedName("message") val message: String,
    @SerializedName("userId") val userId: Long,
    @SerializedName("email") val email: String
)