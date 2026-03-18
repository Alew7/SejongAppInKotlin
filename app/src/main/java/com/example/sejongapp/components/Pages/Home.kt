package com.example.sejongapp.components.Pages

import LocalData
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.sejongapp.Activities.AiActivity.AiActivity
import com.example.sejongapp.Activities.AppUpdate.appupdateactivity
import com.example.sejongapp.Activities.ProfileActivity.ProfileActivity
import com.example.sejongapp.MainActivity
import com.example.sejongapp.R
import com.example.sejongapp.models.DataClasses.UserDataClasses.UserData
import com.example.sejongapp.models.ViewModels.GradeBookViewModels.GroupDetailsViewModel
import com.example.sejongapp.models.ViewModels.UserViewModels.UserViewModel
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.primaryColor
import com.example.sejongapp.utils.NavigationScreenEnum
import com.example.sejongapp.utils.UserStatusEnum
import kotlinx.coroutines.delay

@Composable
fun HomePage(
    onChangeScreen: (NavigationScreenEnum) -> Unit,


) {

    val studentSkipsViewModel: GroupDetailsViewModel = viewModel()
    val allStudents by studentSkipsViewModel.students.collectAsStateWithLifecycle()
    val studentSkips by studentSkipsViewModel.studentSkips.collectAsStateWithLifecycle()
    val studentPresents by studentSkipsViewModel.studentPresents.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val userData: UserData = LocalData.getUserData(context)


    val myGroupId = remember(userData.groups) {
        val groupName = userData.groups.firstOrNull() ?: ""
        when {
            groupName.contains("4A-1") -> 17
            else -> groupName.toIntOrNull() ?: -1
        }
    }

    val skipsCount = remember(studentSkips, allStudents, userData.fullname) {
        val myName = userData.fullname.trim().lowercase()

        val myProfile = allStudents.find {
            it.student_name_en.trim().lowercase() == myName ||
                    it.student_name_tj.trim().lowercase() == myName
        }
        // Получаем ID как строку (ключ для мапы)
        val myIdKey = myProfile?.student_id?.toString() ?.trim() ?: ""
        studentSkips[myIdKey] ?: 0
    }

    val presentsCount = remember(studentPresents, allStudents, userData.fullname) {
        val myName = userData.fullname.trim().lowercase()
        val myProfile = allStudents.find {
            it.student_name_en.trim().lowercase() == myName ||
                    it.student_name_tj.trim().lowercase() == myName
        }
        val myIdKey = myProfile?.student_id?.toString() ?: ""
        studentPresents[myIdKey] ?: 0
    }


    val iconSize = 80.dp

    val cardScale = remember { Animatable(0.8f) }
    val scale = remember { Animatable(0.2f) }




    var isClickedOnce by remember { mutableStateOf(false) }



    val calculatedProgress = (1.0f - (skipsCount * 0.03f)).coerceIn(0f, 1.0f)




    var progressTarget by remember { mutableStateOf(0f) }


    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("Chatbot.lottie")
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )



    LaunchedEffect(calculatedProgress) {
        progressTarget = calculatedProgress
    }


    //  Back press logicу
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

    LaunchedEffect(myGroupId) {
        if (myGroupId != -1) {
            studentSkipsViewModel.loadGroupData(myGroupId, context)
        } else {
            Log.e("HOME_DEBUG", "ID ГРУППЫ НЕ НАЙДЕН: ${userData.groups}")
        }
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


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {

//        Image (
//            painter = painterResource(R.drawable.wtit_logo),
//            contentDescription = null,
//            modifier = Modifier
//            .size(250.dp)
//                .align(Alignment.Center)
//                .alpha(0.70f),
//            contentScale = ContentScale.Fit
//        )

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
                            (onChangeScreen(NavigationScreenEnum.MAGAZINES))
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
                                    text = context.getString(R.string.My_groups),
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF1A1A1A),
                                    letterSpacing = 0.5.sp
                                )

                                Spacer(modifier = Modifier.height(4.dp))


                                Text (
                                    text = context.getString(R.string.Group_Gradebook),
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
                Spacer(modifier = Modifier.height(24.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .scale(cardScale.value),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // ЛЕВАЯ ЧАСТЬ (Иконка + Текст)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
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
                                    letterSpacing = 0.5.sp,
                                    maxLines = 1
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.wrapContentWidth()
                                ) {
                                    Surface(
                                        color = Color(0xFFE8F5E9),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = "$presentsCount " + context.getString(R.string.Lesson),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF2E7D32),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "$skipsCount " + context.getString(R.string.skips),
                                        fontSize = 12.sp,
                                        color = Color(0xFF757575),
                                        maxLines = 1
                                    )
                                }
                            }
                        }


                        Box(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(68.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                progress = 1f,
                                modifier = Modifier.fillMaxSize(),
                                color = Color(0xFFF0F0F0),
                                strokeWidth = 7.dp
                            )

                            CircularProgressIndicator(
                                progress = animatedprogress,
                                modifier = Modifier.fillMaxSize(),
                                color = dynamicProgressColor,
                                strokeWidth = 7.dp,
                                strokeCap = StrokeCap.Round
                            )

                            Text(
                                text = "${(animatedprogress * 100).toInt()}%",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = dynamicProgressColor
                            )
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

                Spacer(modifier = Modifier.height(30.dp))

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


                Spacer (modifier = Modifier.height(20.dp))

            }

            Spacer(modifier = Modifier.weight(1f)) //  низ



        }
        Box (
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 130.dp, end = 20.dp)
                .size(100.dp)

                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    context.startActivity(
                        Intent(context, AiActivity::class.java)
                    )
                },
            contentAlignment = Alignment.Center

        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(150.dp)
            )
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
