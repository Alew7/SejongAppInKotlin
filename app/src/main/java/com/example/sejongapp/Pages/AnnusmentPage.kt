package com.example.sejongapp.Pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sejongapp.R
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.primaryColor


@Composable

fun annousmentPage () {
    BoxWithConstraints  (
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        val screenWidth = maxWidth
        val startPadding = screenWidth * 0.5f
        val endPadding = screenWidth * 0.5f

        Column {
            Box(
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
                Image(
                    painter = painterResource(R.drawable.ic_hed),
                    contentDescription = "ic_had",
                    modifier = Modifier
                        .size(65.dp)
                        .align(Alignment.TopEnd)
                        .padding(end = 25.dp)
                )
                Image (
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = "ic_back",
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .clickable {

                        }

                )

            }
        }
    }
}



@Preview (showBackground = true, showSystemUi = true)
@Composable
private fun Preview () {
    annousmentPage()
}



