package com.example.sejongapp.Pages

import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sejongapp.R
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.primaryColor
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.sejongapp.AnnousmentActivity.AnnousmentActivity
import com.example.sejongapp.NavBar.getLocalized
import com.example.sejongapp.components.showError
import com.example.sejongapp.models.DataClasses.AnnouncementDateItem
import com.example.sejongapp.models.ViewModels.AnnouncmentsViewModel
import com.example.sejongapp.retrofitAPI.NetworkResponse
import com.example.sejongapp.ui.theme.brightBackgroundColor
import com.example.sejongapp.ui.theme.darkGray
import com.example.sejongapp.utils.NavigationScreenEnum


const val TAG = "AnnouncmentsViewModel_TAG"



@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnousmentPage(onChangeScreen: (NavigationScreenEnum) -> Unit = {}) {

    val announcementView : AnnouncmentsViewModel = viewModel()
    val context = LocalContext.current
    var isSeraching by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }


    LaunchedEffect(Unit) {
        Log.i(TAG, "AnnouncementPage: Starting to fetch data")
        announcementView.getAllannouncments(context)
    }


    BackHandler {
        onChangeScreen(NavigationScreenEnum.HOMEPAGE);
    }


//    The header (with logo icon and search btn)
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        val screenWidth = maxWidth

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
                if (!isSeraching) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "ic_ArrowBack",
                    modifier = Modifier
                        .size(64.dp)
                        .padding(start = 25.dp)
                        .clickable (
                            interactionSource = remember {MutableInteractionSource()},
                            indication = null

                        ) {
                            onChangeScreen(NavigationScreenEnum.HOMEPAGE)
                        }
                )

                Image (
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = "ic_search",
                    modifier = Modifier
                        .size(48.dp)
                        .padding(end = 20.dp,top = 20.dp)
                        .align(Alignment.TopEnd)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null

                        ) {
                            isSeraching = true
                        }
                )

            }

                if (isSeraching) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                                .padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {


                            Spacer(modifier = Modifier.width(20.dp))

                            OutlinedTextField(
                                value = searchText,
                                onValueChange = { searchText = it },
                                placeholder = { Text("Search") },
                                leadingIcon = {
                                    IconButton(onClick = {isSeraching = false}) {
                                        Icon (
                                            imageVector = Icons.Default.ArrowBack,
                                            contentDescription = "Back",
                                            tint = Color.Black
                                        )
                                    }
                                },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedTextColor = Color.Black,
                                    focusedBorderColor = darkGray,
                                    cursorColor = Color.Black

                                ),
                                modifier = Modifier
                                    .padding(end = 15.dp)
                                    .fillMaxWidth()
                                    .height(60.dp),
                                shape = RoundedCornerShape(15.dp)

                            )


                        }
                }
            }


        }
    }


    val result by announcementView.announcments.observeAsState(NetworkResponse.Idle)


    when(result){
        is NetworkResponse.Error -> {
            Log.e(TAG, "AnnouncementPage: Got an error in fetching data")
            showError((result as NetworkResponse.Error).message){
                announcementView.resetAnnouncments()
            }
        }

        NetworkResponse.Idle -> {}
        NetworkResponse.Loading -> {
            Log.d(TAG, "AnnouncementPage: Loading the data")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 100.dp),
                contentAlignment = Alignment.Center

            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }
        is NetworkResponse.Success -> {
            Log.v(TAG, "AnnouncementPage: Success got data!")
            Log.i(TAG, "AnnouncementPage: the fetched data is ${(result as NetworkResponse.Success<AnnouncementDateItem>).data}")

            val announcementData: List<AnnouncementDateItem> = (result as NetworkResponse.Success<List<AnnouncementDateItem>>).data
            Log.i(TAG, "AnnouncementPage: the data is in the var and its $announcementData")
            Log.i(TAG, "AnnouncementPage: the data size is ${announcementData.size}")

                // Список карточек
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 110.dp), // 110
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp,  bottom = 100.dp), // 80
                    verticalArrangement = Arrangement.spacedBy(5.dp) // 16.dp
                ) {
                    items(
                        items = announcementData,
                        key = { it.custom_id}
                    ) { ann ->
                        AnnousmentCard(ann) {
                            val intent = Intent(context, AnnousmentActivity::class.java)
                            intent.putExtra("AnnData", ann)
                            context.startActivity(intent)
                        }
                    }
                }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AnnousmentCard(annData: AnnouncementDateItem, onClick: () -> Unit) {


    val context = LocalContext.current
    val imgSize = 64.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            },
        shape = RoundedCornerShape(15.dp),
//        elevation = CardDefaults.cardElevation(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = brightBackgroundColor,
        )
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            val firstImage = annData.images?.firstOrNull()?.let { fixGoogleDriveLink(it) }
            Image(
                painter = rememberImagePainter(firstImage),
                contentDescription = "annousment_img",
                modifier = Modifier.size(imgSize)
                    .clip(RoundedCornerShape(15.dp))


            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {

                Text(
                    text = annData.title.getLocalized(context),
                    fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = annData.content.getLocalized(context),
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp),
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis

                )
                Text(
                    text = annData.time_posted,
                    fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//private fun Preview() {
//    AnnousmentPage()
//}

fun fixGoogleDriveLink(url: String): String {
    return if (url.contains("drive.google.com/thumbnail?id=")) {
        val id = url.substringAfter("id=")
        "https://drive.google.com/uc?id=$id"
    } else {
        url
    }
}
