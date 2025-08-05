package com.example.shesoul.features.auth.presentation

import com.google.gson.annotations.SerializedName

/**
 * Aligns Android JSON keys with Java backend ProfileRequest schema:
 * - user_type (required)
 * - name (required, non-empty)
 * - nickname -> nickname
 * - age, height, weight -> optional
 * - preferred_service_type -> optional; server expects MENSTRUAL | BREAST_CANCER | MENTAL_HEALTH
 * - referred_by_code -> optional
 *
 * userId is omitted; server infers user_id from Bearer token.
 * language_code is not part of the active schema, so we do not send preferredLanguage.
 */
data class ProfileRequest(
    @SerializedName("name") val name: String,
    @SerializedName("nickname") val nickname: String?,
    @SerializedName("user_type") val userType: UserType,
    @SerializedName("age") val age: Int?,
    @SerializedName("weight") val weight: Double?,
    @SerializedName("height") val height: Double?,
    // Important: set nullable to avoid enum mismatch until user picks a service
    @SerializedName("preferred_service_type") val preferredServiceType: ServiceType? = null,
    @SerializedName("referred_by_code") val referredByCode: String? = null
)

enum class UserType {
    USER,
    PARTNER
}

/**
 * Server's expected service types per Java backend UserServiceType.java:
 * MENSTRUAL, BREAST_CANCER, MENTAL_HEALTH
 */
enum class ServiceType {
    MENSTRUAL,
    BREAST_CANCER,
    MENTAL_HEALTH
}