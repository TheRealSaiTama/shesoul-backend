package com.example.shesoul.widgets

import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shesoul.ui.theme.PoppinsFamily
import com.example.shesoul.ui.theme.SheSoulTheme
import kotlin.math.sin

@Composable
fun WaveFillButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    buttonText: String = "Agree & Continue",
    backgroundColor: Color = Color.White,
    textColor: Color = Color.Black,
    waveColorLight: Color = Color.LightGray,
    waveColorDark: Color = Color.Gray,
    fillProgress: Float,
    clearProgress: Float, // <<< CHANGE: New progress for the clearing wave
    isClickable: Boolean,
    showWave: Boolean,
    clearWaveColor: Color = backgroundColor // <<< CHANGE: Color for the clearing wave
) {
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
            .height(50.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(backgroundColor)
            .clickable(enabled = isClickable) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    if (showWave) {
                        // --- Draw Act I: The Grey Fill Wave ---
                        val fillPath = createWavePath(
                            size = this.size,
                            wavePhase = wavePhase,
                            fillProgress = fillProgress
                        )
                        drawIntoCanvas { canvas ->
                            val paint = Paint().apply {
                                isAntiAlias = true
                                style = Paint.Style.FILL
                                shader = LinearGradient(
                                    0f, size.height, 0f, 0f,
                                    waveColorDark.toArgb(),
                                    waveColorLight.toArgb(),
                                    Shader.TileMode.CLAMP
                                )
                            }
                            canvas.nativeCanvas.drawPath(fillPath.asAndroidPath(), paint)
                        }

                        // --- Draw Act II: The White Clearing Wave ---
                        if (clearProgress > 0f) {
                            val clearPath = createWavePath(
                                size = this.size,
                                wavePhase = wavePhase, // Use same phase for consistent shape
                                fillProgress = clearProgress
                            )
                            drawIntoCanvas { canvas ->
                                val paint = Paint().apply {
                                    isAntiAlias = true
                                    style = Paint.Style.FILL
                                    color = clearWaveColor.toArgb() // Use solid background color
                                }
                                canvas.nativeCanvas.drawPath(clearPath.asAndroidPath(), paint)
                            }
                        }
                    }
                    drawContent()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = buttonText,
                color = textColor,
                fontFamily = PoppinsFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun createWavePath(
    size: Size,
    wavePhase: Float,
    fillProgress: Float
): Path {
    val waveAmplitude = 15f
    val waveFrequency = 2f

    return Path().apply {
        moveTo(0f, size.height)
        // This calculation now works for both the fill and clear waves
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


@Preview(showBackground = true)
@Composable
fun WaveFillButtonPreview() {
    SheSoulTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            WaveFillButton(
                onClick = {},
                modifier = Modifier,
                fillProgress = 1f,     // Preview with button fully filled...
                clearProgress = 0.5f,  // ...and the clear animation halfway.
                isClickable = true,
                showWave = true
            )
        }
    }
}