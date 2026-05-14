package com.mindmat.myappl

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun CapacityScreen() {

    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    var isAcceptingOrders by remember { mutableStateOf(false) }
    var capacity by remember { mutableStateOf(0f) }

    // 🔥 Auto status
    val status = if (isAcceptingOrders) {
        "Ready to take orders for ${capacity.toInt()} units this week"
    } else {
        "Not accepting orders"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text("Capacity Meter", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(20.dp))

        // 🔘 Toggle Switch
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Accepting Orders")
            Switch(
                checked = isAcceptingOrders,
                onCheckedChange = { isAcceptingOrders = it }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 🎚️ Slider (SeekBar equivalent)
        Text("Set Weekly Capacity: ${capacity.toInt()} units")

        Slider(
            value = capacity,
            onValueChange = { capacity = it },
            valueRange = 0f..100f
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 📊 Auto Status
        Text(
            text = status,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 💾 Save Button
        Button(
            onClick = {

                val data = hashMapOf(
                    "acceptingOrders" to isAcceptingOrders,
                    "capacity" to capacity.toInt(),
                    "status" to status
                )

                userId?.let {
                    db.collection("capacity")
                        .document(it)
                        .set(data)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Capacity")
        }
    }
}