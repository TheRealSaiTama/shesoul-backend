package com.example.shesoul.features.auth.presentation
import com.example.shesoul.ui.components.HorizontalWaveButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import android.widget.Toast
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shesoul.R
import com.example.shesoul.ui.components.HorizontalWaveButton
import com.example.shesoul.ui.theme.PoppinsFamily
import com.example.shesoul.ui.theme.PlayfairDisplayFamily
import com.example.shesoul.ui.theme.SheSoulTheme

@Composable
fun LoginScreen(
    onNavigateToSignUp: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    
    val context = LocalContext.current
    val loginResult by authViewModel.loginResult.observeAsState()

    LaunchedEffect(loginResult) {
        loginResult?.let { result ->
            isLoading = false
            result.fold(
                onSuccess = { loginResponse ->
                    Toast.makeText(context, loginResponse.message, Toast.LENGTH_SHORT).show()
                    onLoginSuccess()
                },
                onFailure = { error ->
                    val errorMessage = when {
                        error.message?.contains("Incorrect username or password") == true -> 
                            "Invalid email or password. Please check your credentials."
                        error.message?.contains("User not found") == true -> 
                            "User not found. Please check your email or sign up."
                        else -> "Login failed: ${error.message}"
                    }
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    fun validateInputs(): Boolean {
        var isValid = true
        
        if (email.isEmpty()) {
            emailError = "Email is required"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Please enter a valid email address"
            isValid = false
        } else {
            emailError = null
        }
        
        if (password.isEmpty()) {
            passwordError = "Password is required"
            isValid = false
        } else if (password.length < 8) {
            passwordError = "Password must be at least 8 characters"
            isValid = false
        } else {
            passwordError = null
        }
        
        return isValid
    }

    fun handleLogin() {
        if (validateInputs()) {
            isLoading = true
            val loginRequest = LoginRequest(email, password)
            authViewModel.login(loginRequest)
        }
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

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Login",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = PoppinsFamily,
                    color = Color.Black
                )

                Text(
                    text = "Welcome back to app",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = PoppinsFamily,
                    color = SubtitleColor,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { 
                    email = it
                    emailError = null
                },
                label = { Text("Email Address", color = Color(0xFF5A5A5A)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                textStyle = TextStyle(fontFamily = PoppinsFamily, color = Color.Black),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (emailError != null) Color.Red else FieldBorderColor,
                    unfocusedBorderColor = if (emailError != null) Color.Red else FieldBorderColor
                ),
                isError = emailError != null
            )
            
            if (emailError != null) {
                Text(
                    text = emailError!!,
                    color = Color.Red,
                    fontSize = 12.sp,
                    fontFamily = PoppinsFamily,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { 
                    password = it
                    passwordError = null
                },
                label = { Text("Password", color = Color(0xFF5A5A5A)) },
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
                    focusedBorderColor = if (passwordError != null) Color.Red else FieldBorderColor,
                    unfocusedBorderColor = if (passwordError != null) Color.Red else FieldBorderColor
                ),
                isError = passwordError != null
            )
            
            if (passwordError != null) {
                Text(
                    text = passwordError!!,
                    color = Color.Red,
                    fontSize = 12.sp,
                    fontFamily = PoppinsFamily,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }

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

            HorizontalWaveButton(
                onClick = { handleLogin() },
                text = if (isLoading) "Logging in..." else "Login",
                startColor = Color(0xFFBBBDFF),
                endColor = Color(0xFF9092FF),
                modifier = Modifier.fillMaxWidth().height(44.dp),
                cornerRadius = 10.dp,
                useVerticalGradient = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Or Sign Up With",
                fontSize = 14.sp,
                fontFamily = PoppinsFamily,
                color = SubtitleColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google_icon),
                    contentDescription = "Sign Up with Google",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { /* TODO: Handle Google Sign Up */ }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            val annotatedString = buildAnnotatedString {
                append("Not Registered Yet ? ")
                pushStringAnnotation(tag = "SIGNUP", annotation = "signup")
                withStyle(style = SpanStyle(color = Color(0xFF6769F7), fontWeight = FontWeight.Medium)) {
                    append("Sign Up")
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
                    annotatedString.getStringAnnotations(tag = "SIGNUP", start = offset, end = offset)
                        .firstOrNull()?.let { onNavigateToSignUp() }
                },
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    SheSoulTheme {
        LoginScreen(
            onNavigateToSignUp = { },
            onLoginSuccess = { }
        )
    }
}