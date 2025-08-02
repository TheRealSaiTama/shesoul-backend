package com.example.shesoul.features.auth.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.shesoul.ui.theme.PoppinsFamily
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

private enum class WeightUnit { KG, LBS }

@Composable
fun WeightSelectionScreen(
    onContinue: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    var selectedUnit by remember { mutableStateOf(WeightUnit.KG) }
    var selectedWeightInKg by remember { mutableStateOf(60) }

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
                    text = "And your current weight?",
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

                WeightPicker(
                    unit = selectedUnit,
                    onWeightChange = { weightInKg ->
                        selectedWeightInKg = weightInKg
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
                        authViewModel.setUserWeight(selectedWeightInKg.toDouble())
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
private fun UnitSelector(
    selectedUnit: WeightUnit,
    onUnitSelected: (WeightUnit) -> Unit
) {
    val selectedColor = Brush.verticalGradient(
        listOf(Color(0xFFBBBDFF), Color(0xFF9092FF))
    )
    val unselectedColor = Brush.verticalGradient(
        listOf(Color.Transparent, Color.Transparent)
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        UnitButton(
            text = "kg",
            isSelected = selectedUnit == WeightUnit.KG,
            background = if (selectedUnit == WeightUnit.KG) selectedColor else unselectedColor,
            textColor = if (selectedUnit == WeightUnit.KG) Color.White else Color(0xFF9092FF),
            onClick = { onUnitSelected(WeightUnit.KG) }
        )

        UnitButton(
            text = "lbs",
            isSelected = selectedUnit == WeightUnit.LBS,
            background = if (selectedUnit == WeightUnit.LBS) selectedColor else unselectedColor,
            textColor = if (selectedUnit == WeightUnit.LBS) Color.White else Color(0xFF9092FF),
            onClick = { onUnitSelected(WeightUnit.LBS) }
        )
    }
}

@Composable
private fun UnitButton(
    text: String,
    isSelected: Boolean,
    background: Brush,
    textColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.width(100.dp).height(44.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        border = if (!isSelected) BorderStroke(1.dp, Color(0xFF9092FF)) else null,
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = Modifier.fillMaxSize().background(background, shape = RoundedCornerShape(50)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = textColor,
                fontSize = 16.sp,
                fontFamily = PoppinsFamily,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun WeightPicker(
    unit: WeightUnit,
    onWeightChange: (Int) -> Unit
) {
    val (displayList, initialIndex) = remember(unit) {
        if (unit == WeightUnit.KG) {
            val kgList = (40..150).toList()
            val initialKg = 60
            kgList.map { it.toString() } to maxOf(0, kgList.indexOf(initialKg) - 2)
        } else {
            val lbsList = (90..330).toList()
            val initialLbs = 132 // Approx 60kg
            lbsList.map { it.toString() } to maxOf(0, lbsList.indexOf(initialLbs) - 2)
        }
    }

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)

    val centerIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            if (layoutInfo.visibleItemsInfo.isEmpty()) -1 else {
                val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
                layoutInfo.visibleItemsInfo.minByOrNull { abs(it.offset + it.size / 2 - viewportCenter) }?.index ?: -1
            }
        }
    }

    LaunchedEffect(listState.isScrollInProgress, unit) {
        if (!listState.isScrollInProgress && centerIndex != -1) {
            val weightInKg = if (unit == WeightUnit.KG) {
                displayList[centerIndex].toInt()
            } else {
                val lbs = displayList[centerIndex].toInt()
                (lbs / 2.20462).roundToInt()
            }
            onWeightChange(weightInKg)
        }
    }

    Box(
        modifier = Modifier.height(280.dp).fillMaxWidth().background(
            brush = Brush.radialGradient(colors = listOf(Color(0xFFF3F2FF), Color.White), radius = 250f)
        ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.width(90.dp).height(45.dp).background(
                color = Color(0xFF6D5FFD), shape = RoundedCornerShape(16.dp)
            )
        )
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(vertical = 117.5.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(displayList.size) { index ->
                val isSelected = (index == centerIndex)
                Box(
                    modifier = Modifier.height(50.dp).fillMaxWidth().clickable(enabled = false, onClick = {}),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = displayList[index],
                        color = if (isSelected) Color.White else Color(0xFF6D5FFD),
                        fontSize = 26.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeightSelectionScreenPreview() {
    WeightSelectionScreen(
        onContinue = {
            println("Continue button clicked!")
        }
    )
}