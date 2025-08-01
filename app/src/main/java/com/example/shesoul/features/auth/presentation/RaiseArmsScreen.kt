package com.example.shesoul.features.auth.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shesoul.R
import com.example.shesoul.ui.components.HorizontalWaveButton
import com.example.shesoul.ui.theme.PoppinsFamily
import com.example.shesoul.ui.theme.SheSoulTheme

@Composable
fun RaiseArmsScreen(
    onContinue: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Main content - centered vertically
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Title text - "Then Raise Both Arms Above Your Head"
                Text(
                    text = "Then Raise Both Arms Above Your Head",
                    fontFamily = PoppinsFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    color = Color(0xFF8254FF),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 2.dp)
                )

                // Breast examination image with raised arms
                Image(
                    painter = painterResource(id = R.drawable.breast2),
                    contentDescription = "Breast self-examination with raised arms",
                    modifier = Modifier
                        .width(365.dp)
                        .height(329.dp)
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Description text below image
                Text(
                    text = "Before touching anything, just observe.\nLook at both breasts carefully",
                    fontFamily = PoppinsFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = Color(0xFF8254FF),
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp,
                    modifier = Modifier
                        .width(310.dp)
                        .height(48.dp)
                        .wrapContentHeight(Alignment.CenterVertically)
                )
            }

            // Tip text above continue button
            Text(
                text = "Tip: You can even take a monthly picture (just for yourself) to track changes over time.",
                fontFamily = PoppinsFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            )

            // Bottom continue button with gradient animation
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalWaveButton(
                    onClick = { /* Animation will start */ },
                    onAnimationComplete = onContinue,
                    modifier = Modifier.fillMaxWidth(),
                    text = "Continue >"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RaiseArmsScreenPreview() {
    SheSoulTheme {
        RaiseArmsScreen()
    }
}
