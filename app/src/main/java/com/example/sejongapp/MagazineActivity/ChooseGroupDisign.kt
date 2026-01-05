package com.example.sejongapp.MagazineActivity

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.primaryColor
import com.example.sejongapp.utils.NavigationScreenEnum
import kotlinx.coroutines.delay

@Composable
fun ChooseGroupDesign(onChangeScreen: (NavigationScreenEnum) -> Unit = {}) {

    val groups = listOf(
        "1B-1",
        "1B-2",
        "2A-3",
        "3A-1",
        "3B-1",
        "4A"
    )

    var startAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 20.dp)
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Мои группы",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1A1A1A)
        )

        Text(
            text = "Выберите группу для ведения журнала",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 6.dp, bottom = 24.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {

            itemsIndexed(groups) { index, groupName ->

                var visible by remember { mutableStateOf(false) }

                LaunchedEffect(startAnimation) {
                    delay(index * 90L) // волна
                    visible = true
                }

                AnimatedVisibility(
                    visible = visible,
                    enter = slideInVertically(
                        initialOffsetY = { it / 2 },
                        animationSpec = tween(450)
                    ) + fadeIn(animationSpec = tween(450))
                ) {
                    GroupCard(groupName)
                }
            }
        }
    }
}

@Composable
fun GroupCard(groupName: String) {

    val context = LocalContext.current

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable {
                context.startActivity(
                    Intent(context, Magazine::class.java)
                )
            },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.elevatedCardElevation(2.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    color = primaryColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Groups,
                        contentDescription = null,
                        tint = primaryColor,
                        modifier = Modifier
                            .padding(10.dp)
                            .size(22.dp)
                    )
                }

                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.LightGray
                )
            }

            Column {
                Text(
                    text = groupName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Text(
                    text = "24 студента",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewChooseGroup() {
    ChooseGroupDesign()
}
