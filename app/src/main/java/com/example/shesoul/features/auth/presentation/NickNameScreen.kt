package com.example.shesoul.features.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shesoul.R
import com.example.shesoul.ui.components.HorizontalWaveButton
import com.example.shesoul.ui.theme.PlayfairDisplayFamily
import com.example.shesoul.ui.theme.SheSoulTheme

@Composable
fun NickNameScreen(
    onContinue: () -> Unit = {},
    onSkip: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    var nickName by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 56.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

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

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "What would you like to be called?",
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = Color(0xFF2D2D2D),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = nickName,
                onValueChange = { nickName = it },
                placeholder = {
                    Text(
                        "Nick Name",
                        color = Color(0xFF8588FF),
                        fontFamily = NunitoFamily,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                modifier = Modifier.width(346.dp),
                shape = RoundedCornerShape(12.dp),
                textStyle = TextStyle(
                    fontFamily = NunitoFamily,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF8588FF),
                    unfocusedBorderColor = Color(0xFF8588FF)
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .width(346.dp)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HorizontalWaveButton(
                    onClick = {
                        authViewModel.setUserNickname(nickName)
                        onContinue()
                    },
                    text = "Continue >",
                    startColor = Color(0xFFE0BBFF),
                    endColor = Color(0xFF9092FF),
                    modifier = Modifier
                        .width(156.dp)
                        .height(50.dp),
                    cornerRadius = 12.dp
                )

                Button(
                    onClick = {
                        authViewModel.setUserNickname(null)
                        onSkip()
                    },
                    modifier = Modifier
                        .width(156.dp)
                        .height(50.dp)
                        .border(
                            width = 2.dp,
                            color = Color(0xFF9092FF),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Text(
                        text = "Skip >",
                        color = Color(0xFF9092FF),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NickNameScreenPreview() {
    SheSoulTheme {
        NickNameScreen(
            onContinue = { },
            onSkip = { }
        )
    }
}