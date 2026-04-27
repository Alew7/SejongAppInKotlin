package com.example.sejongapp.Activities.GradeBookActivity

import LocalData
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.sejongapp.utils.LocaleHelper


class GradeBookActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val groupId = intent.getIntExtra("GROUP_ID", -1)
        val groupName = intent.getStringExtra("GROUP_NAME") ?: "Неизвестный"
        enableEdgeToEdge()
        setContent {
                GroupDetailPage(groupId = groupId, groupName = groupName)
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val lang = LocalData.getSavedLanguage(newBase) ?: "ENG"
        val context = LocaleHelper.setLocale(newBase,lang)
        super.attachBaseContext(context)
    }
}
