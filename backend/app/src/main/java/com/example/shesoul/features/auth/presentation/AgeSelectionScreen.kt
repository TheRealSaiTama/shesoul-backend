package com.example.shesoul.features.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shesoul.ui.components.HorizontalWaveButton
import com.example.shesoul.ui.theme.PlayfairDisplayFamily
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun AgeSelectionScreen(
    onContinue: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    var selectedAge by remember { mutableStateOf(27) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.statusBars.asPaddingValues())
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val brandText = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color(0xFF9092FF))) { append("She") }
                    withStyle(style = SpanStyle(color = Color.Black)) { append("&") }
                    withStyle(style = SpanStyle(color = Color(0xFF9092FF))) { append("Soul") }
                }

                Text(
                    text = brandText,
                    fontSize = 32.sp,
                    fontFamily = PlayfairDisplayFamily,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "How old are you?",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                InfiniteAgePicker(
                    onAgeChange = { age ->
                        selectedAge = age
                    }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 16.dp)
                    .padding(WindowInsets.navigationBars.asPaddingValues()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalWaveButton(
                    onClick = {
                        authViewModel.setUserAge(selectedAge)
                        onContinue()
                    },
                    text = "Continue",
                    startColor = Color(0xFFBBBDFF),
                    endColor = Color(0xFF9092FF),
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    cornerRadius = 10.dp,
                    useVerticalGradient = true
                )
            }
        }
    }
}

@Composable
private fun InfiniteAgePicker(
    onAgeChange: (Int) -> Unit
) {
    var selectedAge by remember { mutableStateOf(27) }
    val ageRange = (12..100).toList()
    val coroutineScope = rememberCoroutineScope()

    val initialIndex = maxOf(0, ageRange.indexOf(selectedAge) - 2)
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)

    val centerIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            if (visibleItems.isEmpty()) {
                -1
            } else {
                val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
                visibleItems.minByOrNull { abs(it.offset + it.size / 2 - viewportCenter) }?.index ?: -1
            }
        }
    }

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress && centerIndex != -1) {
            val newAge = ageRange[centerIndex]
            if (newAge != selectedAge) {
                selectedAge = newAge
                onAgeChange(newAge)
            }
        }
    }

    Box(
        modifier = Modifier
            .height(280.dp)
            .fillMaxWidth()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFF3F2FF), Color.White),
                    radius = 250f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(90.dp)
                .height(45.dp)
                .background(
                    color = Color(0xFF6D5FFD),
                    shape = RoundedCornerShape(16.dp)
                )
        )
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(vertical = 117.5.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(ageRange.size) { index ->
                val isSelected = (index == centerIndex)
                Box(
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            val scrollToIndex = maxOf(0, index - 2)
                            coroutineScope.launch {
                                listState.animateScrollToItem(scrollToIndex)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = ageRange[index].toString(),
                        color = if (isSelected) Color.White else Color(0xFF6D5FFD),
                        fontSize = 26.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .height((140.dp))
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.White, Color.White.copy(alpha = 0.5f), Color.Transparent),
                    )
                )
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height((140.dp))
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.White.copy(alpha = 0.5f), Color.White),
                    )
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AgeSelectionScreenPreview() {
    AgeSelectionScreen(
        onContinue = {
            println("Continue button clicked!")
        }
    )
}