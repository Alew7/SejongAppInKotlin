package com.example.sejongapp.NavBar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sejongapp.Pages.AnnousmentPage
import com.example.sejongapp.Pages.HomePage
import com.example.sejongapp.Pages.librarypage
import com.example.sejongapp.Pages.test
import com.example.sejongapp.R
import com.example.sejongapp.ui.theme.WarmBeige

import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.primaryColor

@Composable
fun NavBar(modifier: Modifier = Modifier) {
    val navItemList = listOf(
        NavItem(R.drawable.home),
        NavItem(R.drawable.annousment),
        NavItem(R.drawable.sexata),
    )

    var selectedIndex by remember { mutableStateOf(0) }
    var isDrawerOpen by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
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
                    navItemList.forEachIndexed { index, navItem ->
                        NavigationBarItem(
                            selected = selectedIndex == index,
                            onClick = { selectedIndex = index },
                            icon = {
                                Icon(
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
            ContentScreen(modifier = Modifier.padding(innerPadding), selectedIndex = selectedIndex, onChangeScreen = {newIndex -> selectedIndex = newIndex})

        }


    }
}

@Composable
fun ContentScreen (modifier: Modifier = Modifier,selectedIndex : Int,onChangeScreen : (Int) -> Unit) {
    when(selectedIndex) {
        0 -> HomePage(onChangeScreen)
        1 -> AnnousmentPage()
        2 -> librarypage()

        else -> Box (modifier)
    }
}




@Preview (showBackground = true, showSystemUi = true)
@Composable
private  fun Preview () {
    NavBar()
}