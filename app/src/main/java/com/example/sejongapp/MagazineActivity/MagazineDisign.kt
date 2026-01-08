package com.example.sejongapp.MagazineActivity

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sejongapp.ProfileActivity.ui.theme.backgroundColor
import com.example.sejongapp.ProfileActivity.ui.theme.primaryColor
import com.example.sejongapp.models.DataClass2.Student
import com.example.sejongapp.models.ViewModels2.MagazineViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MagazineDesign(
    groupId: Int,

) {
    val viewModel: MagazineViewModel = viewModel ( key = "MagazineViewModel_$groupId" )
    val realStudents by viewModel.students.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val availlableDates by viewModel.availableDates.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedData.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by remember { mutableStateOf(false) }
    var tempSelectedDate by remember { mutableStateOf("") }



    LaunchedEffect(isSheetOpen) {
        if (isSheetOpen) {
            tempSelectedDate = selectedDate
        }
    }

    LaunchedEffect(groupId) {
        viewModel.loadGroupData(groupId)
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
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
               .align(Alignment.Center),
                color = primaryColor
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Журнал посещаемости",
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
                        text = "Группа ID: $groupId",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(realStudents) { student ->
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
                        text = "Сохранить отчёт",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }

        if (isSheetOpen) {
            ModalBottomSheet(
                onDismissRequest = { isSheetOpen = false },
                sheetState = sheetState,
                containerColor = Color.White,
                dragHandle = { BottomSheetDefaults.DragHandle(color = primaryColor) }
            ) {

                val dateParts = tempSelectedDate.split(" ")
                val currentMonth = if (dateParts.size >= 2) dateParts[1] else "January"
                val currentYear = if (dateParts.size >= 3) dateParts[2] else "2026"

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, bottom = 40.dp))
                {
                    Text(
                        text = "Выберите дату урока",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    val daysOfWeek = listOf(
                        "Mon",
                        "Tue",
                        "Wed",
                        "Thu",
                        "Fri",
                        "Sat",
                        "Sun"
                    )

                    Row(modifier = Modifier
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround)
                    {
                        daysOfWeek.forEach { Text(
                            it,
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(7),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val offset = getFirstDayOffset(currentMonth, currentYear)
                        items(offset) { Spacer(modifier = Modifier.fillMaxSize()) }

                        val daysInMonth = getDaysInMonth(currentMonth, currentYear)
                        items(daysInMonth) { index ->
                            val day = index + 1
                            val dateString = "$day $currentMonth $currentYear"
                            val isEnabled = availlableDates.contains(dateString)
                            val isSelected = tempSelectedDate == dateString
                            val isFuture = isDateInFuture(dateString)

                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isSelected -> primaryColor
                                            isEnabled && !isFuture -> primaryColor.copy(alpha = 0.15f)
                                            else -> Color.Transparent
                                        }
                                    )
                                    .clickable(enabled = isEnabled && !isFuture) {
                                        tempSelectedDate = dateString
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isFuture && isEnabled) {
                                    Icon(
                                        Icons.Default.Lock,
                                        null,
                                        modifier = Modifier.size(16.dp),
                                        tint = Color.LightGray
                                    )
                                } else {
                                    Text(
                                        text = day.toString(),
                                        fontSize = 16.sp,
                                        fontWeight = if (isEnabled) FontWeight.Bold else FontWeight.Normal,
                                        color = when {
                                            isSelected -> Color.White
                                            isEnabled && !isFuture -> primaryColor
                                            else -> Color.Gray.copy(alpha = 0.4f)
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    val isFutureSelected = isDateInFuture(tempSelectedDate)
                    Button(
                        onClick = {
                            viewModel.updateSelectedDate(tempSelectedDate)
                            isSheetOpen = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isFutureSelected) Color(0xFFE0E0E0) else primaryColor
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (isFutureSelected) "Недоступно (Будущее)" else "Подтвердить выбор",
                            color = if (isFutureSelected) Color.Gray else Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StudentAttendanceItem(
    isSaved: Boolean,
    student: Student,
    currentStatus: String,
    onStatusChange: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val isChecked = currentStatus != "Не был"


    val totalNB = if (isSaved && currentStatus == "Не был") 1 else 0

    val totalDamage = (totalNB * 14.2f).coerceIn(0f, 100f)
    val healthFactor = (100f - totalDamage) / 100f

    val animatedHealth by animateFloatAsState(
        targetValue = healthFactor,
        animationSpec = tween(800)
    )


    val targetColor = when {
        currentStatus == "Опоздал" -> if (isSaved)
            Color(0xFFFFF9C4)
        else Color(0xFFE8F5E9)
        healthFactor > 0.7f -> if (currentStatus == "Не был" && isSaved)
            Color(0xFFC8E6C9)
        else Color(0xFFE8F5E9)
        else -> if (currentStatus == "Не был")
            Color(0xFFEF5350)
        else Color(0xFFE8F5E9)
    }

    val animatedBackground by
    animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(600)
    )

    val statusTheme = when (currentStatus) {
        "Не был" -> Color.Red to Icons.Default.Close
        "Опоздал" -> Color(0xFFFFA500) to Icons.Default.Timer
        else -> Color(0xFF4CAF50) to Icons.Default.Check
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .drawBehind {
                drawRect(
                    color = animatedBackground,
                    size = size.copy(width = size.width * animatedHealth)
                )
            }
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(primaryColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = student.student_name_en.take(1).uppercase(),
                    color = primaryColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = student.student_name_en,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                if (!isExpanded) {
                    Text(
                        text = currentStatus,
                        fontSize = 12.sp,
                        color = statusTheme.first
                    )
                }
            }

            Box(
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        isExpanded = !isExpanded
                    }
            ) {
                Switch(
                    checked = isChecked,
                    onCheckedChange = null,
                    thumbContent = {
                        Icon(
                            imageVector = statusTheme.second,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                            tint = statusTheme.first
                        )
                    },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = statusTheme.first.copy(alpha = 0.2f),
                        checkedThumbColor = Color.White,
                        checkedBorderColor = statusTheme.first,
                        uncheckedTrackColor = Color.Red.copy(alpha = 0.1f),
                        uncheckedThumbColor = Color.White,
                        uncheckedBorderColor = Color.Red
                    )
                )
            }
        }

        if (isExpanded) {
            val options = listOf("Был", "Опоздал", "Не был")
            options.forEach { statusName ->
                val icon = when(statusName) {
                    "Был" -> Icons.Default.Check
                    "Опоздал" -> Icons.Default.Timer
                    else -> Icons.Default.Close
                }
                val itemColor = when (statusName) {
                    "Не был" -> Color.Red
                    "Опоздал" -> Color(0xFFFFA500)
                    else -> primaryColor
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onStatusChange(statusName)
                            isExpanded = false
                        }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = itemColor,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = statusName,
                        fontSize = 14.sp,
                        color = if (currentStatus == statusName) itemColor else Color.Black,
                        fontWeight = if (currentStatus == statusName) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun InfoSelectionCard(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .height(48.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
                onClick()
              },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = icon,
                    contentDescription = null,
                    tint = primaryColor
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text,
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = primaryColor,
                modifier = Modifier.size(20.dp)
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
