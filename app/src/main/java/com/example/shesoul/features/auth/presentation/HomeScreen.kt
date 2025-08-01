package com.example.shesoul.features.auth.presentation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shesoul.R
import com.example.shesoul.ui.theme.PlayfairDisplayFamily
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily

@Composable
fun PeriodCycleCard() {
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
                                painter = painterResource(id = R.drawable.watedrop),
                                contentDescription = "Period",
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("For Next Period", fontFamily = FontFamily(Font(R.font.poppins_medium)), fontWeight = FontWeight.Medium, fontSize = 16.sp)
                            Text(
                                "25",
                                fontSize = 56.sp, // Adjusted for better fit
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
                    Text("Edit Cycle", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun FertilityCard() {
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
                    text = "Fertility",
                    fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Low",
                    fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 36.sp,
                    color = Color(0xFF8254FF)
                )
                Text(
                    text = "11 Days Left for next Ovulation Cycle",
                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Color.Black
                )
            }
            
            Image(
                painter = painterResource(id = R.drawable.circle),
                contentDescription = "Fertility Chart",
                modifier = Modifier.size(80.dp)
            )
        }
    }
}

@Composable
fun PartnerCard() {
    Box(
        modifier = Modifier
            .width(356.dp)
            .height(220.dp)
    ) {
        // Main card with purple background
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .align(Alignment.BottomCenter)
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
                        fontSize = 24.sp,
                        color = Color.White,
                        modifier = Modifier.width(208.dp)
                    )
                    Text(
                        text = "Share Your Journey.\nLet Him Walk Beside You",
                        fontFamily = FontFamily(Font(R.font.poppins_regular)),
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier.width(212.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Button(
                        onClick = { /* TODO */ },
                        modifier = Modifier
                            .width(120.dp)
                            .height(40.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Link Partner",
                            fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = Color(0xFF8254FF)
                        )
                    }
                }
            }
        }
        
        // Boy figure positioned to extend beyond card boundaries for 3D effect
        Image(
            painter = painterResource(id = R.drawable.boy),
            contentDescription = "Partner Boy",
            modifier = Modifier
                .width(145.dp)
                .height(224.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 5.dp, y = 0.dp)
        )
    }
}

@Composable
fun PCOSAssessmentCard() {
    Card(
        modifier = Modifier
            .width(356.dp)
            .height(135.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7FF)),
        border = BorderStroke(1.dp, Color(0xFFE0E0FF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "PCOS Self-Assessment",
                    fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Understand Your Symptoms. Take Charge of Your Health.",
                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Color.Black.copy(alpha = 0.7f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
            
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .width(170.dp)
                    .height(65.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFFBBBDFF), Color(0xFF9092FF))
                            )
                        )
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Start Assessment",
                        fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Color(0xFFFFFFFF)
                    )
                }
            }
        }
    }
}

@Composable
fun BMICard() {
    Card(
        modifier = Modifier
            .width(356.dp)
            .height(132.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7FF)),
        border = BorderStroke(1.dp, Color(0xFFE0E0FF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "You Are ",
                        fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp,
                        color = Color(0xFF000000)
                    )
                    Text(
                        text = "Underweight",
                        fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp,
                        color = Color(0xFF8254FF)
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Your BMI is 18.2",
                    fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color(0xFFBBBDFF), Color(0xFF9092FF))
                                )
                            )
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Get Diet Plan",
                            fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                }
                
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFF9092FF))
                ) {
                    Text(
                        text = "Calculate BMI",
                        fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = Color(0xFF8254FF)
                    )
                }
            }
        }
    }
}

@Composable
fun PremiumCard() {
    Card(
        modifier = Modifier
            .width(356.dp)
            .height(389.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF8254FF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Want More?",
                    fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                    color = Color(0xFFFFFFFF)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // List of premium features
                PremiumFeatureItem("Personalized Health Insights")
                PremiumFeatureItem("Advanced Symptom Tracking")
                PremiumFeatureItem("AI-Powered Diet Plans")
                PremiumFeatureItem("Priority Support")
                PremiumFeatureItem("Ad-Free Experience")
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "For 399/month",
                    fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = Color(0xFFFFFFFF)
                )
            }
            
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(
                    text = "Get Premium",
                    fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color(0xFF8254FF)
                )
            }
        }
    }
}

@Composable
private fun PremiumFeatureItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(Color(0xFF8254FF), CircleShape)
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Text(
            text = text,
            fontFamily = FontFamily(Font(R.font.poppins_medium)),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = Color.White
        )
    }
}

// Data class for bottom nav items
private data class BottomNavItem(
    val title: String,
    val route: String,
    val lightIcon: Int,
    val darkIcon: Int
)

@Composable
fun HomeScreen() {
    var selectedScreen by remember { mutableStateOf("Home") }

    Scaffold(
        topBar = {
            HomeTopAppBar(userName = "Angel")
        },
        bottomBar = {
            HomeBottomNavigationBar(
                onScreenSelected = { route ->
                    selectedScreen = route
                }
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (selectedScreen == "Home") {
                PeriodCycleCard()
                Spacer(modifier = Modifier.height(6.dp))
                FertilityCard()
                Spacer(modifier = Modifier.height(12.dp))
                PartnerCard()
                Spacer(modifier = Modifier.height(12.dp))
                PCOSAssessmentCard()
                Spacer(modifier = Modifier.height(12.dp))
                BMICard()
                Spacer(modifier = Modifier.height(12.dp))
                PremiumCard()
            } else {
                Text(
                    text = "Content for $selectedScreen",
                    fontSize = 24.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopAppBar(userName: String) {
    TopAppBar(
        title = {
            Text(
                text = "Hi, $userName",
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                fontFamily = PlayfairDisplayFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp,
                color = Color(0xFF9092FF)
            )
        },
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background), // Replace with actual image
                contentDescription = "User Profile Picture",
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(40.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape),
                contentScale = ContentScale.Crop
            )
        },
        actions = {
            Image(
                painter = painterResource(id = R.drawable.journal),
                contentDescription = "Journal",
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(30.dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}

@Composable
private fun HomeBottomNavigationBar(onScreenSelected: (String) -> Unit) {
    val items = listOf(
        BottomNavItem("Home", "home", R.drawable.homelight, R.drawable.homedark),
        BottomNavItem("Journal", "journal", R.drawable.pagelight, R.drawable.pagedark),
        BottomNavItem("Chat", "chat", R.drawable.communicationlight, R.drawable.communicationdark),
        BottomNavItem("Profile", "profile", R.drawable.meditationlight, R.drawable.meditation)
    )
    var selectedRoute by remember { mutableStateOf("home") }
    val selectedIndex by remember(selectedRoute) { mutableStateOf(items.indexOfFirst { it.route == selectedRoute }) }

    BoxWithConstraints {
        val itemWidthDp = maxWidth / items.size
        val targetDipPositionDp = itemWidthDp * (selectedIndex + 0.5f)
        val animatedDipPositionDp by animateDpAsState(targetDipPositionDp)

        val bottomBarGradient = Brush.verticalGradient(
            colors = listOf(Color(0xFFBBBDFF), Color(0xFF9092FF))
        )

        Box {
            BottomAppBar(
                containerColor = Color.Transparent,
                modifier = Modifier
                    .background(bottomBarGradient)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items.forEach { item ->
                        BottomNavigationItem(
                            item = item,
                            isSelected = selectedRoute == item.route,
                            onClick = {
                                selectedRoute = item.route
                                onScreenSelected(item.title)
                            }
                        )
                    }
                }
            }

            if (selectedIndex >= 0) {
                val selectionGradient = Brush.horizontalGradient(
                    colors = listOf(Color(0xFFE0BBFF), Color(0xFF9092FF))
                )

                // White outer circle creating the "dip" illusion
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .offset(
                            x = animatedDipPositionDp - 35.dp,
                            y = (-35).dp
                        )
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    // Inner gradient circle
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(selectionGradient, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = items[selectedIndex].lightIcon),
                            contentDescription = items[selectedIndex].title,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomNavigationItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (!isSelected) {
            Image(
                painter = painterResource(id = item.darkIcon),
                contentDescription = item.title,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}