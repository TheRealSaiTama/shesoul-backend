package com.example.shesoul.features.auth.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shesoul.R
import com.example.shesoul.ui.theme.PoppinsFamily
import com.example.shesoul.ui.theme.SheSoulTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BreastCancerAwarenessJourneyScreen(
    onJourneyComplete: () -> Unit = {}
) {
    var currentStep by remember { mutableStateOf(0) }
    var isAnimating by remember { mutableStateOf(false) }
    
    val steps = listOf<@Composable () -> Unit>(
        { Step1in8() },
        { Step23M() },
        { Step50Percent() },
        { Step5Minutes() },
        { StepLearn() },
        { StepLookInMirror() },
        { StepRaiseArms() },
        { StepSoftPadFingers() },
        { StepLookOutFor() }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Main content area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                // Animated content transition
                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = {
                        (slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(600, easing = FastOutSlowInEasing)
                        ) + fadeIn(animationSpec = tween(600, easing = FastOutSlowInEasing)))
                        .togetherWith(slideOutHorizontally(
                            targetOffsetX = { -it },
                            animationSpec = tween(600, easing = FastOutSlowInEasing)
                        ) + fadeOut(animationSpec = tween(600, easing = FastOutSlowInEasing)))
                    },
                    label = "step_transition"
                ) { step ->
                    steps[step]()
                }
            }

            // Progress indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(steps.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                if (index <= currentStep) Color(0xFF8254FF) 
                                else Color(0xFF8254FF).copy(alpha = 0.3f)
                            )
                    )
                    if (index < steps.size - 1) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }

            // Navigation arrow button
            Box(
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 48.dp)
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFE0BBFF),
                                Color(0xFF9092FF)
                            )
                        )
                    )
                    .clickable(enabled = !isAnimating) {
                        if (currentStep < steps.size - 1) {
                            isAnimating = true
                            currentStep++
                        } else {
                            onJourneyComplete()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Next",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }

    // Reset animation state after transition
    LaunchedEffect(currentStep) {
        if (isAnimating) {
            delay(300)
            isAnimating = false
        }
    }
}

@Composable
private fun Step1in8() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "1 in 8",
            fontFamily = PoppinsFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 96.sp,
            color = Color(0xFF8254FF),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "women worldwide will be\ndiagnosed with breast\ncancer in their lifetime.",
            fontFamily = PoppinsFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            color = Color(0xFF8254FF),
            textAlign = TextAlign.Center,
            lineHeight = 30.sp
        )
    }
}

@Composable
private fun Step23M() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "2.3M",
            fontFamily = PoppinsFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 96.sp,
            color = Color(0xFF8254FF),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "new cases were reported\nglobally in 2020 (WHO).",
            fontFamily = PoppinsFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            color = Color(0xFF8254FF),
            textAlign = TextAlign.Center,
            lineHeight = 30.sp
        )
    }
}

@Composable
private fun Step50Percent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "50%",
            fontFamily = PoppinsFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 96.sp,
            color = Color(0xFF8254FF),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
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
}

@Composable
private fun Step5Minutes() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "5",
            fontFamily = PoppinsFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 96.sp,
            color = Color(0xFF8254FF),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Monthly self-checks take 5\nminutes but can save your\nlife",
            fontFamily = PoppinsFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            color = Color(0xFF8254FF),
            textAlign = TextAlign.Center,
            lineHeight = 30.sp
        )
    }
}

@Composable
private fun StepLearn() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Learn",
            fontFamily = PoppinsFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 96.sp,
            color = Color(0xFF8254FF),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "to Feel the Signs",
            fontFamily = PoppinsFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            color = Color(0xFF8254FF),
            textAlign = TextAlign.Center,
            lineHeight = 30.sp
        )
    }
}

@Composable
private fun StepLookInMirror() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Look In The Mirror",
            fontFamily = PoppinsFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            color = Color(0xFF8254FF),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Image(
            painter = painterResource(id = R.drawable.breast_three),
            contentDescription = "Breast self-examination guide",
            modifier = Modifier
                .width(300.dp)
                .height(270.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Before touching anything, just observe.\nLook at both breasts carefully",
            fontFamily = PoppinsFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = Color(0xFF8254FF),
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun StepRaiseArms() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Then Raise Both Arms Above Your Head",
            fontFamily = PoppinsFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            color = Color(0xFF8254FF),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Image(
            painter = painterResource(id = R.drawable.breast2),
            contentDescription = "Breast self-examination with raised arms",
            modifier = Modifier
                .width(300.dp)
                .height(270.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Before touching anything, just observe.\nLook at both breasts carefully",
            fontFamily = PoppinsFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = Color(0xFF8254FF),
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun StepSoftPadFingers() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Make A Soft Pad Using Your Fingers",
            fontFamily = PoppinsFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            color = Color(0xFF8254FF),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Image(
            painter = painterResource(id = R.drawable.hand),
            contentDescription = "Hand position for breast self-examination",
            modifier = Modifier
                .width(300.dp)
                .height(270.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Use the pads (not tips) of your index,\nmiddle, and ring finger",
            fontFamily = PoppinsFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = Color(0xFF8254FF),
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun StepLookOutFor() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(0.5f))
        
        // Box for overlapping text and image with descriptions inside
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp), // Taller container to fit everything
            contentAlignment = Alignment.Center // Center everything in the middle
        ) {
            // Box to hold both the image and the overlapping text descriptions
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                // Image as background
                Image(
                    painter = painterResource(id = R.drawable.lump),
                    contentDescription = "Signs to look out for: lumps, changes, discharge",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(horizontal = 4.dp) // Add horizontal padding to center and stretch
                        .align(Alignment.Center) // Center the image in the container
                )
                
                // "Look Out For" text overlapping in the middle of the image
                Text(
                    text = "Look Out For",
                    fontFamily = PoppinsFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 28.sp,
                    color = Color(0xFF8254FF),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 20.dp)
                )
                    // Row with descriptions overlapping the middle-bottom of the image
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                            .padding(top = 120.dp)
                            .padding(horizontal = 16.dp), // Add horizontal padding to bring texts closer
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Top
                    ) {
            Text(
                text = "Hard Lumps,\nKnots or\nthickening",
                fontFamily = PoppinsFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = Color(0xFF8254FF),
                textAlign = TextAlign.Center,
                lineHeight = 18.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Sudden\nChanges in\nBreast",
                fontFamily = PoppinsFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = Color(0xFF8254FF),
                textAlign = TextAlign.Center,
                lineHeight = 18.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Discharge\nin Nipples",
                fontFamily = PoppinsFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = Color(0xFF8254FF),
                textAlign = TextAlign.Center,
                lineHeight = 18.sp,
                modifier = Modifier.weight(1f)
            )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Text(
        //     text = "Tip: Imagine you’re trying to feel a tiny seed under a cloth — slow, smooth, and focused.",
        //     fontFamily = PoppinsFamily,
        //     fontWeight = FontWeight.Normal,
        //     fontSize = 14.sp,
        //     color = Color(0xFF8254FF).copy(alpha = 0.8f),
        //     textAlign = TextAlign.Center,
        //     lineHeight = 20.sp,
        //     modifier = Modifier.padding(bottom = 24.dp)
        // )
    }
}

@Preview(showBackground = true)
@Composable
fun BreastCancerAwarenessJourneyScreenPreview() {
    SheSoulTheme {
        BreastCancerAwarenessJourneyScreen()
    }
}
