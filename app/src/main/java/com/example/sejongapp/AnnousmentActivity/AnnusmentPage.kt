package com.example.sejongapp.AnnousmentActivity

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sejongapp.R
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.primaryColor


@Composable

fun AnnousmentDetailPage () {
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
                    .padding(top = 25.dp)
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
                Image(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = "ic_back",
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .clickable {

                    }

                )
            }
            Text(
                text = "11.02.2025",
                modifier = Modifier
                    .padding(top = 10.dp,start = 15.dp)
            )
            Text (
                text = "Dushanbe 3 Sejong Institute: A\nHub of Cultural Exchange and\nLanguage Mastery",
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                modifier = Modifier
                    .padding(start = 15.dp)
            )

            Column (
                modifier = Modifier
                    .padding(bottom = 40.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Image(
                    painter = painterResource(R.drawable.annousment_img),
                    contentDescription = "annousment_img",
                    modifier = Modifier
                        .size(350.dp)

                )
                Text (
                    text = "   In the heart of Dushanbe, the Dushanbe 3 Sejong Institute stands as a vibrant center for the promotion of Korean language and culture, fostering a deeper understanding between Tajikistan and South Korea. Since its establishment, the institute has witnessed a remarkable surge in interest, with students of all ages eager to explore the rich tapestry of Korean traditions.  \n" +
                            "   The institute's comprehensive curriculum extends beyond basic language instruction, offering a diverse range of programs that delve into the intricacies of Korean history, art, and contemporary culture. From traditional calligraphy and cooking classes to modern K-pop dance workshops, the Sejong Institute provides a holistic cultural experience.\n" +
                            "  "
                )

            }
        }
    }
}



@Preview (showBackground = true, showSystemUi = true)
@Composable
private fun Preview () {
    AnnousmentDetailPage()
}



