package com.example.shesoul.features.auth.presentation

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("message") val message: String? = null,
    @SerializedName("user_id") val userId: Long? = null,
    @SerializedName("jwt") val jwt: String? = null
)
