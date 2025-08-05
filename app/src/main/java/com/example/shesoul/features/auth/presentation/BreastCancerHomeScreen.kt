package com.example.shesoul.features.auth.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shesoul.R
import com.example.shesoul.ui.theme.SheSoulTheme

@Composable
fun BreastCancerAssessmentCard() {
    Card(
        modifier = Modifier
            .width(356.dp)
            .height(325.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7FF)),
        border = BorderStroke(1.dp, Color(0xFFE0E0FF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Box(contentAlignment = Alignment.TopCenter) {
                // Outer circle defines the solid color ring of the dial
                Box(
                    modifier = Modifier
                        .size(230.dp)
                        .background(Color(0xFF9092FF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    // Inner circle creates the empty space inside the dial
                    Box(
                        modifier = Modifier
                            .size(212.dp)
                            .background(Color(0xFFF7F7FF), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(id = R.drawable.breasthomescreen),
                                contentDescription = "Breast Assessment",
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "For Re-Assess", 
                                fontFamily = FontFamily(Font(R.font.poppins_medium)), 
                                fontWeight = FontWeight.Medium, 
                                fontSize = 16.sp
                            )
                            Text(
                                "25",
                                fontSize = 56.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF8254FF)
                            )
                            Text("Days Left", color = Color.Gray)
                        }
                    }
                }
                IconButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 8.dp, y = (-8).dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.refresh),
                        contentDescription = "Refresh",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Button(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .width(120.dp)
                    .height(28.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(),
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xFFE0BBFF), Color(0xFF9092FF))
                            )
                        )
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Re-Assess", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun BreastCancerProbabilityCard() {
    Card(
        modifier = Modifier
            .width(356.dp)
            .height(140.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7FF)),
        border = BorderStroke(1.dp, Color(0xFFE0E0FF))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                Text(
                    text = "Breast Cancer Probability",
                    fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Text(
                    text = "LOW",
                    fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 36.sp,
                    color = Color(0xFF8254FF)
                )
                Text(
                    text = "25 Days Left for next Assessment",
                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Color.Black
                )
            }
            
            Image(
                painter = painterResource(id = R.drawable.circle),
                contentDescription = "Probability Chart",
                modifier = Modifier.size(80.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreastCancerHomeScreen(
    onNavigateBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.boy),
                                contentDescription = "Profile",
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color.Gray, CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Hi, Angel",
                                fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp,
                                color = Color(0xFF8254FF)
                            )
                        }
                        
                        IconButton(onClick = { /* TODO */ }) {
                            Image(
                                painter = painterResource(id = R.drawable.pagelight),
                                contentDescription = "Notifications",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            
            // Main assessment card
            BreastCancerAssessmentCard()
            
            // Probability card
            BreastCancerProbabilityCard()
            
            // Add Your Partner Card (reusing from original)
            AddPartnerCard()
            
            // Get Report Card (reusing from original)
            GetReportCard()
            
            // Curated For You Section (reusing from original)
            CuratedForYouSection()
            
            // BMI Card (reusing from original)
            BreastCancerBMICard()
            
            // Premium Card (reusing from original)
            BreastCancerPremiumCard()
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// Reuse existing cards from HomeScreen.kt with same styling
@Composable
fun AddPartnerCard() {
    Card(
        modifier = Modifier
            .width(356.dp)
            .height(140.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF8254FF))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Add Your Partner",
                    fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Color.White
                )
                Text(
                    text = "Share Your Journey.\nLet Him Walk Beside You",
                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Color.White
                )
                
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .width(100.dp)
                        .height(28.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(
                        "Link Partner",
                        color = Color(0xFF8254FF),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            Image(
                painter = painterResource(id = R.drawable.boy),
                contentDescription = "Partner",
                modifier = Modifier.size(80.dp)
            )
        }
    }
}

@Composable
fun GetReportCard() {
    Card(
        modifier = Modifier
            .width(356.dp)
            .height(100.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7FF)),
        border = BorderStroke(1.dp, Color(0xFFE0E0FF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Get Report",
                fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Text(
                text = "Understand Your Symptoms, Take Charge of Your Health",
                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = Color.Gray
            )
            
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .width(120.dp)
                    .height(28.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(),
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xFFE0BBFF), Color(0xFF9092FF))
                            )
                        )
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Get Full Report", color = Color.White, fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
fun CuratedForYouSection() {
    Column(
        modifier = Modifier.width(356.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Curated For You",
            fontFamily = FontFamily(Font(R.font.poppins_semibold)),
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = Color.Black
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.breast2),
                        contentDescription = "Breast Health",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f))
                    )
                    Text(
                        text = "Breast Health Tips",
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp)
                    )
                }
            }
            
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.meditation),
                        contentDescription = "Self Care",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f))
                    )
                    Text(
                        text = "Self Care During Assessment",
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BreastCancerBMICard() {
    Card(
        modifier = Modifier
            .width(356.dp)
            .height(80.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7FF)),
        border = BorderStroke(1.dp, Color(0xFFE0E0FF))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "You Are Underweight",
                    fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = "Your BMI is 18.2",
                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .width(80.dp)
                        .height(28.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(),
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFFE0BBFF), Color(0xFF9092FF))
                                )
                            )
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Get Diet Plan", color = Color.White, fontSize = 8.sp)
                    }
                }
                
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .width(80.dp)
                        .height(28.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(),
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFFE0BBFF), Color(0xFF9092FF))
                                )
                            )
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Get Workout Plan", color = Color.White, fontSize = 8.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun BreastCancerPremiumCard() {
    Card(
        modifier = Modifier
            .width(356.dp)
            .height(200.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF8254FF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Want More ?",
                fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Text(
                text = "For ₹99/Month",
                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = Color.White
            )
            
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .width(120.dp)
                    .height(32.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(
                    "Get Premium",
                    color = Color(0xFF8254FF),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BreastCancerHomeScreenPreview() {
    SheSoulTheme {
        BreastCancerHomeScreen()
    }
}
