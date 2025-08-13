package com.example.sejongapp.models.DataClasses

data class ScheduleTime(
    val classroom: Int,
    val day: Int,
    val end_time: String,
    val start_time: String
)