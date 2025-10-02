package com.example.sejongapp.NavBar


import LocalData.deletToken
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
                drawerContainerColor = backgroundColor
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.Start
                ) {
                    // Заголовок
                    Text(
                        text = "SejongApp",
                        style = MaterialTheme.typography.titleLarge
                    )

                    // ---------- Profile ----------
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                val intent = Intent(context, ProfileActivity::class.java)
                                context.startActivity(intent)
                            },
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_sejong_profile),
                                contentDescription = "Profile",
                                modifier = Modifier.size(28.dp)
                            )
                            Text(
                                text = context.getString(R.string.Profile),
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }
                    }

                    // ---------- Language ----------
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_flag_kor),
                                contentDescription = "Language",
                                modifier = Modifier.size(28.dp)
                            )
                            Text(
                                text = context.getString(R.string.Language),
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }
                    }

                    // ---------- List of languages ----------
                    val languages = listOf(
                        "KOR" to R.drawable.ic_flag_kor,
                        "ENG" to R.drawable.ic_flag_eng,
                        "RUS" to R.drawable.ic_flag_rus,
                        "TAJ" to R.drawable.ic_flag_taj
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column {
                            languages.forEach { (lang, flagRes) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { changeAppLanguage(context, lang) }
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(flagRes),
                                        contentDescription = lang,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Text(
                                        text = lang,
                                        modifier = Modifier.padding(start = 12.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // ---------- Log out ----------
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { deletToken(context) },
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_logout),
                                contentDescription = "Log out",
                                modifier = Modifier.size(28.dp)
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
                                    modifier = Modifier.size(24.dp),
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
        NavigationScreenEnum.HOMEPAGE -> HomePage(onChangeScreen = onChangeScreen)
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






//@Preview (showBackground = true, showSystemUi = true)
//@Composable
//private  fun Preview () {
//    NavBar()
//}