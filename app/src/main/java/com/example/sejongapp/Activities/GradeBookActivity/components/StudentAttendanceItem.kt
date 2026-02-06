package com.example.sejongapp.Activities.GradeBookActivity.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sejongapp.Activities.AnnousmentActivity.ui.theme.primaryColor
import com.example.sejongapp.models.DataClasses.StudentGroups.Student



@Composable
fun StudentAttendanceItem(
    isSaved: Boolean,
    student: Student,
    currentStatus: String,
    allSkips: Int,
    onStatusChange: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val isChecked = currentStatus != "absent"


    val displaySkips = if (currentStatus == "absent" && !isSaved) allSkips + 1 else allSkips


    val totalDamage = (displaySkips * 14.2f).coerceIn(0f, 100f)


    val healthFactor = (100f - totalDamage) / 100f

    val animatedHealth by animateFloatAsState(
        targetValue = healthFactor,
        animationSpec = tween(800),
        label = "healthAnimation"
    )


//    val targetColor = when {
//        currentStatus == "late" -> if (isSaved)
//            Color(0xFFFFF9C4)
//        else Color(0xFFE8F5E9)
//        healthFactor > 0.7f -> if (currentStatus == "absent" && isSaved)
//            Color(0xFFC8E6C9)
//        else Color(0xFFE8F5E9)
//        else -> if (currentStatus == "absent")
//            Color(0xFFEF5350)
//        else Color(0xFFE8F5E9)
//    }

    val targetColor = when {

        currentStatus == "late" -> Color(0xFFFFF9C4)


        currentStatus == "absent" -> {
            when {
                healthFactor > 0.7f -> Color(0xFFC8E6C9)
                healthFactor > 0.3f -> Color(0xFFFFF9C4)
                else -> Color(0xFFFFCDD2)
            }
        }


        else -> Color(0xFFE8F5E9)
    }

    val animatedBackground by
    animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(600)
    )

    val statusTheme = when (currentStatus) {
        "absent" -> Color.Red to Icons.Default.Close
        "late" -> Color(0xFFFFA500) to Icons.Default.Timer
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
            val options = listOf("present", "late", "absent")
            options.forEach { statusName ->
                val icon = when(statusName) {
                    "present" -> Icons.Default.Check
                    "late" -> Icons.Default.Timer
                    else -> Icons.Default.Close
                }
                val itemColor = when (statusName) {
                    "absent" -> Color.Red
                    "late" -> Color(0xFFFFA500)
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

