package com.example.sejongapp.components.Pages

import LocalData
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sejongapp.Appupdate.appupdateactivity
import com.example.sejongapp.MainActivity
import com.example.sejongapp.ProfileActivity.ProfileActivity
import com.example.sejongapp.R
import com.example.sejongapp.models.DataClasses.UserDataClasses.UserData
import com.example.sejongapp.models.ViewModels.UserViewModel
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.primaryColor
import com.example.sejongapp.utils.NavigationScreenEnum
import com.example.sejongapp.utils.UserStatusEnum
import kotlinx.coroutines.delay

@Composable
fun HomePage(
    onChangeScreen: (NavigationScreenEnum) -> Unit,
    viewModel: UserViewModel
) {

    val context = LocalContext.current
    val iconSize = 80.dp

    val cardScale = remember { Animatable(0.8f) }
    val scale = remember { Animatable(0.2f) }
    var progressTarget by remember { mutableStateOf(0f) }

    var isClickedOnce by remember { mutableStateOf(false) }
    val userData: UserData = LocalData.getUserData(context)

    //  Back press logic
    BackHandler {
        if (isClickedOnce) {
            (context as MainActivity).finish()
        } else {
            isClickedOnce = true
            Toast.makeText(
                context,
                context.getString(R.string.press_again_to_exit),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    LaunchedEffect(isClickedOnce) {
        if (isClickedOnce) {
            delay(2000)
            isClickedOnce = false
        }
    }

    //  Scale animation
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1.2f,
            animationSpec = tween(800)
        )
    }

    LaunchedEffect (Unit){
        cardScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(800)
        )
    }

    val animatedprogress by animateFloatAsState(
        targetValue = progressTarget,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
        label = "progressAnimation"
    )

    val dynamicProgressColor = when{
        animatedprogress < 0.5f -> lerp(Color.Red, primaryColor,animatedprogress * 2f)
        else -> lerp(primaryColor,Color(0xFF2E7D32), (animatedprogress - 0.5f) * 2f)

    }
    LaunchedEffect(Unit) {
        progressTarget = 1.0f
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {

        //  HEADER
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_head),
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .padding(start = 25.dp)
                )

                Image(
                    painter = painterResource(R.drawable.ic_zvanik),
                    contentDescription = null,
                    modifier = Modifier
                        .size(55.dp)
                        .padding(end = 25.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            context.startActivity(
                                Intent(context, appupdateactivity::class.java)
                            )
                        }
                )
            }
        }

        //  BODY
        Column(
            modifier = Modifier.fillMaxSize().padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (userData.status == UserStatusEnum.TEACHER || userData.status == UserStatusEnum.ADMIN) {
                Spacer (modifier = Modifier.height(24.dp))
                Card(
                    modifier = Modifier
                        .size(width = 350.dp, height = 140.dp)
                        .scale(cardScale.value)
                        .clickable (
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ){
                            (onChangeScreen.invoke(NavigationScreenEnum.MAGAZINES))
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {


                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(Color(0xFFBFA353), Color(0xFFE5D192))
                                        ),
                                        shape = RoundedCornerShape(18.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = "Журнал группы",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF1A1A1A),
                                    letterSpacing = 0.5.sp
                                )

                                Spacer(modifier = Modifier.height(4.dp))


                                Text (
                                    text = "Мои группы",
                                    fontSize = 12.sp,
                                    color = Color(0xFF757575)
                                )
                            }
                        }
                        Spacer (modifier = Modifier.width(6.dp))


                        Box(contentAlignment = Alignment.Center) {
                            Image (
                                painter = painterResource(R.drawable.ic_edit),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(30.dp)
                            )

                        }
                    }
                }
            }


            if (userData.status == UserStatusEnum.STUDENT) {
                Spacer (modifier = Modifier.height(24.dp))
                Card(
                    modifier = Modifier
                        .size(width = 350.dp, height = 140.dp)
                        .scale(cardScale.value)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {


                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(Color(0xFFBFA353), Color(0xFFE5D192))
                                        ),
                                        shape = RoundedCornerShape(18.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = context.getString(R.string.Attendance),
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF1A1A1A),
                                    letterSpacing = 0.5.sp
                                )

                                Spacer(modifier = Modifier.height(4.dp))


                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        color = Color(0xFFE8F5E9),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = "12 " + context.getString(R.string.Lesson),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF2E7D32),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "0 " + context.getString(R.string.skips),
                                        fontSize = 12.sp,
                                        color = Color(0xFF757575)
                                    )
                                }
                            }
                        }
                        Spacer (modifier = Modifier.width(6.dp))


                        Box(contentAlignment = Alignment.Center) {

                            CircularProgressIndicator(
                                progress = 1f,
                                modifier = Modifier.size(68.dp),
                                color = Color(0xFFF0F0F0),
                                strokeWidth = 8.dp
                            )


                            CircularProgressIndicator(
                                progress = animatedprogress,
                                modifier = Modifier.size(68.dp),
                                color = dynamicProgressColor,
                                strokeWidth = 8.dp,
                                strokeCap = StrokeCap.Round
                            )

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${(animatedprogress * 100).toInt()}%",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = dynamicProgressColor
                                )
                            }
                        }
                    }
                }

            }
            Spacer (modifier = Modifier.height(50.dp))

            //  КОНТЕНТ
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(40.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HomeMenuItem(
                        icon = R.drawable.ic_annousment,
                        text = R.string.Announcement,
                        scale = scale.value,
                        iconSize = iconSize
                    ) { onChangeScreen(NavigationScreenEnum.ANNOUNCEMENTS) }

                    Spacer(modifier = Modifier.width(15.dp))

                    HomeMenuItem(
                        icon = R.drawable.ic_library,
                        text = R.string.ElectLib,
                        scale = scale.value,
                        iconSize = iconSize
                    ) { onChangeScreen(NavigationScreenEnum.LIBRARY) }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(40.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HomeMenuItem(
                        icon = R.drawable.ic_schedule,
                        text = R.string.schedule,
                        scale = scale.value,
                        iconSize = iconSize
                    ) { onChangeScreen(NavigationScreenEnum.SCHEDULE) }

                    Spacer(modifier = Modifier.width(15.dp))

                    HomeMenuItem(
                        icon = R.drawable.ic_profile,
                        text = R.string.Profile,
                        scale = scale.value,
                        iconSize = iconSize
                    ) {
                        context.startActivity(
                            Intent(context, ProfileActivity::class.java)
                        )
                    }

                }

//                if (userData.status == UserStatusEnum.TEACHER || userData.status == UserStatusEnum.ADMIN) {
//                    Spacer(modifier = Modifier.height(24.dp))
//                    HomeMenuItem(
//                        icon = R.drawable.ic_magazine2,
//                        text = R.string.Magazine,
//                        scale = scale.value,
//                        iconSize = 100.dp
//                    ) { onChangeScreen(NavigationScreenEnum.MAGAZINES) }
//                }


            }

            Spacer(modifier = Modifier.weight(1f)) //  низ
        }


    }
}

@Composable
fun HomeMenuItem(
    @DrawableRes icon: Int,
    @StringRes text: Int,
    scale: Float,
    iconSize: Dp,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier
                .size(iconSize)
                .scale(scale)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = stringResource(text),
            fontSize = 10.sp,
            color = primaryColor,
            modifier = Modifier.scale(scale)
        )
    }
}
