package com.example.sejongapp.DialogModels


import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sejongapp.models.DataClasses.StudentGroups.Student
import com.example.sejongapp.ui.theme.primaryColor
import com.example.sejongapp.R


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
    val total = (allSkips + presents + lates).toFloat().coerceAtLeast(1f)

    // Анимация появления
    val curScale by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(1000, easing = FastOutSlowInEasing)
    )

    LaunchedEffect(Unit) { animationPlayed = true }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color(0xFFE0E0E0)) },
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Имя и подзаголовок
            Text(
                text = student.student_name_en,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )

            Text(
                text = stringResource(R.string.Live_Statistics).lowercase(),
                fontSize = 14.sp,
                color = primaryColor,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(25.dp))

            // Центральный блок с графиком и общими данными
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF7F9FC), RoundedCornerShape(24.dp))
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Круговой график (слева)
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(120.dp)) {
                    androidx.compose.foundation.Canvas(modifier = Modifier.size(100.dp)) {
                        val stroke = 30f
                        val presentDeg = (presents / total) * 360f * curScale
                        val lateDeg = (lates / total) * 360f * curScale
                        val skipDeg = (allSkips / total) * 360f * curScale

                        // Слой "Присутствие"
                        drawArc(Color(0xFF4CAF50), -90f, presentDeg, false, style = Stroke(cap = StrokeCap.Round, width = stroke))

                        // Слой "Опоздание"
                        drawArc(Color(0xFFFFA000), -90f + presentDeg, lateDeg, false, style = Stroke(stroke, cap = StrokeCap.Round))

                        // Слой "Пропуск"
                        drawArc(Color(0xFFE53935), -90f + presentDeg + lateDeg, skipDeg, false, style = Stroke(stroke, cap = StrokeCap.Round))
                    }
                    Text(
                        "${((presents / total) * 100).toInt()}%",
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                // Информация справа от графика
                Column {
                    Text(
                        text = stringResource(R.string.Academic_Performance),
                        fontSize = 13.sp,
                        color = Color.Gray
                    )

                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Сетка из трех карточек в ряд
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ModernStatCard(
                    icon = Icons.Default.Check,
                    label = stringResource(R.string.present),
                    value = presents.toString(),
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.weight(1f)
                )
                ModernStatCard(
                    icon = Icons.Default.Timer,
                    label = stringResource(R.string.late),
                    value = lates.toString(),
                    color = Color(0xFFFFA000),
                    modifier = Modifier.weight(1f)
                )
                ModernStatCard(
                    icon = Icons.Default.Close,
                    label = stringResource(R.string.absent),
                    value = allSkips.toString(),
                    color = Color(0xFFE53935),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun ModernStatCard(
    icon: ImageVector,
    label: String,
    value: String,
    color: Color,
    modifier: Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFEEEEEE))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color.Gray,
                maxLines = 1
            )
        }
    }
}