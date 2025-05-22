package com.example.sejongapp.Pages


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sejongapp.SpleshLoginPages.SplashLoginActivity
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.secondaryColor


@Composable
fun test () {

    val context = LocalContext.current

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {

    }
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Button(
            shape = RoundedCornerShape(10.dp),
            onClick = {

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = secondaryColor
            )

        ) {
            Text (
                text = "Log out"
            )

        }

    }
}



@Preview (showBackground = true, showSystemUi = true)
@Composable
private fun Preview () {
    test()
}

