package com.example.sejongapp.Pages
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sejongapp.R
import com.example.sejongapp.models.DataClasses.ScheduleData
import com.example.sejongapp.models.DataClasses.ScheduleTime
import com.example.sejongapp.ui.theme.cardGreyBackground
import com.example.sejongapp.ui.theme.lightGray
import com.example.sejongapp.ui.theme.primaryColor
import com.example.sejongapp.utils.NavigationScreenEnum
import org.jetbrains.annotations.Async


@Composable
fun Schedule(onChangeScreen: (NavigationScreenEnum) -> Unit = {}){

    var scheduleData: ArrayList<ScheduleData> = arrayListOf(
        ScheduleData(1, "Sejong Group 1", "Alisher", listOf<ScheduleTime>(
            ScheduleTime(301, 0, "12:00", "13:00"),
            ScheduleTime(301, 2, "12:00", "13:00"),
            ScheduleTime(301, 4, "12:00", "13:00"),
            )
        ),
        ScheduleData(2, "Sejong Group 2", "Anushervon", listOf<ScheduleTime>(
            ScheduleTime(301, 1, "12:00", "14:00"),
            ScheduleTime(301, 3, "12:00", "14:00"),
        )
    )
    )

    Column {
        //    The header with logo icon
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .drawBehind {
                        val strokeWidth = 2.dp.toPx()
                        val y = size.height - strokeWidth / 2
                        drawLine(
                            color = primaryColor,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = strokeWidth
                        )
                    }
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = "ic_head",
                    modifier = Modifier
                        .size(64.dp)
                        .padding(start = 25.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null

                        ) {
                            onChangeScreen(NavigationScreenEnum.HOMEPAGE)
                        }
                )
            }
        }

        /////////////////////////////////////   BODY  /////////////////////////////////////

        var selectedPage by remember { mutableStateOf(0) }

//        The pagination func for sorting data
        PaginationSelector(
            selectedIndex = selectedPage,
            onSelected = { selectedPage = it }
        )

        Spacer(modifier = Modifier.height(4.dp))

        val sortedScheduleData: ArrayList<ScheduleData> = scheduleData
        if  (selectedPage != 0) scheduleData.filter { it.book == scheduleData[selectedPage-1].book }

        LazyColumn {
            items(sortedScheduleData.size) { index ->
                table(sortedScheduleData[index])
            }
        }


    }
  


}

// a compose func for pagination (the one that sorts data from all to the specific group)
@Composable
fun PaginationSelector(
    pages: List<String> = listOf("All", "1", "2", "3", "4","5","6","7"),
    selectedIndex: Int,
    onSelected: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(pages) { index, label ->
            val isSelected = index == selectedIndex

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) primaryColor
                        else lightGray
                    )
                    .clickable { onSelected(index) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }




}


@Composable()
fun table(scheduleData: ScheduleData) {
    ElevatedCard (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
//            .background(lightGray),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = cardGreyBackground
        )


    )
    {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = scheduleData.group,
                fontSize = 32.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
//                DAY
                Box(
                    Modifier.weight(1F).drawBehind {
                        val stroke = 2.dp.toPx()
                        val borderColor = Color.Black

//                        Right border
                        drawLine(borderColor, Offset(size.width, 0f), Offset(size.width, size.height), stroke/2)

                        // Bottom border
                        drawLine(borderColor, Offset(0f, size.height), Offset(size.width, size.height), stroke)
                    }
                ){
                    Text(
                        text = "DAY",
                        fontSize = 20.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
//                TIME
                Box(
                    Modifier.weight(2F).drawBehind {
                        val stroke = 2.dp.toPx()
                        val borderColor = Color.Black

                        // Bottom border
                        drawLine(borderColor, Offset(0f, size.height), Offset(size.width, size.height), stroke)
                    }
                ){
                    Text(
                        text = "TIME",
                        fontSize = 20.sp,
                        modifier = Modifier.align(Alignment.Center)

                    )
                }
            }

            scheduleData.time.forEach { time ->
                TableRowElements(time.day.toString(), time.start_time + "-" + time.end_time)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier.align(Alignment.Start),
                fontSize = 20.sp,
                text = scheduleData.teacher
            )
        }
    }
}


@Composable()
fun TableRowElements(day: String, time: String){
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
//                DAY
        Box(
            Modifier.weight(1F).drawBehind {
                val stroke = 1.dp.toPx()
                val borderColor = Color.Black

//                        Right border
                drawLine(borderColor, Offset(size.width, 0f), Offset(size.width, size.height), stroke)

                // Top border
                drawLine(borderColor, Offset(0f, 0f), Offset(size.width, 0f), stroke)

            }
        ){
            Text(
                text = day,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.Center).padding(vertical = 10.dp)
            )
        }

//                TIME
        Box(
            Modifier.weight(2F).drawBehind {
                val stroke = 1.dp.toPx()
                val borderColor = Color.Black

                // Top border
                drawLine(borderColor, Offset(0f, 0f), Offset(size.width, 0f), stroke)
            }
        ){
            Text(
                text = time,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.Center).padding(vertical = 10.dp)

            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable()
private fun Preview() {
    Async.Schedule()
}