package com.example.shesoul.features.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shesoul.ui.components.HorizontalWaveButton
import com.example.shesoul.ui.theme.PoppinsFamily
import com.example.shesoul.ui.theme.SheSoulTheme

@Composable
fun IndianBreastCancerStatisticsScreen(
    onContinue: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
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
                // "50%" text - exactly like the image
                Text(
                    text = "50%",
                    fontFamily = PoppinsFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 96.sp,
                    color = Color(0xFF8254FF),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Description text - exactly like the image
                Text(
                    text = "Indian women diagnosed\nwith breast cancer are under\nthe age of 50.",
                    fontFamily = PoppinsFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                    color = Color(0xFF8254FF),
                    textAlign = TextAlign.Center,
                    lineHeight = 30.sp
                )
            }

            // Bottom continue button with gradient animation
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
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
fun IndianBreastCancerStatisticsScreenPreview() {
    SheSoulTheme {
        IndianBreastCancerStatisticsScreen()
    }
}
