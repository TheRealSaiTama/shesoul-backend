package com.example.shesoul.features.auth.presentation

import com.google.gson.annotations.SerializedName

data class ProfileRequest(
    @SerializedName("userId") val userId: Long,
    @SerializedName("name") val name: String,
    @SerializedName("nickname") val nickname: String?,
    @SerializedName("userType") val userType: UserType,
    @SerializedName("preferredLanguage") val preferredLanguage: String,
    @SerializedName("age") val age: Int?,
    @SerializedName("weight") val weight: Double?,
    @SerializedName("height") val height: Double?,
    @SerializedName("preferredServiceType") val preferredServiceType: UserServiceType,
    @SerializedName("referredByCode") val referredByCode: String?
)

enum class UserType {
    USER,
    PARTNER
}

enum class UserServiceType {
    SELF_USE,
    PARTNER_USE
}