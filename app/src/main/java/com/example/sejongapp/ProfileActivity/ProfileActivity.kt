package com.example.sejongapp.ProfileActivity

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sejongapp.ProfileActivity.ui.theme.SejongAppTheme
import com.example.sejongapp.utils.LocaleHelper

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProfilePage()
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val lang = LocalData.getSavedLanguage(newBase) ?: "ENG" // store selected lang
        val context = LocaleHelper.setLocale(newBase, lang)
        super.attachBaseContext(context)
    }
}

