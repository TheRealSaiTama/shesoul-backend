package com.example.shesoul.features.auth.presentation

import com.google.gson.annotations.SerializedName

/**
 * Aligns Android JSON keys with FastAPI schema at api/schemas/profile.py:
 * - user_type (required)
 * - name (required, non-empty)
 * - nickname -> nickname
 * - age, height, weight -> optional
 * - preferred_service_type -> optional; server expects MENSTRUATION | BREAST_HEALTH | MENTAL_HEALTH | PCOS
 * - referred_by_code -> optional
 *
 * userId is omitted; server infers user_id from Bearer token.
 * language_code is not part of the active api/schemas/profile.py, so we do not send preferredLanguage.
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
 * Server's expected service types per api/schemas/profile.py:
 * MENSTRUATION, BREAST_HEALTH, MENTAL_HEALTH, PCOS
 */
enum class ServiceType {
    MENSTRUATION,
    BREAST_HEALTH,
    MENTAL_HEALTH,
    PCOS
}