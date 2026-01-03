package com.example.sejongapp.NavBar


import LocalData.deletToken
import LocalData.getUserData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sejongapp.MainActivity
import com.example.sejongapp.Pages.AnnousmentPage
import com.example.sejongapp.components.Pages.ElectronicLibraryPage
import com.example.sejongapp.components.Pages.HomePage
import com.example.sejongapp.components.Pages.Schedule
import com.example.sejongapp.ProfileActivity.ProfileActivity
import com.example.sejongapp.R
import com.example.sejongapp.models.DataClasses.Content
import com.example.sejongapp.models.DataClasses.Title
import com.example.sejongapp.models.ViewModels.UserViewModel
import com.example.sejongapp.retrofitAPI.NetworkResponse
import com.example.sejongapp.ui.theme.WarmBeige
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.primaryColor
import com.example.sejongapp.utils.NavigationScreenEnum
import kotlinx.coroutines.launch
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import com.example.sejongapp.TelegramManager.TelegramManager
import com.example.sejongapp.components.ReviewDialog
import com.example.sejongapp.utils.UserStatusEnum


const val TAG = "TAG_NavBar"



@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun NavBar(modifier: Modifier = Modifier) {
    val iconSize = 40.dp
    val navItemList = listOf(
        NavItem(R.drawable.ic_burger),  // index 0;
        NavItem(R.drawable.annousment), // index 1;
        NavItem(R.drawable.home),       // index 2;
    )

    val userViewModel : UserViewModel = viewModel()
    var selectedIndex by remember { mutableStateOf(NavigationScreenEnum.HOMEPAGE) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val userData = remember { getUserData(context) }

    var showReviewDialog by remember { mutableStateOf(false) }





    LaunchedEffect(Unit) {

        if (LocalData.getSavedToken(context) != "null") {
            getAndSaveUserData(userViewModel,context)
        }
    }


//  The side bar and nav bar
    ModalNavigationDrawer(
        drawerState = drawerState,

//        side bar content
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp),
                drawerContainerColor = Color(0xFFFBF8F1)
            ) {
                var isLanguageListExpanded by remember { mutableStateOf(false) }

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                        .fillMaxSize()

                        .animateContentSize(),
                    horizontalAlignment = Alignment.Start
                ) {
                    // Заголовок "SejongApp"
                    Text(
                        text = "SejongApp",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )


                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable (
                                indication = null,
                                interactionSource = remember {MutableInteractionSource()}
                            )  {
                                val intent = Intent(context, ProfileActivity::class.java)
                                context.startActivity(intent)
                                scope.launch { drawerState.close() }
                            },
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                modifier = Modifier.size(28.dp),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = context.getString(R.string.Profile),
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(

                            modifier = Modifier.animateContentSize()
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable  (
                                        indication = null,
                                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                                    ){
                                        isLanguageListExpanded = !isLanguageListExpanded

                                    }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_lenguage),
                                    contentDescription = "Language",
                                    modifier = Modifier.size(28.dp),
                                    tint = Color.Unspecified
                                )
                                Text(
                                    text = context.getString(R.string.Language),
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(
                                    imageVector = if (isLanguageListExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Toggle Language List",
                                    modifier = Modifier.size(24.dp)
                                )
                            }


                            if (isLanguageListExpanded) {
                                val languages = listOf(
                                    "KOR" to R.drawable.ic_flag_kor,
                                    "ENG" to R.drawable.ic_flag_eng,
                                    "RUS" to R.drawable.ic_flag_rus,
                                    "TAJ" to R.drawable.ic_flag_taj
                                )

                                val currentLang = LocalData.getSavedLanguage(context)

                                languages.forEach { (lang, flagRes) ->

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                changeAppLanguage(context, lang)
                                                scope.launch { drawerState.close() }
                                            }
                                            .background(
                                                if (lang == currentLang) Color(0xFFEDE9E3) else Color.Transparent
                                            )
                                            .padding(horizontal = 12.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            painter = painterResource(flagRes),
                                            contentDescription = lang,
                                            modifier = Modifier.size(28.dp),
                                            tint = Color.Unspecified
                                        )
                                        Text(
                                            text = lang,
                                            modifier = Modifier.padding(start = 12.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    Card (
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)


                    ) {
                        Column {
                            Row ( modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically ) {
                                Image (
                                    painter = painterResource(R.drawable.ic_instagram),
                                    contentDescription = "ic_instegram",
                                    modifier = Modifier.size(28.dp)
                                        .clickable (
                                            indication = null,
                                            interactionSource = remember {(MutableInteractionSource())}
                                        ) {
                                            openInstagram(context,"dushanbe3_king_sejong")
                                        }
                                )
                                Text (
                                    text = context.getString(R.string.Our_Instagram),
                                    modifier = Modifier.padding(start = 12.dp)
                                        .clickable (
                                            indication = null,
                                            interactionSource = remember {MutableInteractionSource()}
                                        ) {
                                            openInstagram(context,"dushanbe3_king_sejong")
                                        }


                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row (
                                modifier = Modifier
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image (
                                    painter = painterResource(R.drawable.ic_telegram),
                                    contentDescription = "ic_telegram",
                                    modifier = Modifier.size(28.dp)
                                        .clickable (
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource()}
                                        )   {
                                            openTelegram(context,"dushanbe3_king_sejong")
                                        }
                                )
                                Text (
                                    text = context.getString(R.string.Contact_Admin),
                                    modifier = Modifier.padding(start = 12.dp)
                                        .clickable (
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }
                                        ) {
                                            openTelegram(context,"dushanbe3_king_sejong")
                                        }
                                )
                            }
                        }
                    }

                    Card (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable (
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                showReviewDialog = true
                            },
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)

                    ) {
                        Row (
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically

                        ) {
                            Image (
                                painter = painterResource(R.drawable.ic_otzif),
                                contentDescription = "ic_otzif",
                                modifier = Modifier.size(28.dp)
                            )
                            Text (
                                text = context.getString(R.string.Review),
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }
                    }
                    if (showReviewDialog) {
                        ReviewDialog  (
                            onDismiss = { showReviewDialog = false },
                            onSend = { rating, text ->
                                scope.launch {
                                    val realname = userData.fullname
                                    val statusName = userData.status.name
                                    val group = userData.groups.joinToString (", ")
                                    TelegramManager.sendReview(
                                        rating = rating,
                                        comment = text,
                                        userName = realname,
                                        status = statusName,
                                        group = group
                                    )
                                }
                                showReviewDialog = false
                            }
                        )
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable (
                                indication = null,
                                interactionSource = remember {MutableInteractionSource()}
                            ) {
                                deletToken(context)
                                scope.launch {drawerState.close()}
                            },
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Logout,
                                contentDescription = "Log out",
                                modifier = Modifier.size(28.dp),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = context.getString(R.string.Log_out),
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),

//            nav bar  btns  & Icons
            bottomBar = {
                NavigationBar(
                    containerColor = backgroundColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            val strokeWidth = 2.dp.toPx()
                            drawLine(
                                color = primaryColor,
                                start = Offset(0f, 0f),
                                end = Offset(size.width, 0f),
                                strokeWidth = strokeWidth
                            )
                        }
                ) {
//                    Each Icons
                    navItemList.forEachIndexed { index, navItem ->
                        NavigationBarItem(
                            selected = selectedIndex == NavigationScreenEnum.entries[index],
                            onClick = {
                                if (index == 0) {
                                    scope.launch { drawerState.open() }
                                }
                                else {
                                    selectedIndex = NavigationScreenEnum.entries[index]
                                }

                            },
                            icon = {
                                Icon(
                                    modifier = Modifier.size(24.dp), // 24
                                    painter = painterResource(navItem.icon),
                                    contentDescription = "Icon"

                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = WarmBeige
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->
            ContentScreen(modifier = Modifier.padding(innerPadding), selectedIndex = selectedIndex,onChangeScreen = {index -> selectedIndex = index})
        }
    }
}



@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun ContentScreen (modifier: Modifier = Modifier,selectedIndex : NavigationScreenEnum,onChangeScreen : (NavigationScreenEnum) -> Unit) {
    when(selectedIndex) {
        NavigationScreenEnum.ANNOUNCEMENTS -> AnnousmentPage(onChangeScreen = onChangeScreen)
        NavigationScreenEnum.HOMEPAGE -> HomePage(onChangeScreen = onChangeScreen, viewModel = UserViewModel())
        NavigationScreenEnum.SCHEDULE -> Schedule(onChangeScreen = onChangeScreen)
        NavigationScreenEnum.LIBRARY -> ElectronicLibraryPage(onChangeScreen = onChangeScreen)
        NavigationScreenEnum.SIDEBAR -> TODO() //it is for the sidebar only! no functions need to be applied

    }
}



fun getAndSaveUserData(userViewModel: UserViewModel, context: Context) {
    userViewModel.getUserData(LocalData.getSavedToken(context))
    userViewModel.userDataResult.observeForever { result ->
        when (result) {
            is NetworkResponse.Error -> Log.e(TAG, "Error getting user data")
            is NetworkResponse.Idle -> Log.i(TAG, "The user data now is Idle")
            is NetworkResponse.Loading -> Log.d(TAG, "The user data is loading")
            is NetworkResponse.Success -> {
                LocalData.setUserData(context, result.data)
            }
            null -> { /* ignore */ }
        }
    }
}

fun changeAppLanguage(context: Context, lang: String) {
    LocalData.setLanguage(context, lang) // save language
    val intent = Intent(context, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intent)
}

fun Title.getLocalized(context: Context): String {
    return when (LocalData.getSavedLanguage(context)) {
        "RUS" -> this.rus
        "ENG" -> this.eng
        "KOR" -> this.kor
        "TAJ" -> this.taj
        else -> this.rus // по умолчанию русский
    }
}

fun Content.getLocalized(context: Context): String {
    return when (LocalData.getSavedLanguage(context)) {
        "RUS" -> this.rus
        "ENG" -> this.eng
        "KOR" -> this.kor
        "TAJ" -> this.taj
        else -> this.rus
    }
}



fun openTelegram(context: Context,username: String) {
    val telegramIntent = Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=$username"))
    try {
        context.startActivity(telegramIntent)

    } catch (e: Exception) {
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/$username"))
        context.startActivity(webIntent)
    }
}



//@Preview (showBackground = true, showSystemUi = true)
//@Composable
//private  fun Preview () {
//    NavBar()
//}





fun openInstagram ( context: Context,username: String ) {
    val uri = Uri.parse("http://instagram.com/_u/$username")
    val instagramIntent = Intent(Intent.ACTION_VIEW,uri).apply {
        setPackage("com.instagram.android")
    }

    try {
        context.startActivity(instagramIntent)
    } catch (e: Exception) {
        val webIntent = Intent(Intent.ACTION_VIEW,Uri.parse("https://intagram.com/$username"))
        context.startActivity(webIntent)
    }
}



