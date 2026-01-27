package com.example.sejongapp.Activities.GradeBookActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge


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
}
