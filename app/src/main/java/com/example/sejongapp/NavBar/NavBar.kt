package com.example.sejongapp.NavBar


import LocalToken.deletToken
import android.content.Intent
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sejongapp.Pages.AnnousmentPage
import com.example.sejongapp.Pages.HomePage
import com.example.sejongapp.ProfileActivity.ProfileActivity
import com.example.sejongapp.R
import com.example.sejongapp.ui.theme.WarmBeige
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.primaryColor
import kotlinx.coroutines.launch




@Composable
fun NavBar(modifier: Modifier = Modifier) {
    val iconSize = 40.dp
    val navItemList = listOf(
        NavItem(R.drawable.ic_burger),  // index 0;
        NavItem(R.drawable.annousment), // index 1;
        NavItem(R.drawable.home),       // index 2;
    )

    var selectedIndex by remember { mutableStateOf(2) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current


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
                            selected = selectedIndex == index,
                            onClick = {
                                if (index == 0) {
                                    scope.launch { drawerState.open() }
                                }
                                else {
                                    selectedIndex = index
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
fun ContentScreen (modifier: Modifier = Modifier,selectedIndex : Int,onChangeScreen : (Int) -> Unit) {
    when(selectedIndex) {
        1 -> AnnousmentPage(onChangeScreen = onChangeScreen)
        2 -> HomePage(onChangeScreen = onChangeScreen)
    }
}






@Preview (showBackground = true, showSystemUi = true)
@Composable
private  fun Preview () {
    NavBar()
}