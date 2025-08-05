package com.example.shesoul.features.auth.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shesoul.R
import com.example.shesoul.ui.components.HorizontalWaveButton
import com.example.shesoul.ui.theme.PlayfairDisplayFamily
import com.example.shesoul.ui.theme.PoppinsFamily
import com.example.shesoul.ui.theme.SheSoulTheme

enum class ExperienceType {
    GENERAL, BREAST_CANCER_SUPPORT
}

@Composable
fun ExperienceSelectionScreen(
    onExperienceSelected: (ExperienceType) -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    var selectedExperience by remember { mutableStateOf(ExperienceType.GENERAL) }
    val snackbarHostState = remember { SnackbarHostState() }
    var inlineError by remember { mutableStateOf<String?>(null) }
    val saveState = authViewModel.saveState.observeAsState(SaveState.Idle)

    LaunchedEffect(saveState.value) {
        when (val state = saveState.value) {
            is SaveState.Success -> {
                inlineError = null
                onExperienceSelected(selectedExperience)
            }
            is SaveState.Error -> {
                inlineError = state.message
                snackbarHostState.showSnackbar(state.message ?: "Couldn't save experience")
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(snackbarData = data)
            }
        }
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Branding with status bar padding
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.statusBars.asPaddingValues())
                    .padding(top = 24.dp), // Increased from 16dp to 24dp for better spacing
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val brandText = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color(0xFF9092FF))) { append("She") }
                    withStyle(style = SpanStyle(color = Color.Black)) { append("&") }
                    withStyle(style = SpanStyle(color = Color(0xFF9092FF))) { append("Soul") }
                }
                Text(
                    text = brandText,
                    fontSize = 32.sp,
                    fontFamily = PlayfairDisplayFamily,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Header Text
            Text(
                text = "Choose your experience",
                fontFamily = PoppinsFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                color = Color.Black.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "We offer tailored experiences to better serve your needs",
                fontFamily = PoppinsFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color.Black.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Experience Selection Cards
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // General Experience Card
                ExperienceCard(
                    title = "General Experience",
                    description = "Complete wellness tracking with comprehensive health features for your daily well-being",
                    icon = R.drawable.ic_heart, // You'll need to add this icon
                    isSelected = selectedExperience == ExperienceType.GENERAL,
                    onClick = { selectedExperience = ExperienceType.GENERAL }
                )

                // Breast Cancer Support Card
                ExperienceCard(
                    title = "Breast Cancer Support",
                    description = "Specialized features for breast cancer patients and survivors, including treatment tracking and support resources",
                    icon = R.drawable.ic_ribbon, // You'll need to add this icon
                    isSelected = selectedExperience == ExperienceType.BREAST_CANCER_SUPPORT,
                    onClick = { selectedExperience = ExperienceType.BREAST_CANCER_SUPPORT },
                    accentColor = Color(0xFFFF69B4) // Pink ribbon color
                )
            }

            // Bottom continue button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .padding(WindowInsets.navigationBars.asPaddingValues()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalWaveButton(
                    onClick = { /* Animation will start */ },
                    onAnimationComplete = { onExperienceSelected(selectedExperience) },
                    modifier = Modifier.fillMaxWidth(),
                    text = "Continue"
                )
            }
        }
    }
}

@Composable
private fun ExperienceCard(
    title: String,
    description: String,
    icon: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    accentColor: Color = Color(0xFF9092FF)
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) accentColor else Color.Gray.copy(alpha = 0.3f),
        animationSpec = tween(300),
        label = "border_color"
    )
    
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) accentColor.copy(alpha = 0.1f) else Color.White,
        animationSpec = tween(300),
        label = "background_color"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                accentColor.copy(alpha = 0.2f),
                                accentColor.copy(alpha = 0.1f)
                            )
                        ),
                        shape = RoundedCornerShape(28.dp)
                    )
                    .clip(RoundedCornerShape(28.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontFamily = PoppinsFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Color.Black.copy(alpha = 0.9f)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = description,
                    fontFamily = PoppinsFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = Color.Black.copy(alpha = 0.6f),
                    lineHeight = 20.sp
                )
            }

            // Selection Indicator
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(
                            color = accentColor,
                            shape = RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_send), // Using built-in checkmark
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExperienceSelectionScreenPreview() {
    SheSoulTheme {
        ExperienceSelectionScreen()
    }
}
