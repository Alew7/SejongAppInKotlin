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
    onStatusChange: (String) -> Unit
) {


    var isExpanded by remember { mutableStateOf(false) } // отвечает за раскрытие списка студентов
    val isChecked = currentStatus != "Не был" // Switch считается включонным если студент не Был



    /* ------------------ ЛОГИКА ЗДОРОВИЯ -----------------*/


    val totalNB = if (isSaved && currentStatus == "Не был") 1 else 0

    // каждый пропуск отнимает 14.2%
    val totalDamage = (totalNB * 14.2f).coerceIn(0f, 100f)

    val healthFactor = (100f - totalDamage) / 100f


    // анимация изменения здоровия

    val animatedHealth by animateFloatAsState(
        targetValue = healthFactor,
        animationSpec = tween(800)
    )

    /* --------------------- ЦВЕТ ФОНА------------------------------*/

    // Выбираем цвет фона в зависимости от статуса и здоровья

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

    // плавная анимация смена цвета
    val animatedBackground by
    animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(600)
    )

    /* ---------------- ИКОНКА + ЦВЕТ СТАТУСА ----------*/

    val statusTheme = when (currentStatus) {
        "Не был" -> Color.Red to Icons.Default.Close
        "Опоздал" -> Color(0xFFFFA500) to Icons.Default.Timer
        else -> Color(0xFF4CAF50) to Icons.Default.Check
    }

    /*------------------ КАРТОЧКА СТУДЕНТА ---------------- */


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            // рисуем цветную полосу здоровя
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
            // Аватарка студентов

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

            // имя студентов и его статус
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = student.student_name_en,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                // показываем список
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
                // Switch используем как индикатор статус
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

        /* ----------- ВЫПАДАЮШИЙ СПИСОК ------------- */

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