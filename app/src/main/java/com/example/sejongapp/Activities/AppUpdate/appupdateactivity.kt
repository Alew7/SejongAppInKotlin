package com.example.sejongapp.Activities.AppUpdate

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.sejongapp.utils.LocaleHelper


class appupdateactivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        val proData = intent.getParcelableExtra<ProgramUpdate>("AnnData")
        setContent {
            AppUpdateDesign()
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val lang = LocalData.getSavedLanguage(newBase) ?: "ENG" // store selected lang
        val context = LocaleHelper.setLocale(newBase, lang)
        super.attachBaseContext(context)
    }
}




