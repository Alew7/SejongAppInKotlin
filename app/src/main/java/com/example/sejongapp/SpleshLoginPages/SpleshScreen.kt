package com.example.sejongapp.SpleshLoginPages


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.sejongapp.R
import com.example.sejongapp.ui.theme.backgroundColor


@Composable
fun SpleshScreen () {

    val scale  = remember { Animatable(0.2f)}
    LaunchedEffect  (Unit){
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800)
        )
    }


    Box (
        modifier = Modifier
            .fillMaxSize().background(backgroundColor)
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth().
                fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Image (
                painter = painterResource(R.drawable.ic_sejong),
                contentDescription = "ic_sejong",
                modifier = Modifier
                    .scale(scale.value)


            )
        }
    }

}

