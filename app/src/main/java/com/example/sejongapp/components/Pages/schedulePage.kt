package com.example.sejongapp.components.Pages
import LocalData
import android.content.Intent
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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

    when (val result = scheduleState) {
        is NetworkResponse.Success -> {
            Log.d(TAG, "the schedule result is Successful")
            Log.i(TAG, "the schedule result is $result")

            var scheduleData: ArrayList<ScheduleData> = arrayListOf()
            scheduleData = result.data as? ArrayList<ScheduleData> ?: arrayListOf()

//            var sortedScheduleData: ArrayList<ScheduleData> = sortScheduleData(scheduleData)
            var sortedScheduleData = if (selectedPage == 0) {
                sortScheduleData(scheduleData)
            } else {
                sortScheduleByLastDigitOnly(scheduleData)
            }


//            LazyColumn(
//                modifier = Modifier.padding(bottom = 105.dp)
//            ) {
//                if (selectedPage != 0){
//                    items(sortedScheduleData.size) { index ->
//                        if (selectedPage == sortedScheduleData[index].book){
//                            table(sortedScheduleData[index])
//                        }
//                    }
//                }
//                else{
//                    items(sortedScheduleData.size) { index ->
//                        table(sortedScheduleData[index])
//                    }
//                }
//            }

            var sortedScheduleDataa = sortScheduleData(scheduleData)

            LazyColumn(
                modifier = Modifier.padding(bottom = 105.dp)
            ) {
                if (selectedPage != 0) {
                    items(sortedScheduleDataa.size) { index ->
                        if (selectedPage == sortedScheduleDataa[index].book) {
                            table(sortedScheduleDataa[index])
                        }
                    }
                } else {

                    items(sortedScheduleDataa.size) { index ->
                        table(sortedScheduleDataa[index])
                    }
                }
            }
        }
        is NetworkResponse.Error -> {
            Log.d(TAG, "the schedule result is Error")
            Log.e(TAG, "the schedule result is ${result.message}")

            Text(
                text = result.message,
                color = Color.Red
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
                CircularProgressIndicator(
                    color = primaryColor,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }
        else -> {}
    }
}


// a compose func for pagination (the one that sorts data from all to the specific group)
@Composable
fun PaginationSelector(
    pages: List<String> = listOf(LocalContext.current.getString(R.string.All), "1", "2", "3", "4","5","6","7"),
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


@Composable
fun table(scheduleData: ScheduleData) {

    val context = LocalContext.current


    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)

        ) {
            // Название группы
            Text(
                text = scheduleData.group,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Заголовок таблицы
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = context.getString(R.string.DAY),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Text(
                    text = context.getString(R.string.TIME),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Text (
                    text = context.getString(R.string.CLASS),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.Gray
                )


            }

            Divider(
                color = Color(0xFFE0E0E0),
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Список дней
            scheduleData.time.forEachIndexed { index, time,  ->
                TableRowElements(
                    day = weekDays[time.day].toString(),
                    time = "${time.start_time} - ${time.end_time}",
                    classRoom = "${time.classroom}"


                )

                if (index != scheduleData.time.lastIndex) {
                    Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
                }
            }



            Spacer(modifier = Modifier.height(12.dp))

            // Учитель
            Text(
                text = scheduleData.teacher,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF757575),
                modifier = Modifier.align(Alignment.End)
            )


        }
    }
}

@Composable
fun TableRowElements(day: String, time: String,classRoom: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = day,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
        Text(
            text = time,
            fontSize = 16.sp,
            color = Color(0xFF424242)
        )
        Text (
            text = classRoom,
            fontSize = 16.sp,
            color = Color(0xFF424242)
        )
    }
}



//fun sortScheduleData(scheduleData: ArrayList<ScheduleData>): ArrayList<ScheduleData>{
//
//
//    Log.i(TAG, "starts soring the schedule data, the size of datas ${scheduleData.size}")
//    var sortedScheduleData: ArrayList<ScheduleData> = arrayListOf<ScheduleData>()
//    var i : Int = 0
//    sortedScheduleData.add(scheduleData[0])
//
//    scheduleData.forEach { item->
//
//        if (i == 0){
//            i++;
//            return@forEach
//        }
//
//        for (j in 0..sortedScheduleData.size-1){
//            if (item.book < sortedScheduleData[j].book){
//                sortedScheduleData.add(j, item)
//                return@forEach
//            }
//            else if (j == sortedScheduleData.size-1){
//                sortedScheduleData.add(item)
//            }
//        }
//
//    }
//    Log.i(TAG, "the data is sorted")
//    sortedScheduleData.forEach { item->
//        Log.i(TAG, "the book : ${item.book}")
//    }
//    return sortedScheduleData;
//
//}
fun sortScheduleData(scheduleData: ArrayList<ScheduleData>): ArrayList<ScheduleData> {
    Log.i(TAG, "Starts sorting the schedule data, size: ${scheduleData.size}")


    val sortedList = scheduleData.sortedWith(
        compareBy<ScheduleData> { it.book }
            .thenBy { getLastNumber(it.group) }
    )

    Log.i(TAG, "Data is sorted by book and last digit")
    return ArrayList(sortedList)
}
fun sortScheduleByLastDigitOnly(scheduleData: ArrayList<ScheduleData>): ArrayList<ScheduleData>{
    return ArrayList(
        scheduleData.sortedBy { getLastNumber(it.group) }
    )
}
//fun getLastNumber(group: String): Int {
//    return group.substringAfterLast("-").toIntOrNull()?:0
//}

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