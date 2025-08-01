package com.example.shesoul.ui.components

import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shesoul.ui.theme.PoppinsFamily
import kotlin.math.sin

@Composable
fun HorizontalWaveButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    startColor: Color = Color(0xFFE0BBFF),
    endColor: Color = Color(0xFF9092FF),
    textColor: Color = Color.White,
    height: Dp = 44.dp,
    cornerRadius: Dp = 10.dp,
    animationDuration: Int = 1200,
    useVerticalGradient: Boolean = false,
    onAnimationComplete: () -> Unit = {}
) {
    var isAnimating by remember { mutableStateOf(false) }
    val waveProgress = remember { Animatable(0f) }

    LaunchedEffect(isAnimating) {
        if (isAnimating) {
            waveProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = animationDuration, easing = LinearEasing)
            )
            waveProgress.snapTo(0f)
            isAnimating = false
            onAnimationComplete()
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "wave_oscillation")
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wavePhase"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                if (useVerticalGradient) {
                    Brush.verticalGradient(colors = listOf(endColor, startColor))
                } else {
                    Brush.horizontalGradient(colors = listOf(startColor, endColor))
                }
            )
            .clickable(enabled = !isAnimating) {
                isAnimating = true
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    if (isAnimating && waveProgress.value > 0f) {
                        val waveFillPath = createWaveFillPath(
                            size = this.size,
                            wavePhase = wavePhase,
                            fillProgress = waveProgress.value
                        )
                        
                        drawIntoCanvas { canvas ->
                            val paint = Paint().apply {
                                isAntiAlias = true
                                style = Paint.Style.FILL

                                // Create brighter colors for the wave to make it visible
                                val waveStartColor = lerp(startColor, Color.White, 0.2f)
                                val waveEndColor = lerp(endColor, Color.White, 0.2f)

                                shader = if (useVerticalGradient) {
                                    LinearGradient(
                                        0f, 0f, 0f, size.height,
                                        waveEndColor.toArgb(),
                                        waveStartColor.toArgb(),
                                        Shader.TileMode.CLAMP
                                    )
                                } else {
                                    LinearGradient(
                                        0f, size.height, 0f, 0f,
                                        waveStartColor.toArgb(),
                                        waveEndColor.toArgb(),
                                        Shader.TileMode.CLAMP
                                    )
                                }
                            }
                            canvas.nativeCanvas.drawPath(waveFillPath.asAndroidPath(), paint)
                        }
                    }
                    
                    drawContent()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = textColor,
                fontFamily = PoppinsFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun createWaveFillPath(
    size: Size,
    wavePhase: Float,
    fillProgress: Float
): Path {
    val waveAmplitude = 15f
    val waveFrequency = 2f

    return Path().apply {
        moveTo(0f, size.height)
        
        val totalHeightToTravel = size.height + (2 * waveAmplitude)
        val baseHeight = (size.height + waveAmplitude) - (fillProgress * totalHeightToTravel)
        
        for (x in 0..size.width.toInt()) {
            val angle = (x.toFloat() / size.width) * waveFrequency * 2 * Math.PI.toFloat() +
                    wavePhase * (Math.PI / 180).toFloat()
            val y = (baseHeight - waveAmplitude * sin(angle)).coerceIn(0f, size.height)
            
            if (x == 0) {
                lineTo(0f, y)
            } else {
                lineTo(x.toFloat(), y)
            }
        }
        
        lineTo(size.width, size.height)
        close()
    }
}