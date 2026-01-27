package com.example.sejongapp.components.Pages
import LocalData
import android.content.Intent
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sejongapp.Activities.SpleshLoginPages.SplashLoginActivity
import com.example.sejongapp.R
import com.example.sejongapp.models.DataClasses.ScheduleData
import com.example.sejongapp.models.ViewModels.UserViewModels.ScheduleViewModel
import com.example.sejongapp.models.ViewModels.UserViewModels.UserViewModel
import com.example.sejongapp.retrofitAPI.NetworkResponse
import com.example.sejongapp.room.RoomUserViewModel
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.lightGray
import com.example.sejongapp.ui.theme.primaryColor
import com.example.sejongapp.utils.NavigationScreenEnum

const val TAG = "Schedule_TAG"

var weekDays = HashMap<Int, String>()

@Composable
fun Schedule(onChangeScreen: (NavigationScreenEnum) -> Unit = {}){

    val scheduleViewModel: ScheduleViewModel = viewModel()
    val scheduleResult = scheduleViewModel.scheduleResult.observeAsState(NetworkResponse.Idle)

    val context = LocalContext.current
    val roomviewModel: RoomUserViewModel = viewModel()
    val userState by roomviewModel.user.collectAsState()

    val token = LocalData.getSavedToken(context)


    LaunchedEffect(Unit) {
        roomviewModel.loadUser(token)
    }







    if (LocalData.getSavedToken(context) == "null"){
        Log.i(TAG, "The token is ${LocalData.getSavedToken(context)}")
        val intent = Intent (context, SplashLoginActivity:: class.java)
        context.startActivity(intent)

    }

    BackHandler {
        onChangeScreen(NavigationScreenEnum.HOMEPAGE)
    }

    Log.i(TAG, "Ther user's token is ${LocalData.getSavedToken(context)}")
    Log.i(TAG, "The user data here is ${LocalData.getUserData(context)}")

//    Getting all the schedule data from the server db
    LaunchedEffect(Unit) {
        scheduleViewModel.getAllSchedules(context)
    }


    context.resources.getStringArray(R.array.week_days).forEachIndexed { index, day ->
        weekDays.put(index, day)
    }




    Column  (
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ){
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

        ScheduleScreen(scheduleViewModel, selectedPage)


    }
}

@Composable
fun ScheduleScreen(viewModel: ScheduleViewModel, selectedPage: Int) {
    val scheduleState by viewModel.scheduleResult.observeAsState()


    androidx.compose.animation.AnimatedContent(
        targetState = scheduleState,
        label = "screenTransition"
    ) { state ->
        when (val result = state) {
            is NetworkResponse.Success -> {

                Log.d(TAG, "the schedule result is Successful")
                Log.i(TAG, "the schedule result is $result")

                val scheduleData = result.data as? ArrayList<ScheduleData> ?: arrayListOf()


                val sortedScheduleDataa = sortScheduleData(scheduleData)


                val filteredData = if (selectedPage != 0) {
                    sortedScheduleDataa.filter { it.book == selectedPage }
                } else {
                    sortedScheduleDataa
                }

                LazyColumn(
                    modifier = Modifier.padding(bottom = 105.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    itemsIndexed(
                        items = filteredData,
                        key = { _, item -> "${item.group}_${item.book}" }
                    ) { index, item ->


                        val visibleState = remember(selectedPage) {
                            MutableTransitionState(false).apply { targetState = true }
                        }


                        AnimatedVisibility(
                            visibleState = visibleState,
                            enter = fadeIn(animationSpec = tween(400, delayMillis = index * 50)) +
                                    slideInVertically(
                                        initialOffsetY = { 40 },
                                        animationSpec = tween(400, delayMillis = index * 50)
                                    )
                        ) {
                            table(item)
                        }
                    }
                }
            }
            is NetworkResponse.Error -> {
                Log.d(TAG, "the schedule result is Error")
                Log.e(TAG, "the schedule result is ${result.message}")
                Text(
                    text = result.message,
                    color = Color.Red,
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
            is NetworkResponse.Loading -> {
                Log.d(TAG, "the schedule result is Loading")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = primaryColor)
                }
            }
            else -> {}
        }
    }
}


@Composable
fun PaginationSelector(
    pages: List<String> = listOf(LocalContext.current.getString(R.string.All), "1", "2", "3", "4", "5", "6", "7"),
    selectedIndex: Int,
    onSelected: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        itemsIndexed(pages) { index, label ->
            val isSelected = index == selectedIndex

            Card(
                modifier = Modifier
                    .size(46.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onSelected(index) },
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = if (isSelected) 6.dp else 1.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) primaryColor else Color.White
                ),
                border = if (!isSelected) BorderStroke(1.dp, Color.LightGray.copy(0.5f)) else null
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = if (isSelected) Color.White else Color.Black,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat_medium))
                    )
                }
            }
        }
    }
}

@Composable
fun table(scheduleData: ScheduleData) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .background(Color.White, RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        // Шапка карточки (Группа и Преподаватель)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text(
                    text = scheduleData.group,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF1A1A1A),
                    letterSpacing = (-1).sp
                )
                Text(
                    text = "Преподаватель: ${scheduleData.teacher}",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                text = "КНИГА ${scheduleData.book}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor,
                modifier = Modifier
                    .background(primaryColor.copy(0.1f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))


        val sortedTimeList = scheduleData.time.sortedBy { it.day }


        sortedTimeList.forEach { time ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(35.dp)
                        .clip(CircleShape)
                        .background(primaryColor)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Название дня (MON, TUE и т.д.)
                Text(
                    text = weekDays[time.day]?.take(3)?.uppercase() ?: "???",
                    modifier = Modifier.width(45.dp),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF1A1A1A)
                )

                // Время занятия
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${time.start_time} — ${time.end_time}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF444444)
                    )
                }

                // Номер кабинета
                Box(
                    modifier = Modifier
                        .background(Color(0xFFF0F2F5), RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "класс. ${time.classroom}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF555555)
                    )
                }
            }

            Divider(
                color = Color(0xFFF5F5F5),
                thickness = 1.dp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}




fun sortScheduleData(scheduleData: ArrayList<ScheduleData>): ArrayList<ScheduleData> {
    Log.i(TAG, "Starts sorting the schedule data, size: ${scheduleData.size}")


    val sortedList = scheduleData.sortedWith(
        compareBy<ScheduleData> { it.book }
            .thenBy { getLastNumber(it.group) }
    )

    Log.i(TAG, "Data is sorted by book and last digit")
    return ArrayList(sortedList)
}



fun getLastNumber(group: String): Int {
    return try {

        group.substringAfterLast("-").filter { it.isDigit() }.toInt()
    } catch (e: Exception) {
        0
    }
}




@Preview(showSystemUi = true)
@Composable()
private fun Preview() {
    Schedule()
}