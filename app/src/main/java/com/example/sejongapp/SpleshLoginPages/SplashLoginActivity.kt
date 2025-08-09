package com.example.sejongapp.SpleshLoginPages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import kotlinx.coroutines.delay

class SplashLoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SejongApp()
        }
    }
}

@Composable
private fun SejongApp () {
    var showSplesh by remember { mutableStateOf(true) }

    LaunchedEffect  (Unit){
        delay(1500)
        showSplesh = false
    }

    if (showSplesh) {
        SpleshScreen()
    }
    else {
        LoginScreen()
    }
}




