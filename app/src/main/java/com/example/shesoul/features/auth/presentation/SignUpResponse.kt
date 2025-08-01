package com.example.shesoul.features.auth.presentation

data class SignUpResponse(
    val message: String,
    val userId: Long,
    val jwt: String
)
