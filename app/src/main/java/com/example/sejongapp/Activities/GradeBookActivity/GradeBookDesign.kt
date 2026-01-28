package com.example.sejongapp.Activities.GradeBookActivity

import android.annotation.SuppressLint
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
    val viewModel: GroupDetailsViewModel = viewModel ( key = "MagazineViewModel_$groupId" )

    val TheRecievedData by viewModel.data.collectAsStateWithLifecycle()

    val realStudents by viewModel.students.collectAsStateWithLifecycle()
    val availlableDates by viewModel.availableDates.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedData.collectAsStateWithLifecycle()

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
            if (!studentsData.containsKey(student.id)) {
                studentsData[student.id] = "Был"
                savedStates[student.id] = false
            }
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(backgroundColor))
    {

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
                        items(items.data.group_students) { student ->
                            StudentAttendanceItem(
                                student = student,
                                currentStatus = studentsData[student.id] ?: "Был",
                                isSaved = savedStates[student.id] ?: false,
                                onStatusChange = { newStatus ->
                                    studentsData[student.id] = newStatus
                                    savedStates[student.id] = false
                                }
                            )
                        }
                    }

                    Button(
                        onClick = {
                            realStudents.forEach { savedStates[it.id] = true } },
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

        // Функция календаря
        if (isSheetOpen) {
            LessonDateBottomSheet(
                sheetState = sheetState,
                availableDates = availlableDates,
                selectedDate = selectedDate,
                onDismiss = { isSheetOpen = false },
                onDateConfirm = {
                    viewModel.updateSelectedDate(selectedDate)
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
