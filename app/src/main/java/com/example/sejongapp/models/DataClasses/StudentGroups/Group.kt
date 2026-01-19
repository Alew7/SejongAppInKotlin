package com.example.sejongapp.models.DataClasses.StudentGroups

data class Group(
    val id: Int,
    val name: String,
    val teacher_id: Int,
    val teacher_name_kr: String,
    val schedule: String,
    val time: String
)