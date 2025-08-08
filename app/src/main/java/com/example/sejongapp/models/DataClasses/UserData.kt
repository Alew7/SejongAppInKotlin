package com.example.sejongapp.models.DataClasses

data class UserData(
    val username: String,
    val avatar: String,
    val fullname: String,
    val email: String,
    val status: String,
    val groups: List<String>
)
