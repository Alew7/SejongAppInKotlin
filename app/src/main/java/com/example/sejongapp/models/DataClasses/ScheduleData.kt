package com.example.sejongapp.models.DataClasses

data class ScheduleData(
    val book: Int,
    val group: String,
    val teacher: String,
    val time: List<ScheduleTime>
)


