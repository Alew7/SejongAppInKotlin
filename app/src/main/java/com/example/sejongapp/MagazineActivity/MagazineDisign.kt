package com.example.sejongapp.MagazineActivity

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sejongapp.ProfileActivity.ui.theme.backgroundColor
import com.example.sejongapp.ProfileActivity.ui.theme.primaryColor
import com.example.sejongapp.models.DataClass2.Student

import com.example.sejongapp.models.ViewModels2.MagazineViewModel





@Composable
fun MagazineDesign(
    groupId: Int,
    viewModel: MagazineViewModel = viewModel()
) {

    val realStudents by viewModel.students.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()


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

    Box(modifier = Modifier.fillMaxSize().background(backgroundColor)) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = primaryColor)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Журнал посещаемости", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(20.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    InfoSelectionCard(icon = Icons.Default.DateRange, text = "10 Января 2026")
                    InfoSelectionCard(icon = Icons.Default.PeopleAlt, text = "Группа ID: $groupId")
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
                        realStudents.forEach { student ->
                            savedStates[student.id] = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp).padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(text = "Сохранить отчёт", color = Color.White, fontSize = 16.sp)
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
        currentStatus == "Опоздал" -> if (isSaved) Color(0xFFFFF9C4) else Color(0xFFE8F5E9)
        healthFactor > 0.7f -> if (currentStatus == "Не был" && isSaved) Color(0xFFC8E6C9) else Color(0xFFE8F5E9)
        else -> if (currentStatus == "Не был") Color(0xFFEF5350) else Color(0xFFE8F5E9)
    }

    val animatedBackground by animateColorAsState(targetValue = targetColor, animationSpec = tween(600))

    val statusTheme = when (currentStatus) {
        "Не был" -> Color.Red to Icons.Default.Close
        "Опоздал" -> Color(0xFFFFA500) to Icons.Default.Timer
        else -> primaryColor to Icons.Default.Check
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
            modifier = Modifier.fillMaxWidth().padding(12.dp),
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
                    text = student.student_name_tj.take(1).uppercase(),
                    color = primaryColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = student.student_name_tj, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                if (!isExpanded) {
                    Text(text = currentStatus, fontSize = 12.sp, color = statusTheme.first)
                }
            }

            Box(
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { isExpanded = !isExpanded }
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
                    Icon(icon, contentDescription = null, tint = itemColor, modifier = Modifier.size(20.dp))
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
fun RowScope.InfoSelectionCard(icon: ImageVector, text: String) {
    Card(
        modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .clickable(interactionSource = MutableInteractionSource(), indication = null) { },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = primaryColor)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text, fontSize = 14.sp, color = Color.Black)
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

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun MagazineDesignPreview() {
//    MagazineDisign()
//}