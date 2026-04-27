package com.example.sejongapp.Activities.GradeBookActivity.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sejongapp.Activities.AnnousmentActivity.ui.theme.primaryColor
import com.example.sejongapp.models.DataClasses.StudentGroups.Student
import com.example.sejongapp.R

@Composable
fun StudentAttendanceItem(
    isSaved: Boolean,
    student: Student,
    currentStatus: String,
    allSkips: Int,
    selectedData: String,
    onClickListener: () -> Unit,
    onStatusChange: (String) -> Unit
) {



    val context = LocalContext.current

    val statusTranslation = mapOf(
        "present" to context.getString(R.string.present),
        "late" to context.getString(R.string.late),
        "absent" to context.getString(R.string.absent)
    )
    var showMenu by remember { mutableStateOf(false) }
    val isChecked = currentStatus != "absent"

    // Математика здоровья


    val healthFactor = remember(selectedData, currentStatus, allSkips) {
        val displaySkips = if (currentStatus == "absent" && !isSaved) allSkips + 1 else allSkips
        ((100f - (displaySkips * 7.7f).coerceIn(0f, 100f)) / 100f)
    }
    val animatedHealth by animateFloatAsState(targetValue = healthFactor, animationSpec = tween(800))

    val targetColor = when {
        currentStatus == "late" -> Color(0xFFFFF9C4)
        currentStatus == "absent" -> if (healthFactor > 0.7f)
            Color(0xFFC8E6C9)
        else if (healthFactor > 0.3f) Color(0xFFFFF9C4)
        else Color(0xFFFFCDD2)
        else -> Color(0xFFE8F5E9)
    }

    val animatedBackground by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(600)
    )

    val statusTheme = when (currentStatus) {
        "absent" -> Color.Red to Icons.Default.Close
        "late" -> Color(0xFFFFA500) to Icons.Default.Timer
        else -> Color(0xFF4CAF50) to Icons.Default.Check
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .drawBehind {
                drawRect(
                    color = animatedBackground,
                    size = size.copy(width = size.width * animatedHealth)
                )
            }
            .clickable { onClickListener() }
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Аватарка
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

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = student.student_name_en,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
                Text(
                    text = statusTranslation[currentStatus] ?: currentStatus
                        .replaceFirstChar { it.uppercase() },
                    fontSize = 12.sp,
                    color = statusTheme.first
                )
            }

            // SWITCH КАК МЕНЮ
            Box {
                Switch(
                    modifier = Modifier
                        .scale(0.85f)
                        .clickable { showMenu = true }, // Клик открывает меню
                    checked = isChecked,
                    onCheckedChange = { showMenu = true }, // Игнорируем прямое переключение, открываем список
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


                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.background(Color.White).width(150.dp)
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                context.getString(R.string.present),
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Check,
                                null,
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier
                                    .size(18.dp)
                            )
                        },
                        onClick = {
                            onStatusChange("present");
                            showMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                context.getString(R.string.late),
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Timer,
                                null,
                                tint = Color(0xFFFFA500),
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        onClick = {
                            onStatusChange("late")
                            showMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                context.getString(R.string.absent),
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Close,
                                null,
                                tint = Color.Red,
                                modifier = Modifier
                                    .size(18.dp)
                            )
                        },
                        onClick = {
                            onStatusChange("absent")
                            showMenu = false
                        }
                    )
                }
            }
        }
    }
}