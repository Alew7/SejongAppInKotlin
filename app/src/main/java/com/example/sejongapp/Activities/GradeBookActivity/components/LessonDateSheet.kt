package com.example.sejongapp.Activities.GradeBookActivity.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
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
import com.example.sejongapp.Activities.GradeBookActivity.isDateInFuture

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
                text = "Выберите дату урока",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
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

                    val isEnabled = availableDates.contains(dateString)
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
                        if (isFuture) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color.LightGray.copy(alpha = 0.6f)
                            )
                        } else {
                            Text(
                                text = day.toString(),
                                fontSize = 16.sp,
                                fontWeight = if (isEnabled) FontWeight.Bold else FontWeight.Normal,
                                color = when {
                                    isSelected -> Color.White
                                    isEnabled -> primaryColor
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
                onClick = { onDateConfirm(tempSelectedDate) },
                enabled = !isFutureSelected,
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
