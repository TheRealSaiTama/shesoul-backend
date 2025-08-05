package com.example.shesoul.features.auth.presentation

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
fun BreastAssessmentScreen(
    onContinue: () -> Unit = {},
    onNavigateToHome: () -> Unit = {}
) {
    var isVisible by remember { mutableStateOf(false) }
    
    // Beautiful entrance animation
    val screenAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "screen_fade_in"
    )
    
    val contentOffset by animateFloatAsState(
        targetValue = if (isVisible) 0f else 50f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "content_slide_up"
    )
    
    // Checkbox states
    var rightBreastLumps1 by remember { mutableStateOf(false) }
    var rightBreastLumps2 by remember { mutableStateOf(false) }
    var leftBreastLumps1 by remember { mutableStateOf(false) }
    var leftBreastLumps2 by remember { mutableStateOf(false) }
    var nothing by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(300) // Small delay for smooth transition
        isVisible = true
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .alpha(screenAlpha),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .offset(y = contentOffset.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            
            // Breast diagram - moved to center of screen
            Box(
                modifier = Modifier.size(280.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.breastdesign),
                    contentDescription = "Breast diagram",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
                
                // Title overlapping the image in the middle
                Text(
                    text = "Upper Inner Quadrant",
                    fontFamily = PoppinsFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color(0xFF8254FF),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.offset(y = (-100).dp)
                )
            }
            
            Spacer(modifier = Modifier.height(2.dp))
            
            // Question - moved down
            Text(
                text = "What Did You Feel ?",
                fontFamily = PoppinsFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Checkbox options - reduced spacing and moved down
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Left column
                    Column {
                        CheckboxOption(
                            text = "Lumps",
                            checked = rightBreastLumps1,
                            onCheckedChange = { rightBreastLumps1 = it }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        CheckboxOption(
                            text = "Lumps",
                            checked = rightBreastLumps2,
                            onCheckedChange = { rightBreastLumps2 = it }
                        )
                    }
                    
                    // Right column
                    Column {
                        CheckboxOption(
                            text = "Lumps",
                            checked = leftBreastLumps1,
                            onCheckedChange = { leftBreastLumps1 = it }
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        CheckboxOption(
                            text = "Lumps",
                            checked = leftBreastLumps2,
                            onCheckedChange = { leftBreastLumps2 = it }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(1.dp))
                
                // Nothing option centered
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CheckboxOption(
                        text = "Nothing",
                        checked = nothing,
                        onCheckedChange = { nothing = it }
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Continue button with same style as sign up page
            HorizontalWaveButton(
                onClick = { 
                    onNavigateToHome()
                },
                text = "Continue >",
                startColor = Color(0xFFE0BBFF), // Same gradient as SignUpScreen
                endColor = Color(0xFF9092FF),   // Same gradient as SignUpScreen
                modifier = Modifier.fillMaxWidth().height(44.dp),
                cornerRadius = 10.dp
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun CheckboxOption(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onCheckedChange(!checked) }
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF8254FF),
                uncheckedColor = Color(0xFF8254FF),
                checkmarkColor = Color.White
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontFamily = PoppinsFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BreastAssessmentScreenPreview() {
    SheSoulTheme {
        BreastAssessmentScreen(
            onContinue = { },
            onNavigateToHome = { }
        )
    }
}
