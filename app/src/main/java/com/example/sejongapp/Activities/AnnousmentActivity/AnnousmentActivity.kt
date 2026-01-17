package com.example.sejongapp.Activities.AnnousmentActivity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.sejongapp.models.DataClasses.AnnouncementDateItem

const val TAG = "AnnousmentActivity_TAG"

class AnnousmentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.i(TAG, "onCreate: Got the intent");
        val annData = intent.getParcelableExtra<AnnouncementDateItem>("AnnData")

        Log.i(TAG, "onCreate: Got the intent with data $annData");

        setContent {
            AnnousmentDetailPage(annData!!)
        }
    }
}
