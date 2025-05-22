package com.example.sejongapp.Pages


import android.content.Intent
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
import com.example.sejongapp.ProfileActivity.ProfileActivity
import com.example.sejongapp.SpleshLoginPages.SplashLoginActivity
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.primaryColor

@Composable
fun HomePage (onChangeScreen : (Int) -> Unit) {
    val context = LocalContext.current
    val scale = remember {
        androidx.compose.animation.core.Animatable(0.2f)
    }

    LaunchedEffect  (Unit){
        scale.animateTo(
            targetValue = 1.2f,
            animationSpec = tween(durationMillis = 800)
        )
    }

    BoxWithConstraints  (
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ){
        val screenWidth = maxWidth
        val startPadding = screenWidth * 0.5f
        val endPadding = screenWidth * 0.5f


        Box (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
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
            Image (
                painter = painterResource(R.drawable.ic_hed),
                contentDescription = "ic_had",
                modifier = Modifier
                    .size(65.dp)
                    .padding(start = 25.dp)
            )
        }

        Column  (
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

        ){
            Image (
                painter = painterResource(R.drawable.icon_annousment),
                contentDescription = "icon_annousment",
                modifier = Modifier
                    .padding(end = endPadding)
                    .scale(scale.value)
                    .clickable (
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null

                    ) {
                        onChangeScreen(1)
                    }

            )
            Image (
                painter = painterResource(R.drawable.ic_library),
                contentDescription = "ic_library",
                modifier = Modifier
                    .padding(start = startPadding)
                    .scale(scale.value)

            )
            Image (
                painter = painterResource(R.drawable.ic_shhedule),
                contentDescription = "ic_shhedule",
                modifier = Modifier
                    .padding(end = endPadding)
                   .scale(scale.value)
            )
            Image (
                painter = painterResource(R.drawable.ic_profile),
                contentDescription = "ic_profile",
                modifier = Modifier
                    .padding(start = startPadding)
                    .scale(scale.value)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        val intent = Intent (context,ProfileActivity :: class.java)
                        context.startActivity(intent)
                    }

            )
        }
    }




}



@Preview (showBackground = true, showSystemUi = true)
@Composable
private fun Preview () {
    HomePage(onChangeScreen = {})
}
