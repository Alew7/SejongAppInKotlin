package com.example.sejongapp.models.DataClasses.StudentGroups

data class groupAttendanceData(
    val date: String,
    val group_id: Int,
    val group_name: String,
    val status: String,
    val student_id: String
)