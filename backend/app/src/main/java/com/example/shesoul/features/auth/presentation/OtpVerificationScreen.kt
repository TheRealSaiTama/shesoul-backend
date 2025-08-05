package com.example.shesoul.features.auth.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shesoul.ui.components.HorizontalWaveButton
import com.example.shesoul.ui.theme.PlayfairDisplayFamily
import com.example.shesoul.ui.theme.PoppinsFamily
import com.example.shesoul.ui.theme.SheSoulTheme
import com.example.shesoul.features.auth.presentation.AuthViewModel

@Composable
fun OtpVerificationScreen(
    email: String,
    onOtpVerified: () -> Unit = {},
    viewModel: AuthViewModel = viewModel()
) {
    var otpValues by remember { mutableStateOf(List(6) { "" }) }
    val focusRequesters = remember { List(6) { FocusRequester() } }
    val keyboardController = LocalSoftwareKeyboardController.current
    
    val otpVerificationResult by viewModel.otpVerificationResult.observeAsState()
    val otpSendResult by viewModel.otpSendResult.observeAsState()
    
    // Handle OTP verification result
    LaunchedEffect(otpVerificationResult) {
        otpVerificationResult?.let { result ->
            result.fold(
                onSuccess = { message ->
                    Log.d("OTP", "OTP verification successful: $message")
                    onOtpVerified()
                },
                onFailure = { error ->
                    Log.e("OTP", "OTP verification failed: ${error.message}")
                    // Reset OTP fields on error
                    otpValues = List(6) { "" }
                    focusRequesters[0].requestFocus()
                    // TODO: Show error message to user
                }
            )
        }
    }
    
    // Handle resend OTP result
    LaunchedEffect(otpSendResult) {
        otpSendResult?.let { result ->
            result.fold(
                onSuccess = { message ->
                    Log.d("OTP", "OTP resent successfully: $message")
                    // TODO: Show success message to user
                },
                onFailure = { error ->
                    Log.e("OTP", "Failed to resend OTP: ${error.message}")
                    // TODO: Show error message to user
                }
            )
        }
    }
    
    // Auto focus first field when screen loads
    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 56.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Top Branding with custom colors
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

            Spacer(modifier = Modifier.height(72.dp))

            // Title and Description
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Verify Your Email",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = PoppinsFamily,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Enter the 6-digit code sent to",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = PoppinsFamily,
                    color = Color(0xFF808080),
                    textAlign = TextAlign.Center
                )
                
                Text(
                    text = email,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = PoppinsFamily,
                    color = Color(0xFF9092FF),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // OTP Input Fields
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                otpValues.forEachIndexed { index, value ->
                    OtpDigitField(
                        value = value,
                        onValueChange = { newValue ->
                            if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                                val newOtpValues = otpValues.toMutableList()
                                newOtpValues[index] = newValue
                                otpValues = newOtpValues
                                
                                // Auto move to next field
                                if (newValue.isNotEmpty() && index < 5) {
                                    focusRequesters[index + 1].requestFocus()
                                }
                            }
                        },
                        onBackspace = {
                            if (otpValues[index].isEmpty() && index > 0) {
                                focusRequesters[index - 1].requestFocus()
                            } else {
                                val newOtpValues = otpValues.toMutableList()
                                newOtpValues[index] = ""
                                otpValues = newOtpValues
                            }
                        },
                        focusRequester = focusRequesters[index],
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Resend Code
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Didn't receive the code? ",
                    fontSize = 14.sp,
                    fontFamily = PoppinsFamily,
                    color = Color(0xFF808080)
                )
                TextButton(
                    onClick = { 
                        // Call resend OTP from ViewModel
                        viewModel.sendOtpToBackend(email)
                    },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Resend",
                        fontSize = 14.sp,
                        fontFamily = PoppinsFamily,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF9092FF)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Continue Button
            val isOtpComplete = otpValues.all { it.isNotEmpty() }
            HorizontalWaveButton(
                onClick = {
                    if (isOtpComplete) {
                        keyboardController?.hide()
                        val otpString = otpValues.joinToString("")
                        viewModel.verifyOtp(email, otpString)
                    }
                },
                text = "Continue",
                startColor = if (isOtpComplete) Color(0xFFE0BBFF) else Color(0xFFE0E0E0),
                endColor = if (isOtpComplete) Color(0xFF9092FF) else Color(0xFFBDBDBD),
                modifier = Modifier.fillMaxWidth().height(44.dp),
                cornerRadius = 10.dp
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun OtpDigitField(
    value: String,
    onValueChange: (String) -> Unit,
    onBackspace: () -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    
    BasicTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.isEmpty()) {
                onBackspace()
            } else {
                onValueChange(newValue)
            }
        },
        modifier = modifier
            .height(56.dp)
            .focusRequester(focusRequester)
            .onFocusChanged { isFocused = it.isFocused }
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 2.dp,
                color = if (isFocused) Color(0xFF9092FF) else if (value.isNotEmpty()) Color(0xFFD0BFFF) else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(12.dp)
            ),
        textStyle = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = PoppinsFamily,
            color = Color.Black,
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        decorationBox = { innerTextField ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                innerTextField()
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun OtpVerificationScreenPreview() {
    SheSoulTheme {
        OtpVerificationScreen(
            email = "user@example.com",
            onOtpVerified = {}
        )
    }
}
