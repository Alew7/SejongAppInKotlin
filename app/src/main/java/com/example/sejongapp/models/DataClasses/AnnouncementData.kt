package com.example.sejongapp.models.DataClasses

data class AnnouncementData(
    val author: String,
    val content: String,
    val custom_id: Int,
    val images: List<String>,
    val is_active: Boolean,
    val time_posted: String,
    val title: String
)