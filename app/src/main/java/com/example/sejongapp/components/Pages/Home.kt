package com.example.sejongapp.components.Pages

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import com.example.sejongapp.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sejongapp.MainActivity
import com.example.sejongapp.ProfileActivity.ProfileActivity
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.primaryColor
import com.example.sejongapp.utils.NavigationScreenEnum
import kotlinx.coroutines.delay

@Composable
fun HomePage (onChangeScreen: (NavigationScreenEnum) -> Unit) {
    
    val iconSize = 80.dp

    val context = LocalContext.current
    val scale = remember {
        androidx.compose.animation.core.Animatable(0.2f)
    }

    var isClickedOnce by remember { mutableStateOf(false) }

    LaunchedEffect(isClickedOnce) {
        Log.i(TAG, "the launch effect of isCLickedOnce is called with value $isClickedOnce")
        if (isClickedOnce){
            delay(2000)
            isClickedOnce = false
        }
    }

    BackHandler {
        if (isClickedOnce) {
            Log.i(TAG, "Now we are about to exit")
            (context as MainActivity)?.finish()
        }else{
            isClickedOnce = true
            Toast.makeText(context, context.getString(R.string.press_again_to_exit), Toast.LENGTH_SHORT).show()

        }
    }



//    Animation for scaling icons (btn icons)
    LaunchedEffect  (Unit){
        scale.animateTo(
            targetValue = 1.2f,
            animationSpec = tween(durationMillis = 800)
        )
    }


//    Whole page
    BoxWithConstraints  (
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ){
        val screenWidth = maxWidth

//      padding for icons so as they appeard from left and right of the corner
        val startPadding = screenWidth * 0.5f
        val endPadding = screenWidth * 0.5f


//       Head (with head icon and gold line)
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
//                the line
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
//            The HEAD
            Image (
                painter = painterResource(R.drawable.ic_head),
                contentDescription = "ic_head",
                modifier = Modifier
                    .size(64.dp)
                    .padding(start = 25.dp)
            )
        }

//        The body of the page (all btn icons)
        Column  (
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

        ){

//            Announcment
            Image (
                painter = painterResource(R.drawable.ic_annousment),
                contentDescription = "icon_annousment",
                modifier = Modifier
                    .padding(end = endPadding)
                    .scale(scale.value)
                    .size(iconSize)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null

                    ) {
                        onChangeScreen(NavigationScreenEnum.ANNOUNCEMENTS)
                    }

            )

//           Library
            Image (
                painter = painterResource(R.drawable.ic_library),
                contentDescription = "ic_library",
                modifier = Modifier
                    .padding(start = startPadding)
                    .scale(scale.value)
                    .size(iconSize)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null

                    ) {
                        onChangeScreen(NavigationScreenEnum.LIBRARY)
                    }
            )

//            schedule
            Image (
                painter = painterResource(R.drawable.ic_schedule),
                contentDescription = "ic_schedule",
                modifier = Modifier
                    .padding(end = endPadding)
                    .scale(scale.value)
                    .size(iconSize)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null

                    ) {
                        onChangeScreen(NavigationScreenEnum.SCHEDULE)
                    }
            )

//            profile
            Image (
                painter = painterResource(R.drawable.ic_profile),
                contentDescription = "ic_profile",
                modifier = Modifier
                    .padding(start = startPadding)
                    .scale(scale.value)
                    .size(iconSize)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        val intent = Intent(context, ProfileActivity::class.java)
                        context.startActivity(intent)
                    }

            )
        }
    }
}





@Preview (showBackground = true, showSystemUi = true)
@Composable
private fun Preview () {
    HomePage (onChangeScreen = {})
}
