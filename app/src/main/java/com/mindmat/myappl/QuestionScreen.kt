package com.mindmat.myappl

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindmat.myappl.ui.theme.*

@Composable
fun QuestionScreen() {

    var selectedOption by remember { mutableStateOf("") }

    val options = listOf(
        "I only",
        "II only",
        "Both I and II",
        "Neither I nor II"
    )

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
            .padding(18.dp)
    ) {

        Text(
            text = "Consider the following statements:",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            shape = RoundedCornerShape(20.dp),

            colors = CardDefaults.cardColors(
                containerColor = White
            ),

            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(18.dp)
            ) {

                Text(
                    text = "I. The Constitution of India explicitly mentions that in certain spheres the Governor acts in his/her own discretion.",
                    fontSize = 17.sp,
                    color = TextDark
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "II. The President of India can reserve a bill passed by State Legislature without it being forwarded by the Governor.",
                    fontSize = 17.sp,
                    color = TextDark
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        options.forEach { option ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        selectedOption = option
                    },

                shape = RoundedCornerShape(18.dp),

                colors = CardDefaults.cardColors(
                    containerColor =
                        if (selectedOption == option)
                            PurplePrimary.copy(alpha = 0.15f)
                        else
                            White
                ),

                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),

                    verticalAlignment = Alignment.CenterVertically
                ) {

                    RadioButton(
                        selected = selectedOption == option,
                        onClick = {
                            selectedOption = option
                        }
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = option,
                        fontSize = 18.sp,
                        color = TextDark,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}