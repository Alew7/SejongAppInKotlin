package com.example.sejongapp.Activities.AppUpdate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge


class appupdateactivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        val proData = intent.getParcelableExtra<ProgramUpdate>("AnnData")
        setContent {
            AppUpdateDesign()
        }
    }
}



