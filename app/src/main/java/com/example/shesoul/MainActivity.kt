package com.example.shesoul

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shesoul.features.auth.presentation.AgeSelectionScreen
import com.example.shesoul.features.auth.presentation.AuthViewModel
import com.example.shesoul.features.auth.presentation.AssessmentCompletionScreen
import com.example.shesoul.features.auth.presentation.BrandingScreen
import com.example.shesoul.features.auth.presentation.BreastCancerAwarenessJourneyScreen
import com.example.shesoul.features.auth.presentation.BreastCancerHomeScreen
import com.example.shesoul.features.auth.presentation.ExperienceSelectionScreen
import com.example.shesoul.features.auth.presentation.ExperienceType
import com.example.shesoul.features.auth.presentation.HeightSelectionScreen
import com.example.shesoul.features.auth.presentation.HomeScreen
import com.example.shesoul.features.auth.presentation.LoginScreen
import com.example.shesoul.features.auth.presentation.NickNameScreen
import com.example.shesoul.features.auth.presentation.PartnerWelcomeScreen
import com.example.shesoul.features.auth.presentation.PrivacyScreen
import com.example.shesoul.features.auth.presentation.ProcessingScreen
import com.example.shesoul.features.auth.presentation.UserType
import com.example.shesoul.features.auth.presentation.RoleSelectionScreen
import com.example.shesoul.features.auth.presentation.SignUpScreen
import com.example.shesoul.features.auth.presentation.UserInfoScreen
import com.example.shesoul.features.auth.presentation.WeightSelectionScreen
import com.example.shesoul.features.auth.presentation.OtpVerificationScreen
import com.example.shesoul.ui.theme.SheSoulTheme

sealed class Screen {
    object Privacy : Screen()
    object Branding : Screen()
    object SignUp : Screen()
    object Login : Screen()
    object OtpVerification : Screen()
    object UserInfo : Screen()
    object NickName : Screen()
    object RoleSelection : Screen()
    object PartnerWelcome : Screen()
    object AgeSelection : Screen()
    object HeightSelection : Screen()
    object WeightSelection : Screen()
    object ExperienceSelection : Screen()
    object BreastCancerAwarenessJourney : Screen()
    object AssessmentCompletion : Screen()
    object BreastCancerHome : Screen()
    object Processing : Screen()
    object Home : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SheSoulTheme {
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Privacy) }
                var otpEmail by remember { mutableStateOf("") }
                val authViewModel: AuthViewModel = viewModel()

                when (currentScreen) {
                    is Screen.Privacy -> PrivacyScreen(onAgree = { currentScreen = Screen.Branding })
                    is Screen.Branding -> BrandingScreen(onVideoEnd = { currentScreen = Screen.SignUp })
                    is Screen.SignUp -> SignUpScreen(
                        onNavigateToLogin = { currentScreen = Screen.Login },
                        onNavigateToOtpVerification = { email ->
                            otpEmail = email
                            currentScreen = Screen.OtpVerification
                        },
                        viewModel = authViewModel
                    )
                    is Screen.OtpVerification -> OtpVerificationScreen(
                        email = otpEmail,
                        onOtpVerified = { currentScreen = Screen.UserInfo },
                        viewModel = authViewModel
                    )
                    is Screen.Login -> LoginScreen(
                        onNavigateToSignUp = { currentScreen = Screen.SignUp }, 
                        onLoginSuccess = { currentScreen = Screen.UserInfo },
                        authViewModel = authViewModel
                    )
                    is Screen.UserInfo -> UserInfoScreen(
                        onContinue = { currentScreen = Screen.NickName },
                        authViewModel = authViewModel
                    )
                    is Screen.NickName -> NickNameScreen(
                        onContinue = { currentScreen = Screen.RoleSelection }, 
                        onSkip = { currentScreen = Screen.RoleSelection },
                        authViewModel = authViewModel
                    )
                    is Screen.RoleSelection -> {
                        RoleSelectionScreen(
                            onContinue = { role ->
                                when (role) {
                                    UserType.PARTNER -> currentScreen = Screen.PartnerWelcome
                                    UserType.USER -> {
                                        // Continue onboarding flow after RoleSelection for existing users
                                        currentScreen = Screen.AgeSelection
                                    }
                                }
                            },
                            authViewModel = authViewModel
                        )
                    }
                    is Screen.PartnerWelcome -> PartnerWelcomeScreen(onAnimationComplete = { currentScreen = Screen.AgeSelection })
                    is Screen.AgeSelection -> {
                        AgeSelectionScreen(
                            onContinue = { currentScreen = Screen.HeightSelection },
                            authViewModel = authViewModel
                        )
                    }
                    is Screen.HeightSelection -> {
                        HeightSelectionScreen(
                            onContinue = { currentScreen = Screen.WeightSelection },
                            authViewModel = authViewModel
                        )
                    }
                    is Screen.WeightSelection -> {
                        WeightSelectionScreen(
                            onContinue = { currentScreen = Screen.ExperienceSelection },
                            authViewModel = authViewModel
                        )
                    }
                    is Screen.ExperienceSelection -> {
                        ExperienceSelectionScreen(
                            onExperienceSelected = { experienceType ->
                                when (experienceType) {
                                    ExperienceType.GENERAL -> currentScreen = Screen.Processing
                                    ExperienceType.BREAST_CANCER_SUPPORT -> currentScreen = Screen.BreastCancerAwarenessJourney
                                }
                            }
                        )
                    }
                    is Screen.BreastCancerAwarenessJourney -> {
                        BreastCancerAwarenessJourneyScreen(
                            onJourneyComplete = { currentScreen = Screen.AssessmentCompletion }
                        )
                    }
                    is Screen.AssessmentCompletion -> {
                        AssessmentCompletionScreen(
                            onAssessmentStart = { currentScreen = Screen.Processing },
                            onNavigateToBreastHome = { currentScreen = Screen.BreastCancerHome }
                        )
                    }
                    is Screen.BreastCancerHome -> {
                        BreastCancerHomeScreen()
                    }
                    is Screen.Processing -> {
                        ProcessingScreen(
                            onProcessingComplete = {
                                println("Onboarding complete! Navigating to main app...")
                                currentScreen = Screen.Home
                            }
                        )
                    }
                    is Screen.Home -> {
                        HomeScreen()
                    }
                }
            }
        }
    }
}