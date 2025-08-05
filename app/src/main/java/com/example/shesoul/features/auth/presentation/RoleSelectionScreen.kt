package com.example.shesoul.features.auth.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
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
import com.example.shesoul.ui.theme.PlayfairDisplayFamily
import com.example.shesoul.ui.theme.SheSoulTheme

@Composable
fun RoleSelectionScreen(
    onContinue: (UserType) -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    var selectedRole by remember { mutableStateOf(UserType.USER) } // User selected by default
    val context = LocalContext.current
    val profileCreationResult by authViewModel.profileCreationResult.observeAsState()

    LaunchedEffect(profileCreationResult) {
        profileCreationResult?.let { result ->
            result.fold(
                onSuccess = {
                    Toast.makeText(context, "Profile created successfully!", Toast.LENGTH_SHORT).show()
                    onContinue(selectedRole)
                },
                onFailure = { error ->
                    // If backend says user already has a profile, treat as success and continue onboarding
                    val msg = error.message?.lowercase().orEmpty()
                    if (msg.contains("already has a profile") || msg.contains("profile already exists")) {
                        Toast.makeText(context, "Profile already exists. Continuing…", Toast.LENGTH_SHORT).show()
                        onContinue(selectedRole)
                    } else {
                        Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_LONG).show()
                    }
                }
            )
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Scrollable content area
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(56.dp))

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

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Choose your role",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = Color(0xFF2D2D2D),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(), // Use flexible width
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    RoleCard(
                        role = UserType.USER,
                        label = "User",
                        imageRes = R.drawable.women,
                        isSelected = selectedRole == UserType.USER,
                        onClick = { selectedRole = UserType.USER },
                        isTopCard = true
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.1f),
                                        Color.Black.copy(alpha = 0.2f),
                                        Color.Black.copy(alpha = 0.1f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    RoleCard(
                        role = UserType.PARTNER,
                        label = "Partner",
                        imageRes = R.drawable.male,
                        isSelected = selectedRole == UserType.PARTNER,
                        onClick = { selectedRole = UserType.PARTNER },
                        isTopCard = false
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Anchored button at the bottom
            // Ensure consistent height and shape across devices using our shared HorizontalWaveButton defaults
            HorizontalWaveButton(
                onClick = {
                    authViewModel.createProfile(selectedRole)
                },
                text = "Continue",
                startColor = Color(0xFFE0BBFF),
                endColor = Color(0xFF9092FF),
                // Use a consistent min height and shape: 44.dp and 10.dp, matching other screens
                // Avoid wrapping the button in a parent .height that can distort its intrinsic height
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                height = 44.dp,
                cornerRadius = 10.dp,
                useVerticalGradient = true
            )
        }
    }
}

@Composable
fun RoleCard(
    role: UserType,
    label: String,
    imageRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    isTopCard: Boolean
) {
    val topColor = Color.White
    val bottomColor by animateColorAsState(
        targetValue = when (role) {
            UserType.USER -> if (isSelected) Color(0xFFFFC6FB) else Color.Black
            UserType.PARTNER -> if (isSelected) Color(0xFF80DDFF) else Color.Black
        },
        animationSpec = tween(durationMillis = 300),
        label = "gradient_animation"
    )

    val elevation by animateFloatAsState(
        targetValue = if (isSelected) 8.dp.value else 2.dp.value,
        animationSpec = tween(durationMillis = 200),
        label = "elevation_animation"
    )

    val cornerShape = if (isTopCard) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 0.dp, bottomEnd = 0.dp)
    } else {
        RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(288.dp)
            .shadow(elevation.dp, cornerShape)
            .clickable { onClick() },
        shape = cornerShape,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(topColor, bottomColor)
                    )
                )
        ) {
            if (role == UserType.USER) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = "$label character",
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(0.7f),
                        contentScale = ContentScale.Crop
                    )
                    
                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = label,
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp,
                        color = if (isSelected) Color(0xFF9C0000) else Color(0xFFFFFFFF),
                        modifier = Modifier
                            .width(71.dp)
                            .height(44.dp)
                            .padding(end = 8.dp)
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = label,
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp,
                        color = if (isSelected) Color(0xFF0A587C) else Color(0xFFFFFFFF),
                        modifier = Modifier
                            .width(116.dp)
                            .height(44.dp)
                            .padding(start = 8.dp)
                    )
                    
                    Spacer(modifier = Modifier.weight(1f))

                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = "$label character",
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(0.7f),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@Composable
private fun animateFloatAsState(
    targetValue: Float,
    animationSpec: androidx.compose.animation.core.AnimationSpec<Float>,
    label: String
): State<Float> {
    return androidx.compose.animation.core.animateFloatAsState(
        targetValue = targetValue,
        animationSpec = animationSpec,
        label = label
    )
}

@Preview(showBackground = true)
@Composable
fun RoleSelectionScreenPreview() {
    SheSoulTheme {
        RoleSelectionScreen(
            onContinue = { role ->
                // Preview callback
            }
        )
    }
}