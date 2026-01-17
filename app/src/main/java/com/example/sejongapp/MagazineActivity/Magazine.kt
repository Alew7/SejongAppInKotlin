package com.example.sejongapp.MagazineActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge


class Magazine : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val groupId = intent.getIntExtra("GROUP_ID", -1)
        val groupName = intent.getStringExtra("GROUP_NAME") ?: "Неизвестный"
        enableEdgeToEdge()
        setContent {
            MagazineDesign(groupId = groupId, groupName = groupName)
        }
    }
}
