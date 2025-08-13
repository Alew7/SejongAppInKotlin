package com.example.sejongapp.NavBar


import LocalData.deletToken
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sejongapp.Pages.AnnousmentPage
import com.example.sejongapp.Pages.HomePage
import com.example.sejongapp.Pages.Schedule
import com.example.sejongapp.Pages.elaibaryPage
import com.example.sejongapp.ProfileActivity.ProfileActivity
import com.example.sejongapp.R
import com.example.sejongapp.SpleshLoginPages.MoveToMainActivity
import com.example.sejongapp.models.DataClasses.UserData
import com.example.sejongapp.models.ViewModels.UserViewModel
import com.example.sejongapp.retrofitAPI.NetworkResponse
import com.example.sejongapp.ui.theme.WarmBeige
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.primaryColor
import com.example.sejongapp.utils.NavigationScreenEnum
import kotlinx.coroutines.launch


const val TAG = "TAG_NavBar"



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
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Меню", style = MaterialTheme.typography.titleLarge)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))

//                   Profile icon btn
                    Row(
                        modifier = Modifier
                            .clickable  (
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ){
                                val intent = Intent(context,ProfileActivity :: class.java)
                                context.startActivity(intent)
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

//
                        Icon(
                            modifier = Modifier.size(iconSize),
                            painter = painterResource(R.drawable.ic_sejong_profile),
                            contentDescription = "Профиль"

                        )
                        Text(
                            text = "Профиль",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

//                   Exit icon btn
                    Row(
                        modifier = Modifier
                            .clickable (
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            )
                            {
                                deletToken(context)
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(iconSize),
                            painter = painterResource(R.drawable.ic_logout),
                            contentDescription = "Выход",

                        )
                        Text(
                            text = "Выход",
                            modifier = Modifier.padding(start = 8.dp)
                        )
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


@Composable
fun ContentScreen (modifier: Modifier = Modifier,selectedIndex : NavigationScreenEnum,onChangeScreen : (NavigationScreenEnum) -> Unit) {
    when(selectedIndex) {
        NavigationScreenEnum.ANNOUNCEMENTS -> AnnousmentPage(onChangeScreen = onChangeScreen)
        NavigationScreenEnum.HOMEPAGE -> HomePage(onChangeScreen = onChangeScreen)
        NavigationScreenEnum.SCHEDULE -> Schedule(onChangeScreen = onChangeScreen)
        NavigationScreenEnum.LIBRARY -> elaibaryPage(onChangeScreen = onChangeScreen)
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




@Preview (showBackground = true, showSystemUi = true)
@Composable
private  fun Preview () {
    NavBar()
}