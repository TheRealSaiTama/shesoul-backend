package com.example.shesoul.features.auth.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shesoul.R
import com.example.shesoul.ui.theme.InterFamily
import com.example.shesoul.ui.theme.PlayfairDisplayFamily
import com.example.shesoul.ui.theme.PoppinsFamily
import com.example.shesoul.ui.theme.SheSoulTheme
import com.example.shesoul.ui.theme.SheSoulPurple // <<< FIX: ADD THIS IMPORT LINE
import com.example.shesoul.widgets.WaveFillButton
import kotlinx.coroutines.delay

val LightPurple = Color(0xFFE0BBFF)
val DarkPurple = Color(0xFF9092FF)
val LinkColor = Color(0xFFB4AFFF)

@Composable
fun PrivacyScreen(onAgree: () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    val blurRadius by animateDpAsState(
        targetValue = if (visible) 16.dp else 0.dp,
        animationSpec = tween(durationMillis = 500, easing = LinearEasing)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(radius = blurRadius),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = SheSoulPurple)) { // This now works because of the import
                        append("She")
                    }
                    withStyle(style = SpanStyle(color = Color.Black)) {
                        append("&")
                    }
                    withStyle(style = SpanStyle(color = SheSoulPurple)) { // This also works
                        append("Soul")
                    }
                },
                fontFamily = PlayfairDisplayFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 48.sp
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.navigationBars.asPaddingValues()),
            contentAlignment = Alignment.BottomCenter
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(initialOffsetY = { it })
            ) {
                PrivacyCard(onAgree = onAgree)
            }
        }
    }
}

// The rest of the file (PrivacyCard, TermsAndPolicyText, etc.) remains unchanged.
@Composable
fun PrivacyCard(onAgree: () -> Unit) {
    val fillProgressAnim = remember { Animatable(0f) }
    val clearProgressAnim = remember { Animatable(0f) }
    var isClickable by remember { mutableStateOf(false) }
    var showWave by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(500L)
        fillProgressAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1500, easing = LinearEasing)
        )
        isClickable = true
        delay(400L)
        clearProgressAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1500, easing = LinearEasing)
        )
        showWave = false
    }

    Card(
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(LightPurple, DarkPurple)
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.security_user),
                    contentDescription = "Privacy Shield",
                    modifier = Modifier.size(48.dp),
                    contentScale = ContentScale.Inside
                )
                Text(
                    text = "Your privacy is our top priority.",
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = InterFamily,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center
                )
                TermsAndPolicyText()
                Spacer(modifier = Modifier.height(16.dp))

                WaveFillButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onAgree,
                    fillProgress = fillProgressAnim.value,
                    clearProgress = clearProgressAnim.value,
                    isClickable = isClickable,
                    showWave = showWave,
                    waveColorLight = Color.LightGray,
                    waveColorDark = Color.Gray
                )
            }
        }
    }
}

@Composable
fun TermsAndPolicyText() {
    val uriHandler = LocalUriHandler.current
    val annotatedString = buildAnnotatedString {
        append("Your personal journey deserves protection. Please take a moment to read and understand our ")
        pushStringAnnotation(tag = "TERMS", annotation = "https://example.com/terms")
        withStyle(style = SpanStyle(color = Color.White, fontWeight = FontWeight.SemiBold)) {
            append("Terms of service")
        }
        pop()
        append(" and ")
        pushStringAnnotation(tag = "POLICY", annotation = "https://example.com/privacy")
        withStyle(style = SpanStyle(color = Color.White, fontWeight = FontWeight.SemiBold)) {
            append("Privacy Policy")
        }
        pop()
        append(".\n\nWe're committed to keeping your data safe and using it only to improve your experience on She&Soul.")
    }

    ClickableText(
        text = annotatedString,
        style = MaterialTheme.typography.bodyMedium.copy(
            color = Color.Black.copy(alpha = 0.8f),
            fontFamily = PoppinsFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 12.sp,
            textAlign = TextAlign.Center
        ),
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "TERMS", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    uriHandler.openUri(annotation.item)
                }
            annotatedString.getStringAnnotations(tag = "POLICY", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    uriHandler.openUri(annotation.item)
                }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
fun PrivacyScreenPreview() {
    SheSoulTheme {
        PrivacyScreen(onAgree = {})
    }
}