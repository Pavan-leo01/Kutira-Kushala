package com.mindmat.myappl

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

@Composable
fun ProductScreen() {

    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var loading by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri
    }

    Column(modifier = Modifier.padding(20.dp)) {

        Text("Add Product", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(name, { name = it }, label = { Text("Product Name") })
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(category, { category = it }, label = { Text("Category") })
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(price, { price = it }, label = { Text("Price") })
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(unit, { unit = it }, label = { Text("Unit (kg, piece)") })
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(quantity, { quantity = it }, label = { Text("Min Quantity") })

        Spacer(modifier = Modifier.height(12.dp))

        // 🔹 Select Image (optional)
        Button(onClick = { launcher.launch("image/*") }) {
            Text("Select Image (Optional)")
        }

        imageUri?.let {
            Spacer(modifier = Modifier.height(10.dp))
            AsyncImage(
                model = it,
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        // 🔥 SAVE BUTTON (FIXED)
        Button(
            onClick = {

                // ✅ validation
                if (name.isEmpty() || category.isEmpty() || price.isEmpty()
                    || unit.isEmpty() || quantity.isEmpty()
                ) {
                    Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                loading = true

                // 🔹 function to save product
                fun saveProduct(imageUrl: String) {
                    val product = hashMapOf(
                        "name" to name,
                        "category" to category,
                        "price" to price,
                        "unit" to unit,
                        "quantity" to quantity,
                        "image" to imageUrl,
                        "userId" to userId,
                        "timestamp" to System.currentTimeMillis()
                    )

                    db.collection("products")
                        .add(product)
                        .addOnSuccessListener {
                            loading = false
                            Toast.makeText(context, "Product Added", Toast.LENGTH_SHORT).show()

                            // clear fields
                            name = ""
                            category = ""
                            price = ""
                            unit = ""
                            quantity = ""
                            imageUri = null
                        }
                        .addOnFailureListener {
                            loading = false
                            Toast.makeText(context, "Error saving product", Toast.LENGTH_SHORT).show()
                        }
                }

                // ✅ CASE 1: no image
                if (imageUri == null) {
                    saveProduct("")
                    return@Button
                }

                // ✅ CASE 2: upload image
                val ref = storage.reference.child("products/${UUID.randomUUID()}")

                ref.putFile(imageUri!!)
                    .addOnSuccessListener {
                        ref.downloadUrl.addOnSuccessListener { url ->
                            saveProduct(url.toString())
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Image failed, saving without image", Toast.LENGTH_SHORT).show()
                        saveProduct("")
                    }

            },
            enabled = !loading
        ) {
            Text(if (loading) "Saving..." else "Save Product")
        }
    }
}