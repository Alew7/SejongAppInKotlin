package com.example.sejongapp.Activities.GradeBookActivity

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sejongapp.Activities.AnnousmentActivity.ui.theme.backgroundColor
import com.example.sejongapp.Activities.AnnousmentActivity.ui.theme.primaryColor
import com.example.sejongapp.Activities.GradeBookActivity.components.InfoSelectionCard
import com.example.sejongapp.Activities.GradeBookActivity.components.LessonDateBottomSheet
import com.example.sejongapp.Activities.GradeBookActivity.components.StudentAttendanceItem

import com.example.sejongapp.R
import com.example.sejongapp.models.DataClasses.StudentGroups.GroupDetailResponse
import com.example.sejongapp.models.DataClasses.StudentGroups.Student
import com.example.sejongapp.models.DataClasses.apiResponse.StudentAttendanceRequest
import com.example.sejongapp.models.ViewModels.GradeBookViewModels.GroupDetailsViewModel
import com.example.sejongapp.retrofitAPI.NetworkResponse
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailPage(
    groupId: Int,
    groupName: String

) {

//    val studentAttendanceHashMap =  HashMap<Int, StudentAttendanceRequest>()
    val studentAttendanceHashMap = remember { mutableStateMapOf<Int,StudentAttendanceRequest>() }
    val viewModel: GroupDetailsViewModel = viewModel ( key = "MagazineViewModel_$groupId" )

    val TheRecievedData by viewModel.data.collectAsStateWithLifecycle()

    val realStudents by viewModel.students.collectAsStateWithLifecycle()
    val availlableDates by viewModel.availableDates.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedData.collectAsStateWithLifecycle()
    val attendanceRequest by viewModel.groupAttendanceRequest.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by remember { mutableStateOf(false) }
    var tempSelectedDate by remember { mutableStateOf("") }
    val context = LocalContext.current



    var triggerOpeningAnimatin by remember { mutableStateOf(false)}

    LaunchedEffect (Unit){
        kotlinx.coroutines.delay(300)
        triggerOpeningAnimatin = true

    }



    LaunchedEffect(isSheetOpen) {
        if (isSheetOpen) {
            tempSelectedDate = selectedDate
        }
    }

    LaunchedEffect(groupId) {
        viewModel.loadGroupData(groupId, context)
    }

    val studentsData = remember { mutableStateMapOf<Int, String>() }
    val savedStates = remember { mutableStateMapOf<Int, Boolean>() }


    LaunchedEffect(realStudents) {
        realStudents.forEach { student ->
            if (!studentsData.containsKey(student.student_id)) {
                studentsData[student.student_id] = "present"
                savedStates[student.student_id] = false

                // СРАЗУ кладем дефолтное значение в мапу для отправки
                studentAttendanceHashMap[student.student_id] = StudentAttendanceRequest(
                    student_id = student.student_id,
                    group_id = groupId,
                    date = convertDateToBackendFormat(selectedDate),
                    status = "present",
                    group_name = groupName
                )
            }
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(backgroundColor))
    {

        when(attendanceRequest){
            is NetworkResponse.Error ->{}
            NetworkResponse.Idle -> {
                when(TheRecievedData){
                    is NetworkResponse.Error -> {

                    }
                    NetworkResponse.Idle -> {

                    }
                    NetworkResponse.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center),
                            color = primaryColor
                        )
                    }
                    is NetworkResponse.Success -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        )
                        {

                            Text(
                                text = context.getString(R.string.attendance_gradebook),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp))
                            {
                                Box(
                                    modifier = Modifier
                                        .weight(1f))
                                {
                                    InfoSelectionCard(
                                        icon = Icons.Default.DateRange,
                                        text = if (selectedDate.isEmpty()) "Loading..." else selectedDate,
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = {
                                            isSheetOpen = true
                                        }
                                    )
                                }
                                InfoSelectionCard(
                                    icon = Icons.Default.PeopleAlt,
                                    showArrow = false,
                                    text = groupName,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                val items = (TheRecievedData as NetworkResponse.Success<GroupDetailResponse>).data



                                items(
                                    items.data.group_students,
                                    key = { student -> student.student_id } // Используем student_id
                                ) { student ->
                                    StudentAttendanceItem(
                                        student = student,

                                        currentStatus = studentsData[student.student_id] ?: "present",
                                        isSaved = savedStates[student.student_id] ?: false,
                                        onStatusChange = { newStatus ->

                                            studentsData[student.student_id] = newStatus

                                            // 2. Сразу создаем/обновляем объект для отправки в бэкенд
                                            val modifiedStudentData = StudentAttendanceRequest(
                                                student_id = student.student_id, // Используем student_id, чтобы не было 0
                                                group_id = groupId,
                                                date = convertDateToBackendFormat(selectedDate),
                                                status = newStatus,
                                                group_name = groupName
                                            )
                                            studentAttendanceHashMap[student.student_id] = modifiedStudentData

                                            // 3. Сбрасываем флаг "сохранено", так как статус изменился
                                            savedStates[student.student_id] = false
                                        }
                                    )
                                }
                            }


////                    submitting the student attendance
                            Button(
                                onClick = {
                                    // Берем ВСЕХ студентов из загруженных данных
                                    val successData = (TheRecievedData as? NetworkResponse.Success<GroupDetailResponse>)?.data
                                    val allStudents = successData?.data?.group_students ?: emptyList()

                                    // Формируем список: если мы меняли статус — он в мапе, если нет — берем статус из studentsData
                                    val finalAttendanceList = allStudents.map { student ->
                                        studentAttendanceHashMap[student.student_id] ?: StudentAttendanceRequest(
                                            student_id = student.student_id,
                                            group_id = groupId,
                                            date = convertDateToBackendFormat(selectedDate),
                                            status = studentsData[student.student_id] ?: "present",
                                            group_name = groupName
                                        )
                                    }

                                    if (finalAttendanceList.isNotEmpty()) {
                                        viewModel.saveGroupAttendance(context, finalAttendanceList)

                                        allStudents.forEach { student ->
                                            savedStates[student.student_id] = true
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .padding(top = 16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Text(
                                    text = context.getString(R.string.Save_Report),
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
            NetworkResponse.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center),
                    color = primaryColor
                )
                Toast.makeText(context, "Loading the data", Toast.LENGTH_SHORT).show()
            }
            is NetworkResponse.Success<*> -> {
                Toast.makeText(context, "Attendance saved", Toast.LENGTH_LONG).show()
                viewModel.resetGroupAttendance()

            }
        }



        // Функция календаря
        // Внутри GroupDetailPage.kt
        // В GroupDetailPage.kt
        if (isSheetOpen) {
            LessonDateBottomSheet(
                sheetState = sheetState,
                availableDates = availlableDates,
                selectedDate = selectedDate,
                onDismiss = { isSheetOpen = false },
                onDateConfirm = { newChosenDate -> // Добавь этот параметр
//                    viewModel.updateSelectedDate(newChosenDate) // Передай его сюда
                    isSheetOpen = false
                }
            )
        }

    }
}




fun isDateInFuture(dateStr: String): Boolean {
    return try {
        val sdf = SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH)
        val date = sdf.parse(dateStr)
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }.time
        date?.after(today) ?: false
    } catch (e: Exception) { false }
}

fun getFirstDayOffset(month: String, year: String): Int {
    return try {
        val sdf = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
        val date = sdf.parse("$month $year")
        val calendar = Calendar.getInstance()
        if (date != null) calendar.time = date
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        if (dayOfWeek == Calendar.SUNDAY) 6 else dayOfWeek - 2
    } catch (e: Exception) { 0 }
}

fun getDaysInMonth(month: String, year: String): Int {
    return try {
        val sdf = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
        val date = sdf.parse("$month $year")
        val calendar = Calendar.getInstance()
        if (date != null) {
            calendar.time = date
            calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        } else 31
    } catch (e: Exception) { 31 }
}
fun convertDateToBackendFormat(dateStr: String): String {
    return try {
        // Вход: "2 February 2026"
        val inputFormat = SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH)


        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)

        val date = inputFormat.parse(dateStr)
        date?.let { outputFormat.format(it) } ?: dateStr
    } catch (e: Exception) {
        dateStr
    }
}

