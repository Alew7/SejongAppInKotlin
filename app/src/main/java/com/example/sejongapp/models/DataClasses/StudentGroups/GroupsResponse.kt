package com.example.sejongapp.models.DataClasses.StudentGroups


data class GroupDetailResponse(
    val message: String,
    val data: GroupDataWrapper
)

data class TeachersGroupResponse(
    val message: String,
    val data: List<Group>
)



data class GroupDataWrapper(
    val group_students: List<Student>,
    val group_data: Group,
    val group_schedule: GroupSchedule,
    val group_attendance: List<groupAttendanceData>

)

data class Student(
    val id: Int,
    val student_name_en : String,
    val student_name_tj: String,
    val student_name_kr: String,
    val student_id: Int
)

data class GroupSchedule(
    val id: Int,
    val name: String,
    val days: Map<String, Map<String, List<Int>>>
)