package com.example.shesoul.features.auth.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shesoul.ui.components.HorizontalWaveButton
import com.example.shesoul.ui.theme.PlayfairDisplayFamily
import com.example.shesoul.ui.theme.PoppinsFamily
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

private enum class HeightUnit { CM, FT }

private data class FeetAndInches(val feet: Int, val inches: Int) {
    override fun toString(): String {
        return "$feet ft $inches in"
    }
}

@Composable
fun HeightSelectionScreen(
    onContinue: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    var selectedUnit by remember { mutableStateOf(HeightUnit.CM) }
    var selectedHeightInCm by remember { mutableStateOf(160) }
    val snackbarHostState = remember { SnackbarHostState() }
    var inlineError by remember { mutableStateOf<String?>(null) }
    val saveState = authViewModel.saveState.observeAsState(initial = SaveState.Idle)

    LaunchedEffect(saveState.value) {
        when (val state = saveState.value) {
            is SaveState.Success -> {
                inlineError = null
                onContinue()
            }
            is SaveState.Error -> {
                inlineError = state.message
                snackbarHostState.showSnackbar(state.message ?: "Couldn't save height")
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(snackbarData = data)
            }
        }
    ) { _ ->
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
                        .padding(top = 24.dp),
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
                        text = "What's your height?",
                        fontSize = 22.sp,
                        fontFamily = PoppinsFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    UnitSelector(
                        selectedUnit = selectedUnit,
                        onUnitSelected = { selectedUnit = it }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    HeightPicker(
                        unit = selectedUnit,
                        onHeightChange = { heightInCm ->
                            selectedHeightInCm = heightInCm
                        }
                    )
                }

                if (inlineError != null) {
                    Text(
                        text = inlineError!!,
                        color = Color(0xFFD32F2F),
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .padding(bottom = 8.dp)
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
                    val isLoading = saveState.value is SaveState.Loading
                    HorizontalWaveButton(
                        onClick = {
                            authViewModel.setUserHeight(selectedHeightInCm.toDouble())
                        },
                        text = if (isLoading) "Saving..." else "Continue",
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
}

@Composable
private fun UnitSelector(
    selectedUnit: HeightUnit,
    onUnitSelected: (HeightUnit) -> Unit
) {
    val selectedColor = Brush.verticalGradient(listOf(Color(0xFFBBBDFF), Color(0xFF9092FF)))
    val unselectedColor = Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent))

    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        UnitButton(
            text = "cm",
            isSelected = selectedUnit == HeightUnit.CM,
            background = if (selectedUnit == HeightUnit.CM) selectedColor else unselectedColor,
            textColor = if (selectedUnit == HeightUnit.CM) Color.White else Color(0xFF9092FF),
            onClick = { onUnitSelected(HeightUnit.CM) }
        )
        UnitButton(
            text = "ft",
            isSelected = selectedUnit == HeightUnit.FT,
            background = if (selectedUnit == HeightUnit.FT) selectedColor else unselectedColor,
            textColor = if (selectedUnit == HeightUnit.FT) Color.White else Color(0xFF9092FF),
            onClick = { onUnitSelected(HeightUnit.FT) }
        )
    }
}

@Composable
private fun UnitButton(text: String, isSelected: Boolean, background: Brush, textColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.width(100.dp).height(44.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        border = if (!isSelected) BorderStroke(1.dp, Color(0xFF9092FF)) else null,
        contentPadding = PaddingValues()
    ) {
        Box(modifier = Modifier.fillMaxSize().background(background, shape = RoundedCornerShape(50)), contentAlignment = Alignment.Center) {
            Text(text = text, color = textColor, fontSize = 16.sp, fontFamily = PoppinsFamily, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun HeightPicker(unit: HeightUnit, onHeightChange: (Int) -> Unit) {
    val (displayList, initialIndex) = remember(unit) {
        if (unit == HeightUnit.CM) {
            val cmList = (120..220).toList()
            val initialCm = 160
            cmList.map { it.toString() } to maxOf(0, cmList.indexOf(initialCm) - 2)
        } else {
            val ftList = mutableListOf<FeetAndInches>()
            for (feet in 4..7) { for (inches in 0..11) { ftList.add(FeetAndInches(feet, inches)) } }
            val initialFt = FeetAndInches(5, 6)
            ftList.map { it.toString() } to maxOf(0, ftList.indexOf(initialFt) - 2)
        }
    }

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)

    val centerIndex by remember { derivedStateOf {
        val layoutInfo = listState.layoutInfo
        if (layoutInfo.visibleItemsInfo.isEmpty()) -1 else {
            val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
            layoutInfo.visibleItemsInfo.minByOrNull { abs(it.offset + it.size / 2 - viewportCenter) }?.index ?: -1
        }
    }}

    LaunchedEffect(listState.isScrollInProgress, unit) {
        if (!listState.isScrollInProgress && centerIndex != -1) {
            val heightInCm = if (unit == HeightUnit.CM) {
                displayList[centerIndex].toInt()
            } else {
                val parts = displayList[centerIndex].split(" ")
                val feet = parts[0].toInt()
                val inches = parts[2].toInt()
                ((feet * 12 + inches) * 2.54).roundToInt()
            }
            onHeightChange(heightInCm)
        }
    }

    Box(modifier = Modifier.height(280.dp).fillMaxWidth().background(brush = Brush.radialGradient(colors = listOf(Color(0xFFF3F2FF), Color.White), radius = 250f)), contentAlignment = Alignment.Center) {
        LazyColumn(state = listState, contentPadding = PaddingValues(vertical = 115.dp), modifier = Modifier.fillMaxSize()) {
            items(displayList.size) { index ->
                val isSelected = (index == centerIndex)
                Box(modifier = Modifier.height(50.dp).fillMaxWidth().clickable(enabled = false, onClick = {}), contentAlignment = Alignment.Center) {
                    Box(modifier = if (isSelected) Modifier.width(140.dp).height(45.dp).background(color = Color(0xFF6D5FFD), shape = RoundedCornerShape(16.dp)) else Modifier, contentAlignment = Alignment.Center) {
                        Text(text = displayList[index], color = if (isSelected) Color.White else Color(0xFF6D5FFD), fontSize = 26.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HeightSelectionScreenPreview() {
    HeightSelectionScreen(
        onContinue = { println("Continue button clicked!") }
    )
}