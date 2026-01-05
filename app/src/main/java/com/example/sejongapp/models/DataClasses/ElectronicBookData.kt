package com.example.sejongapp.models.DataClasses

data class ElectronicBookData(
    val author: String,
    val cover: String,
    val created_at: String,
    val description: Content,
    val file: String,
    val genres: String,
    val published_date: String,
    val title: Title
)

