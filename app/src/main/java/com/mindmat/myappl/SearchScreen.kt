package com.mindmat.myappl

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

// ✅ ADDED IMPORTS
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext

data class Product(
    val name: String = "",
    val category: String = "",
    val price: String = "",
    val district: String = "",
    val state: String = "",
    val phone: String = ""
)

@Composable
fun SearchScreen() {

    val db = FirebaseFirestore.getInstance()

    var productList by remember { mutableStateOf(listOf<Product>()) }
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedDistrict by remember { mutableStateOf("") }
    var selectedState by remember { mutableStateOf("") }

    // 🔥 Fetch data from Firestore
    LaunchedEffect(Unit) {
        db.collection("products").get()
            .addOnSuccessListener { result ->
                val list = result.map {
                    Product(
                        name = it.getString("name") ?: "",
                        category = it.getString("category") ?: "",
                        price = it.getString("price") ?: "",
                        district = it.getString("district") ?: "",
                        state = it.getString("state") ?: "",
                        phone = it.getString("phone") ?: ""
                    )
                }
                productList = list
            }
    }

    Column(modifier = Modifier.padding(16.dp)) {

        // 🔍 Search Bar
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search product") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 🧷 Category Chips
        val categories = listOf("All", "Food", "Pottery", "Textiles", "Electronics")

        Row {
            categories.forEach { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    label = { Text(category) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 📍 Location Filters
        OutlinedTextField(
            value = selectedDistrict,
            onValueChange = { selectedDistrict = it },
            label = { Text("District") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = selectedState,
            onValueChange = { selectedState = it },
            label = { Text("State") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 🔎 Filter Logic
        val filteredList = productList.filter {

            val matchSearch = it.name.contains(searchText, true)

            val matchCategory =
                selectedCategory == "All" || it.category == selectedCategory

            val matchDistrict =
                selectedDistrict.isEmpty() || it.district.contains(selectedDistrict, true)

            val matchState =
                selectedState.isEmpty() || it.state.contains(selectedState, true)

            matchSearch && matchCategory && matchDistrict && matchState
        }

        // 📦 Product List
        LazyColumn {
            items(filteredList) { product ->

                // ✅ ADDED CONTEXT
                val context = LocalContext.current

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {

                        Text("Name: ${product.name}")
                        Text("Category: ${product.category}")
                        Text("Price: ₹${product.price}")

                        Text(
                            text = if(product.district.isNotEmpty() && product.state.isNotEmpty())
                                "Location: ${product.district}, ${product.state}"
                            else
                                "Location not available"
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // ✅ ADDED CALL BUTTON
                        Button(
                            onClick = {
                                if (product.phone.isNotEmpty()) {

                                    val intent = Intent(Intent.ACTION_DIAL).apply {
                                        data = Uri.parse("tel:${product.phone}")
                                    }

                                    context.startActivity(intent)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("📞 Call to Place Order")
                        }
                    }
                }
            }
        }
    }
}