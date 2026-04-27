package com.example.sejongapp.models.DataClasses.StudentGroups

data class Group(
    val id: Int,
    val name: String,
    val teacher_id: Int,
    val teacher_name_kr: String,
    val teacher_name_tj: String,
    val teacher_name_en: String,
    val schedule: String,
    val group_type: String,
    val time: String
)