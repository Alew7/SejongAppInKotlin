package com.example.sejongapp.DialogModels

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sejongapp.models.DataClasses.StudentGroups.Student
import com.example.sejongapp.ui.theme.primaryColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentInfoBottomSheet(
    student: Student,
    allSkips: Int,
    presents: Int,
    lates: Int,
    onDismiss: () -> Unit
) {
    var animationPlayed by remember { mutableStateOf(false) }

    // Плавная анимация от 0 до 1
    val curScale by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = androidx.compose.animation.core.FastOutSlowInEasing)
    )

    LaunchedEffect(Unit) { animationPlayed = true }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = student.student_name_en, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(text = "Live Статистика", fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(30.dp))

            // ГРАФИКА
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
                val total = (allSkips + presents + lates).toFloat().coerceAtLeast(1f)

                androidx.compose.foundation.Canvas(modifier = Modifier.size(170.dp)) {
                    val stroke = 45f
                    val skipDeg = (allSkips / total) * 360f * curScale
                    val lateDeg = (lates / total) * 360f * curScale
                    val presentDeg = (presents / total) * 360f * curScale

                    // Был (Зеленый)
                    drawArc(
                        color = Color(0xFF4CAF50),
                        startAngle = -90f,
                        sweepAngle = presentDeg,
                        useCenter = false,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(stroke, cap = androidx.compose.ui.graphics.StrokeCap.Round)
                    )
                    // Опоздал (Оранжевый)
                    drawArc(
                        color = Color(0xFFFFB74D),
                        startAngle = -90f + presentDeg,
                        sweepAngle = lateDeg,
                        useCenter = false,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(stroke, cap = androidx.compose.ui.graphics.StrokeCap.Round)
                    )
                    // НБ (Красный)
                    drawArc(
                        color = Color(0xFFFF5252),
                        startAngle = -90f + presentDeg + lateDeg,
                        sweepAngle = skipDeg,
                        useCenter = false,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(stroke, cap = androidx.compose.ui.graphics.StrokeCap.Round)
                    )
                }

                val attendanceRate = ((presents.toFloat() / total) * 100).toInt()
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "$attendanceRate%",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = primaryColor
                    )
                    Text(
                        "Успеваемость",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(35.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatMiniCard(
                    "Был", presents.toString(), Color(0xFF4CAF50), Modifier.weight(1f))
                StatMiniCard(
                    "НБ", allSkips.toString(), Color(0xFFFF5252), Modifier.weight(1f))
                StatMiniCard(
                    "Опозд.", lates.toString(), Color(0xFFFFB74D), Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun StatMiniCard(label: String, value: String, color: Color, modifier: Modifier) {
    Column(
        modifier = modifier
            .background(color.copy(0.05f), RoundedCornerShape(15.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )

        Text(
            text = label,
            fontSize = 11.sp,
            color = Color.Gray
        )
    }
}