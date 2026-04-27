package com.example.sejongapp.Activities.GradeBookActivity.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sejongapp.Activities.AnnousmentActivity.ui.theme.primaryColor
import com.example.sejongapp.Activities.GradeBookActivity.getDaysInMonth
import com.example.sejongapp.Activities.GradeBookActivity.getFirstDayOffset
import com.example.sejongapp.Activities.GradeBookActivity.isDateInFuture
import com.example.sejongapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonDateBottomSheet(
    sheetState: SheetState,
    availableDates: List<String>,
    selectedDate: String,
    onDismiss: () -> Unit,
    onDateConfirm: (String) -> Unit
) {

    val serverMonths = remember(availableDates) {
        availableDates.map { it.split(" ")[1] }.distinct()
    }

    val dateParts = selectedDate.split(" ")
    var currentMonthName by remember { mutableStateOf(dateParts.getOrNull(1) ?: "February") }
    var currentYear by remember { mutableIntStateOf(dateParts.getOrNull(2)?.toInt() ?: 2026) }
    var tempSelectedDate by remember { mutableStateOf(selectedDate) }

    LaunchedEffect(availableDates) {
        if (availableDates.isNotEmpty()) {

            val calendar = java.util.Calendar.getInstance()
            val todayDay = calendar.get(java.util.Calendar.DAY_OF_MONTH)
            val todayMonth = calendar.getDisplayName(java.util.Calendar.MONTH, java.util.Calendar.LONG, java.util.Locale.ENGLISH)
            val todayYear = calendar.get(java.util.Calendar.YEAR)
            val todayStr = "$todayDay $todayMonth $todayYear"


            val targetDate = when {

                selectedDate.isNotEmpty() && availableDates.contains(selectedDate) -> selectedDate

                availableDates.contains(todayStr) -> todayStr

                else -> availableDates.last()
            }


            val parts = targetDate.split(" ")
            if (parts.size >= 3) {
                tempSelectedDate = targetDate
                currentMonthName = parts[1]
                currentYear = parts[2].toInt()
            }
        }
    }


    var triggerOpeningAnimation by remember { mutableStateOf(false) }



    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(300)
        triggerOpeningAnimation = true
    }

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
            // Шапка с переключателем месяцев
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val currentIndex = serverMonths.indexOf(currentMonthName)

                IconButton(
                    enabled = currentIndex > 0,
                    onClick = { currentMonthName = serverMonths[currentIndex - 1] }
                ) {
                    Icon(Icons.Default.ArrowBackIosNew, null, Modifier.size(20.dp),
                        tint = if (currentIndex > 0) primaryColor else Color.LightGray)
                }

                Text(text = "$currentMonthName $currentYear", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                IconButton(
                    enabled = currentIndex < serverMonths.size - 1,
                    onClick = { currentMonthName = serverMonths[currentIndex + 1] }
                ) {
                    Icon(Icons.Default.ArrowForwardIos, null, Modifier.size(20.dp),
                        tint = if (currentIndex < serverMonths.size - 1) primaryColor else Color.LightGray)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Дни недели
            val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                daysOfWeek.forEach { Text(it, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.SemiBold) }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Сетка календаря
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val offset = getFirstDayOffset(currentMonthName, currentYear.toString())
                items(offset) { Spacer(modifier = Modifier.fillMaxSize()) }

                val daysInMonth = getDaysInMonth(currentMonthName, currentYear.toString())
                items(daysInMonth) { index ->
                    val day = index + 1
                    val dateString = "$day $currentMonthName $currentYear"

                    val isEnabled = availableDates.contains(dateString)
                    val isSelected = tempSelectedDate == dateString
                    val isFuture = isDateInFuture(dateString)

                    // Логика отображения замка (как в твоем примере)
                    val showLock = isEnabled && (isFuture || !triggerOpeningAnimation)

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
                        AnimatedContent(
                            targetState = showLock,
                            transitionSpec = {
                                (fadeIn(animationSpec = tween(500)) + scaleIn(initialScale = 0.8f))
                                    .togetherWith(fadeOut(animationSpec = tween(500)) + scaleOut(targetScale = 0.8f))
                            },
                            label = "LockTransition"
                        ) { targetShowLock ->
                            if (targetShowLock) {
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
                                        isEnabled && !isFuture -> primaryColor
                                        else -> Color.Gray.copy(alpha = 0.4f)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Кнопка подтверждения
            val isSelectedFuture = isDateInFuture(tempSelectedDate)
            val canConfirm = availableDates.contains(tempSelectedDate) && !isSelectedFuture
            val context = LocalContext.current

            Button(
                onClick = {
                    onDateConfirm(tempSelectedDate)
                },
                enabled = canConfirm,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (canConfirm) primaryColor else Color(0xFFE0E0E0)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (isSelectedFuture) context.getString(R.string.UnavailableFuture) else context.getString(R.string.Confirm_selection),
                    color = if (canConfirm) Color.White else Color.Gray
                )
            }
        }
    }
}