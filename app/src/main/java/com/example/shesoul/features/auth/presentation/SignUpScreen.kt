package com.example.shesoul.features.auth.presentation

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

import com.example.shesoul.R
import com.example.shesoul.features.auth.presentation.AuthViewModel
import com.example.shesoul.ui.components.HorizontalWaveButton
import com.example.shesoul.ui.theme.SheSoulTheme
import com.example.shesoul.ui.theme.PoppinsFamily
import com.example.shesoul.ui.theme.PlayfairDisplayFamily
import com.example.shesoul.features.auth.presentation.GoogleSignInManager
import com.example.shesoul.features.auth.presentation.SignUpRequest
import com.example.shesoul.features.auth.presentation.GoogleSignInResult
import android.widget.Toast

// Define colors based on Figma design
val AuthButtonGradientStart = Color(0xFFE0BBFF) // Updated to match Figma gradient start
val AuthButtonGradientEnd = Color(0xFF9092FF) // Updated to match Figma gradient end
val AuthLinkColor = Color(0xFF9092FF)
val FieldBorderColor = Color(0xFFD0BFFF) // Light purple border
val SubtitleColor = Color(0xFF808080) // Gray for "Welcome" and others

@Composable
fun SignUpScreen(
    onNavigateToLogin: () -> Unit = {},
    onNavigateToNextOnboarding: () -> Unit = {},
    onNavigateToOtpVerification: (String) -> Unit = {},
    viewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val signupResult by viewModel.signupResponse.observeAsState()
    val googleSignInResult by viewModel.googleSignInResult.observeAsState()
    val otpSendResult by viewModel.otpSendResult.observeAsState()
    
    val context = LocalContext.current
    val googleSignInManager = remember { GoogleSignInManager(context) }
    val coroutineScope = rememberCoroutineScope()
    
    // Google Sign-In launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.handleGoogleSignInResult(result.data, googleSignInManager)
        }
    }
    
    // Handle signup result (manual signup)
    LaunchedEffect(signupResult) {
        signupResult?.let { result ->
            result.fold(
                onSuccess = { response ->
                    Log.d("Signup", "Manual signup successful: ${response.message}")
                    Toast.makeText(context, "Signup successful! Please check your email for OTP.", Toast.LENGTH_LONG).show()
                    // For manual signup, navigate to OTP verification with the email
                    onNavigateToOtpVerification(email)
                },
                onFailure = { error ->
                    Log.e("Signup", "Manual signup failed: ${error.message}")
                    val errorMessage = when {
                        error.message?.contains("409") == true -> "Email already exists. Please use a different email or try logging in."
                        error.message?.contains("403") == true -> "Access denied. Please try again later."
                        error.message?.contains("499") == true -> "Request timeout. Please check your internet connection and try again."
                        else -> "Signup failed: ${error.message}"
                    }
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                }
            )
        }
    }
    
    // Handle OTP send result
    LaunchedEffect(otpSendResult) {
        otpSendResult?.let { result ->
            result.fold(
                onSuccess = { message ->
                    Log.d("OTP", "OTP sent successfully: $message")
                },
                onFailure = { error ->
                    Log.e("OTP", "Failed to send OTP: ${error.message}")
                    // TODO: Show error message to user
                }
            )
        }
    }
    
    // Handle Google Sign-In result
    LaunchedEffect(googleSignInResult) {
        googleSignInResult?.let { result ->
            when (result) {
                is GoogleSignInResult.Success -> {
                    // Handle successful Google Sign-In - navigate directly to next onboarding
                    Log.d("GoogleSignIn", "Google Sign-In Success: ${result.email}")
                    Log.d("GoogleSignIn", "Navigating to next onboarding screen...")
                    onNavigateToNextOnboarding()
                }
                is GoogleSignInResult.Error -> {
                    Log.e("GoogleSignIn", "Error: ${result.message}")
                    // TODO: Show error message to user
                }
                GoogleSignInResult.Cancelled -> {
                    Log.d("GoogleSignIn", "Sign-in cancelled")
                }
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 40.dp, bottom = 16.dp), // Increased top padding for consistency
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Top Branding with custom colors - replacing logo
            val brandText = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color(0xFF9092FF))) {
                    append("She")
                }
                withStyle(style = SpanStyle(color = Color.Black)) {
                    append("&")
                }
                withStyle(style = SpanStyle(color = Color(0xFF9092FF))) {
                    append("Soul")
                }
            }
            
            Text(
                text = brandText,
                fontSize = 32.sp,
                fontFamily = PlayfairDisplayFamily,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(72.dp)) // Increased from 48.dp to 72.dp for even more gap

            // Title and Subtitle - Left aligned above email field
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Sign Up",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = PoppinsFamily,
                    color = Color.Black
                )

                Text(
                    text = "Welcome",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = PoppinsFamily,
                    color = SubtitleColor,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address", color = Color(0xFF5A5A5A)) }, // Darker color instead of SubtitleColor
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                textStyle = TextStyle(fontFamily = PoppinsFamily, color = Color.Black),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FieldBorderColor,
                    unfocusedBorderColor = FieldBorderColor,
                    focusedLabelColor = FieldBorderColor,
                    cursorColor = FieldBorderColor,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = Color(0xFF5A5A5A)) }, // Darker color instead of SubtitleColor
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                textStyle = TextStyle(fontFamily = PoppinsFamily, color = Color.Black),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = FieldBorderColor
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FieldBorderColor,
                    unfocusedBorderColor = FieldBorderColor,
                    focusedLabelColor = FieldBorderColor,
                    cursorColor = FieldBorderColor,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Text(
                text = "Forgot Password ?",
                color = AuthLinkColor,
                fontFamily = PoppinsFamily,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.End)
                    .clickable { /* TODO: Handle Forgot Password */ }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Sign Up Button - Updated height, shape, and gradient to match Figma
            HorizontalWaveButton(
                onClick = {
                    val request = SignUpRequest(
                        email = email,
                        password = password
                    )
                    viewModel.signup(request)
                },
                text = "Sign Up",
                startColor = AuthButtonGradientStart,
                endColor = AuthButtonGradientEnd,
                modifier = Modifier.fillMaxWidth().height(44.dp),
                cornerRadius = 10.dp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Or Sign Up With
            Text(
                text = "Or Sign Up With",
                fontSize = 14.sp,
                fontFamily = PoppinsFamily,
                color = SubtitleColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Social Icons
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google_icon),
                    contentDescription = "Sign Up with Google",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { 
                            // Handle Google Sign Up
                            Log.d("GoogleSignIn", "Google icon clicked!")
                            try {
                                val signInIntent = googleSignInManager.getSignInIntent()
                                Log.d("GoogleSignIn", "Got sign-in intent, launching...")
                                launcher.launch(signInIntent)
                            } catch (e: Exception) {
                                Log.e("GoogleSignIn", "Error getting sign-in intent: ${e.message}", e)
                            }
                        }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Sign In Link
            val annotatedString = buildAnnotatedString {
                append("Already Have An Account ? ")
                pushStringAnnotation(tag = "SIGNIN", annotation = "signin")
                withStyle(style = SpanStyle(color = Color(0xFF6769F7), fontWeight = FontWeight.Medium)) {
                    append("Sign In")
                }
                pop()
            }

            ClickableText(
                text = annotatedString,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = PoppinsFamily,
                    color = SubtitleColor
                ),
                onClick = { offset ->
                    annotatedString.getStringAnnotations(tag = "SIGNIN", start = offset, end = offset)
                        .firstOrNull()?.let { onNavigateToLogin() }
                },
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SheSoulTheme {
        SignUpScreen(
            onNavigateToLogin = { },
            onNavigateToNextOnboarding = { },
            onNavigateToOtpVerification = { }
        )
    }
}
