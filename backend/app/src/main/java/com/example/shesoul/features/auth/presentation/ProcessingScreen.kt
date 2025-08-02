package com.example.shesoul.features.auth.presentation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shesoul.ui.theme.PlayfairDisplayFamily
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ProcessingScreen(
    onProcessingComplete: () -> Unit = {}
) {
    var targetProgress by remember { mutableStateOf(0f) }
    
    // Animate progress from 0 to 1 over 3 seconds
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 3000, easing = LinearEasing),
        label = "progress_animation"
    )
    
    // Infinite rotation for subtle glow effect
    val infiniteTransition = rememberInfiniteTransition(label = "glow_rotation")
    val glowRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "glow_rotation"
    )
    
    // Start the animation when the screen loads
    LaunchedEffect(Unit) {
        targetProgress = 1f
        // Wait for animation to complete, then call completion callback
        delay(3200) // A bit longer than animation to ensure completion
        onProcessingComplete()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Branding with status bar padding
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.statusBars.asPaddingValues())
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
            }
            
            // Center content with progress dial
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Progress text
                Text(
                    text = "Setting up your profile...",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 48.dp)
                )
                
                // Circular Progress Dial
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(200.dp)
                ) {
                    // Progress Ring
                    Canvas(
                        modifier = Modifier
                            .size(200.dp)
                            .rotate(-90f) // Start from top
                            .rotate(glowRotation * 0.1f) // Subtle rotation for glow effect
                    ) {
                        drawCircularProgress(
                            progress = animatedProgress,
                            size = size,
                            glowPhase = glowRotation
                        )
                    }
                    
                    // Progress Percentage Text
                    Text(
                        text = "${(animatedProgress * 100).toInt()}%",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF9092FF),
                        textAlign = TextAlign.Center
                    )
                }
                
                // Optional status message
                Text(
                    text = when {
                        animatedProgress < 0.2f -> "✨ Analyzing your preferences..."
                        animatedProgress < 0.4f -> "🎨 Personalizing your experience..."
                        animatedProgress < 0.6f -> "💫 Crafting your journey..."
                        animatedProgress < 0.8f -> "🌸 Almost there..."
                        animatedProgress < 0.95f -> "✅ Finalizing details..."
                        else -> "🎉 Welcome to She&Soul!"
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 32.dp)
                )
            }
        }
    }
}

private fun DrawScope.drawCircularProgress(
    progress: Float,
    size: Size,
    glowPhase: Float = 0f
) {
    val strokeWidth = 12.dp.toPx()
    val radius = (size.minDimension - strokeWidth) / 2
    val center = Offset(size.width / 2, size.height / 2)
    
    // Background circle (light gray)
    drawCircle(
        color = Color(0xFFF0F0F0),
        radius = radius,
        center = center,
        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
    )
    
    // Progress arc with gradient
    val sweepAngle = 360f * progress
    if (sweepAngle > 0) {
        drawArc(
            brush = Brush.sweepGradient(
                colors = listOf(
                    Color(0xFFBBBDFF),
                    Color(0xFF9092FF),
                    Color(0xFF7B6EFF),
                    Color(0xFFBBBDFF)
                )
            ),
            startAngle = 0f,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            topLeft = Offset(
                center.x - radius,
                center.y - radius
            ),
            size = Size(radius * 2, radius * 2)
        )
    }
    
    // Optional: Add a small dot at the end of progress for extra polish
    if (progress > 0) {
        val angle = Math.toRadians((sweepAngle - 90).toDouble())
        val dotX = center.x + radius * cos(angle).toFloat()
        val dotY = center.y + radius * sin(angle).toFloat()
        
        drawCircle(
            color = Color.White,
            radius = strokeWidth / 3,
            center = Offset(dotX, dotY)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProcessingScreenPreview() {
    ProcessingScreen(
        onProcessingComplete = {
            println("Processing completed!")
        }
    )
}
