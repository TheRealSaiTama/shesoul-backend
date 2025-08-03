package com.example.shesoul.features.auth.presentation

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("jwt")
    val accessToken: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("userId")
    val userId: Long?
)