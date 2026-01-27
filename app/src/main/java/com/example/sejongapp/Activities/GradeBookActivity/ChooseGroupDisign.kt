package com.example.sejongapp.Activities.GradeBookActivity

import android.content.Intent
import androidx.activity.compose.BackHandler
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sejongapp.R
import com.example.sejongapp.models.DataClasses.StudentGroups.Group
import com.example.sejongapp.models.ViewModels.GradeBookViewModels.GroupsViewModel
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.primaryColor
import com.example.sejongapp.utils.NavigationScreenEnum
import com.example.sejongapp.utils.UserStatusEnum
import kotlinx.coroutines.delay

@Composable
fun ChooseGroupDesign(
    onChangeScreen: (NavigationScreenEnum) -> Unit = {},
    viewModel: GroupsViewModel = viewModel(),

) {
    BackHandler {
        onChangeScreen(NavigationScreenEnum.HOMEPAGE)
    }
    val groups by viewModel.groups.collectAsStateWithLifecycle()
    val userData = LocalData.getUserData(LocalContext.current)
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isAmin = userData.status == UserStatusEnum.ADMIN

    var startAnimation by remember { mutableStateOf(false) }
    val context = LocalContext.current


    LaunchedEffect (Unit) {
        viewModel.loadMyGroups(context)
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
            text = context.getString(R.string.My_groups),
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1A1A1A)
        )

        Text(
            text = context.getString(R.string.Select_a_group_for_logging),
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 6.dp, bottom = 24.dp)
        )


        if (isLoading && groups.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = primaryColor)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {

                itemsIndexed(groups) { index, group ->


                    var visible by remember { mutableStateOf(false) }

                    LaunchedEffect(startAnimation) {
                        delay(index * 90L)
                        visible = true
                    }

                    AnimatedVisibility(
                        visible = visible,
                        enter = slideInVertically(
                            initialOffsetY = { it / 2 },
                            animationSpec = tween(450)
                        ) + fadeIn(animationSpec = tween(450))
                    ) {

                        GroupCard(group = group, isAdmin = isAmin)
                    }
                }
            }
        }
    }
}

@Composable
fun GroupCard(group: Group, isAdmin: Boolean) {
    val context = LocalContext.current

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable {
                val intent = Intent(context, GradeBookActivity::class.java).apply {
                    putExtra("GROUP_ID", group.id)
                    putExtra("GROUP_NAME", group.name)
                }
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
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
                    text = group.name ,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333),
                    maxLines = 2

                )
                if (isAdmin) {
                    Column (
                        modifier = Modifier.padding(top = 4.dp)

                    ) {
                        Text(
                            text = "Преподователь: ${group.teacher_name_kr}",
                            fontSize = 12.sp,
                            color = primaryColor,
                            maxLines = 1

                        )
                    }

                }
                else {
                      Text(
                         text = context.getString(R.string.Group_Gradebook),
                         fontSize = 12.sp,
                         color = Color.Gray,
                         modifier = Modifier.padding(top = 4.dp)
                      )

                }
            }
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun PreviewChooseGroup() {
//    ChooseGroupDesign()
//}
