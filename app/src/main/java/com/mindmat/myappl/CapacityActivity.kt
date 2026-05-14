package com.mindmat.myappl

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class CapacityActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CapacityScreen()
        }
    }
}