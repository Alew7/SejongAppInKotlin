package com.example.sejongapp.models.DataClasses.apiResponse

data class StudentAttendanceRequest(
    val student_id: Int,
    val group_id: Int,
    val date: String,
    val status: String,
    val group_name: String
)
