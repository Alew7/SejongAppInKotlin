package com.example.sejongapp.Activities.SpleshLoginPages

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.sejongapp.utils.LocaleHelper
import kotlinx.coroutines.delay

class SplashLoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SejongApp()
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val lang = LocalData.getSavedLanguage(newBase) ?: "ENG" // store selected lang
        val context = LocaleHelper.setLocale(newBase, lang)
        super.attachBaseContext(context)
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




