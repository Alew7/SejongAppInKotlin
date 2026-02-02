package com.example.sejongapp.Activities.GradeBookActivity.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sejongapp.Activities.AnnousmentActivity.ui.theme.primaryColor
import com.example.sejongapp.Activities.GradeBookActivity.getDaysInMonth
import com.example.sejongapp.Activities.GradeBookActivity.getFirstDayOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonDateBottomSheet(
    sheetState: SheetState,
    availableDates: List<String>,
    selectedDate: String,
    onDismiss: () -> Unit,
    onDateConfirm: (String) -> Unit
) {
    var tempSelectedDate by remember { mutableStateOf(selectedDate) }

    val dateParts = tempSelectedDate.split(" ")
    val currentMonth = dateParts.getOrNull(1) ?: "January"
    val currentYear = dateParts.getOrNull(2) ?: "2026"

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = { BottomSheetDefaults.DragHandle(color = primaryColor) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 40.dp)
        ) {
            Text(
                text = "Проверка дат расписания",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Дни недели
            val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                daysOfWeek.forEach {
                    Text(it, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.SemiBold)
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

                    // ПРОВЕРКА: есть ли дата в списке сервера
                    val isLessonDay = availableDates.contains(dateString)
                    val isSelected = tempSelectedDate == dateString

                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) primaryColor else Color.Transparent
                            )
                            .clickable {
                                tempSelectedDate = dateString
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day.toString(),
                            fontSize = 16.sp,
                            // Жирный черный для дат из лога, серый для остальных
                            fontWeight = if (isLessonDay) FontWeight.ExtraBold else FontWeight.Normal,
                            color = when {
                                isSelected -> Color.White
                                isLessonDay -> Color.Black
                                else -> Color.LightGray.copy(alpha = 0.5f)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onDateConfirm(tempSelectedDate) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Подтвердить выбор", color = Color.White)
            }
        }
    }
}