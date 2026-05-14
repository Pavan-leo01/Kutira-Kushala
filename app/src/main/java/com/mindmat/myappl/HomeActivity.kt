package com.mindmat.myappl

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.mindmat.myappl.ui.theme.*

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HomeScreen()
        }
    }
}

@Composable
fun HomeScreen() {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF6F7FB),
                        Color(0xFFEDE7F6)
                    )
                )
            )
            .padding(20.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "🏠 Kutira-Kushala",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Handmade Product Marketplace",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(40.dp))

        // 🔥 CREATE PROFILE
        HomeButton(
            text = "👤 Create Profile"
        ) {
            context.startActivity(
                Intent(context, ProfileActivity::class.java)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔥 ADD PRODUCT
        HomeButton(
            text = "📦 Add Product"
        ) {
            context.startActivity(
                Intent(context, ProductActivity::class.java)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔥 CAPACITY METER
        HomeButton(
            text = "📊 Capacity Meter"
        ) {
            context.startActivity(
                Intent(context, CapacityActivity::class.java)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔥 SEARCH PRODUCTS
        HomeButton(
            text = "🔍 Search Products"
        ) {
            context.startActivity(
                Intent(context, SearchActivity::class.java)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔥 LOGOUT
        HomeButton(
            text = "🚪 Logout"
        ) {
            FirebaseAuth.getInstance().signOut()
            (context as? ComponentActivity)?.finish()
        }
    }
}

@Composable
fun HomeButton(
    text: String,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,

        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),

        shape = RoundedCornerShape(18.dp),

        colors = ButtonDefaults.buttonColors(
            containerColor = PurplePrimary
        ),

        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp
        )
    ) {

        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = White
        )
    }
}