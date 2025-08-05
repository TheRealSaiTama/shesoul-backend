package com.example.shesoul.features.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.shesoul.R
import com.example.shesoul.ui.theme.PlayfairDisplayFamily
import com.example.shesoul.ui.theme.PoppinsFamily
import com.example.shesoul.ui.theme.SheSoulTheme

@Composable
fun PartnerWelcomeScreen(
    onAnimationComplete: () -> Unit = {}
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        // Auto-transition after 3 seconds (adjust timing as needed)
        kotlinx.coroutines.delay(3000L)
        onAnimationComplete()
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFFFFF), // Top: White
                            Color(0xFFBBBDFF)  // Bottom: Light purple
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                
                // Header - She&Soul branding
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
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .width(145.dp)
                        .height(42.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Animation Box - Couple GIF
                Box(
                    modifier = Modifier
                        .width(364.dp)
                        .height(410.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data("android.resource://${context.packageName}/raw/couple")
                            .decoderFactory(GifDecoder.Factory())
                            .build(),
                        contentDescription = "Couple Animation",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Main Quote Text
                Text(
                    text = "💖 You're the kind of partner every\nsoul deserves.",
                    fontSize = 24.sp,
                    fontFamily = PoppinsFamily,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    modifier = Modifier
                        .width(345.dp)
                        .height(72.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Descriptive Paragraph
                Text(
                    text = "Thank you for showing up, for caring, and for wanting to understand her better.\n" +
                            "Your support means more than you know — and trust us, she'll feel it too.\n\n" +
                            "Welcome to She&Soul. You're officially part of the journey.",
                    fontSize = 12.sp,
                    fontFamily = PoppinsFamily,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    modifier = Modifier
                        .width(372.dp)
                        .height(109.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PartnerWelcomeScreenPreview() {
    SheSoulTheme {
        PartnerWelcomeScreen {}
    }
}
