    package com.example.shesoul.features.auth.presentation

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shesoul.ui.theme.PoppinsFamily
import com.example.shesoul.ui.theme.SheSoulTheme
import kotlin.math.sqrt

@Composable
fun AssessmentCompletionScreen(
    onAssessmentStart: () -> Unit = {},
    onNavigateToBreastHome: () -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    
    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }
    val centerX = screenWidthPx / 2f
    val centerY = screenHeightPx / 2f
    val maxRadius = sqrt((centerX * centerX) + (centerY * centerY))
    
    var isExpanded by remember { mutableStateOf(false) }
    var showAssessmentScreen by remember { mutableStateOf(false) }
    
    // Seamless burst animation with custom easing
    val animatedRadius by animateFloatAsState(
        targetValue = if (isExpanded) maxRadius * 1.2f else with(density) { 150.dp.toPx() },
        animationSpec = if (isExpanded) {
            keyframes {
                durationMillis = 1200
                with(density) { 150.dp.toPx() } at 0 using LinearOutSlowInEasing
                with(density) { 200.dp.toPx() } at 150 using FastOutSlowInEasing
                maxRadius * 0.7f at 400 using CubicBezierEasing(0.25f, 0.46f, 0.45f, 0.94f)
                maxRadius * 1.2f at 1200 using FastOutLinearInEasing
            }
        } else {
            spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessHigh
            )
        },
        label = "seamless_burst"
    )
    
    // Button opacity for smooth fade during expansion
    val buttonOpacity by animateFloatAsState(
        targetValue = if (isExpanded) 0f else 1f,
        animationSpec = if (isExpanded) {
            tween(
                durationMillis = 600,
                easing = FastOutLinearInEasing
            )
        } else {
            tween(
                durationMillis = 300,
                easing = LinearOutSlowInEasing
            )
        },
        label = "button_fade"
    )
    
    // Text scale for better visual continuity
    val textScale by animateFloatAsState(
        targetValue = if (isExpanded) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "text_scale"
    )
    
    // Track expansion completion for text fade out
    var isExpansionComplete by remember { mutableStateOf(false) }
    
    // Text opacity for beautiful fade out after expansion
    val textOpacity by animateFloatAsState(
        targetValue = if (isExpansionComplete) 0f else 1f,
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ),
        label = "text_fade_out"
    )
    
    // Animation completion listener with smoother trigger
    LaunchedEffect(animatedRadius, maxRadius) {
        if (isExpanded && animatedRadius >= maxRadius * 0.8f && !isExpansionComplete) {
            isExpansionComplete = true
            kotlinx.coroutines.delay(200) // Much faster transition
            showAssessmentScreen = true
        }
    }

    // Fallback trigger in case the animation condition isn't met
    LaunchedEffect(isExpanded) {
        if (isExpanded) {
            kotlinx.coroutines.delay(1500) // Shorter fallback time
            if (!showAssessmentScreen) {
                showAssessmentScreen = true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .clipToBounds(),
        contentAlignment = Alignment.Center
    ) {
        // Show assessment screen after animation completes
        if (showAssessmentScreen) {
            BreastAssessmentScreen(
                onContinue = onNavigateToBreastHome,
                onNavigateToHome = onNavigateToBreastHome
            )
        } else {
            // Original animation content
            // Circular reveal gradient background with smoother rendering
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                if (isExpanded) {
                    // Draw expanding circle with horizontal gradient like original
                    drawCircle(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFE0BBFF), // Light
                                Color(0xFF9092FF)  // Dark
                            ),
                            startX = 0f,
                            endX = size.width
                        ),
                        radius = animatedRadius,
                        center = Offset(centerX, centerY)
                    )
                }
            }
            
            // Original gradient circle button with fade effect
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFE0BBFF).copy(alpha = buttonOpacity), // Light
                                Color(0xFF9092FF).copy(alpha = buttonOpacity)  // Dark
                            )
                        )
                    )
                    .clickable(enabled = !isExpanded) {
                        isExpanded = true
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Start Assessment",
                    fontFamily = PoppinsFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = (24 * textScale).sp,
                    color = Color(0xFFFFFFFF).copy(alpha = if (isExpanded) textOpacity else buttonOpacity),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AssessmentCompletionScreenPreview() {
    SheSoulTheme {
        AssessmentCompletionScreen()
    }
}
