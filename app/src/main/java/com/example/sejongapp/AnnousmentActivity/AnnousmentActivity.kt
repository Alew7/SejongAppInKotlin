package com.example.sejongapp.AnnousmentActivity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.sejongapp.SpleshLoginPages.LoginScreen
import com.example.sejongapp.models.DataClasses.AnnouncementData

const val TAG = "AnnousmentActivity_TAG"

class AnnousmentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.i(TAG, "onCreate: Got the intent");
        val annData = intent.getParcelableExtra<AnnouncementData>("AnnData")

        Log.i(TAG, "onCreate: Got the intent with data $annData");

        setContent {
            AnnousmentDetailPage(annData!!)
        }
    }
}
