package com.example.shesoul.features.auth.presentation

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("message") 
    val message: String?,
    @SerializedName("userId")  // Changed from "user_id"
    val userId: Long?,
    @SerializedName("jwt") 
    val jwt: String?
)