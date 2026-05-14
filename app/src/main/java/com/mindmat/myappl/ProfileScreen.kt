package com.mindmat.myappl

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProfileScreen()
        }
    }
}

@Composable
fun ProfileScreen() {

    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser

    var ownerName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf(user?.phoneNumber ?: "") }
    var skill by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text("Create Profile", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(ownerName, { ownerName = it }, label = { Text("Owner Name") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(phone, { phone = it }, label = { Text("Contact Number") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(skill, { skill = it }, label = { Text("Skill Area") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(district, { district = it }, label = { Text("District") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(state, { state = it }, label = { Text("State") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                // ✅ VALIDATION (important improvement)
                if (ownerName.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(context, "Fill required fields", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val data = hashMapOf(
                    "ownerName" to ownerName,
                    "phone" to phone,
                    "skill" to skill,
                    "district" to district,
                    "state" to state
                )

                user?.let {
                    db.collection("profiles")
                        .document(it.uid)
                        .set(data)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Profile Saved", Toast.LENGTH_SHORT).show()

                            // ✅ CLEAR FORM (nice UX)
                            ownerName = ""
                            skill = ""
                            district = ""
                            state = ""
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Profile")
        }
    }
}