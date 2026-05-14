package com.mindmat.myappl

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.mindmat.myappl.ui.theme.*
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LoginScreen()
        }
    }
}

@Composable
fun LoginScreen() {

    val context = LocalContext.current
    val activity = context as Activity
    val auth = FirebaseAuth.getInstance()

    var phone by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf<String?>(null) }

    val callbacks = remember {

        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(
                credential: PhoneAuthCredential
            ) {

                auth.signInWithCredential(credential)
                    .addOnSuccessListener {

                        Toast.makeText(
                            context,
                            "Login Success",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent =
                            Intent(activity, HomeActivity::class.java)

                        activity.startActivity(intent)
                        activity.finish()
                    }
            }

            override fun onVerificationFailed(
                e: FirebaseException
            ) {

                Toast.makeText(
                    context,
                    "Error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onCodeSent(
                verId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                verificationId = verId

                Toast.makeText(
                    context,
                    "OTP Sent",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF8F5FF),
                        Color(0xFFEDE7F6)
                    )
                )
            )
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(60.dp))

        Card(
            shape = RoundedCornerShape(24.dp),

            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),

            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(24.dp)
            ) {

                Text(
                    text = "📱 OTP Verification",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = PurplePrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Verify your mobile number",
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(28.dp))

                // PHONE FIELD
                OutlinedTextField(
                    value = phone,

                    onValueChange = {
                        phone = it
                    },

                    modifier = Modifier.fillMaxWidth(),

                    label = {
                        Text("Phone Number")
                    },

                    leadingIcon = {
                        Text("🇮🇳")
                    },

                    shape = RoundedCornerShape(18.dp),

                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PurplePrimary,
                        unfocusedBorderColor = Color.LightGray,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // SEND OTP BUTTON
                Button(

                    onClick = {

                        val options =
                            PhoneAuthOptions.newBuilder(auth)

                                .setPhoneNumber("+91$phone")

                                .setTimeout(
                                    60L,
                                    TimeUnit.SECONDS
                                )

                                .setActivity(activity)

                                .setCallbacks(callbacks)

                                .build()

                        PhoneAuthProvider.verifyPhoneNumber(
                            options
                        )
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),

                    shape = RoundedCornerShape(18.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = PurplePrimary
                    )
                ) {

                    Text(
                        "Send OTP",
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // OTP FIELD
                OutlinedTextField(
                    value = otp,

                    onValueChange = {
                        otp = it
                    },

                    modifier = Modifier.fillMaxWidth(),

                    label = {
                        Text("Enter OTP")
                    },

                    shape = RoundedCornerShape(18.dp),

                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PurplePrimary,
                        unfocusedBorderColor = Color.LightGray,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // VERIFY BUTTON
                Button(

                    onClick = {

                        if (verificationId == null) {

                            Toast.makeText(
                                context,
                                "Send OTP first",
                                Toast.LENGTH_SHORT
                            ).show()

                            return@Button
                        }

                        val credential =
                            PhoneAuthProvider.getCredential(
                                verificationId!!,
                                otp
                            )

                        auth.signInWithCredential(credential)

                            .addOnSuccessListener {

                                Toast.makeText(
                                    context,
                                    "Login Success",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent =
                                    Intent(
                                        activity,
                                        HomeActivity::class.java
                                    )

                                activity.startActivity(intent)

                                activity.finish()
                            }

                            .addOnFailureListener {

                                Toast.makeText(
                                    context,
                                    "Invalid OTP",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),

                    shape = RoundedCornerShape(18.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = PurplePrimary
                    )
                ) {

                    Text(
                        "Verify OTP",
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}