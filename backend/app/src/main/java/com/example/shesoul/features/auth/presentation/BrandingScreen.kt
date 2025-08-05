package com.example.shesoul.features.auth.presentation

import android.net.Uri
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.shesoul.R
import com.example.shesoul.ui.theme.PlayfairDisplayFamily
import com.example.shesoul.ui.theme.SheSoulPurple
import kotlinx.coroutines.delay

@Composable
fun BrandingScreen(onVideoEnd: () -> Unit) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    
    // Responsive title size based on screen width
    val screenWidth = configuration.screenWidthDp.dp
    val titleSize = (screenWidth.value * 0.08f).coerceIn(24f, 42f).sp
    
    LaunchedEffect(Unit) {
        delay(4000) // Show animation for 4 seconds (adjust as needed)
        onVideoEnd()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Minimal title space at very top
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = SheSoulPurple)) { append("She") }
                    withStyle(style = SpanStyle(color = Color.Black)) { append("&") }
                    withStyle(style = SpanStyle(color = SheSoulPurple)) { append("Soul") }
                },
                fontFamily = PlayfairDisplayFamily,
                fontWeight = FontWeight.Bold,
                fontSize = titleSize,
                modifier = Modifier
                    .padding(WindowInsets.statusBars.asPaddingValues())
                    .padding(vertical = 24.dp)
            )
            
            // ULTRA LARGE animation taking up remaining space
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp), // Tiny padding to prevent edge touching
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data("android.resource://${context.packageName}/raw/animated_logo_unscreen")
                        .decoderFactory(GifDecoder.Factory())
                        .build(),
                    contentDescription = "She&Soul Animation",
                    modifier = Modifier.fillMaxSize() // FILLS ENTIRE REMAINING SPACE!
                )
            }
        }
    }
}