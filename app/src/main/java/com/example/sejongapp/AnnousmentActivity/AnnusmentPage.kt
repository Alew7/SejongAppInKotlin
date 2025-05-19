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
                    text = "In the heart of Dushanbe, the Dushanbe 3 Sejong\nInstitute stands as a vibrant center for the promotion\nof Korean language and culture, fostering a deeper\nunderstanding between Tajikistan and South Korea.\nSince its establishment,the instite has witnessed\n a remarkable surge in interest, with students of all ages\neagerto explore the rich tapestry of Korean\ntraditions.\n   The institute's comprehensive curreculum extends\nbeyond basic language instruction, offering a diverse\nrange of programs that delve  into the intricaries of\nKorean history, art, and contemporary culture.From\ntraditional calligraphy and cooking classes to modern\nK-pop dance workshops,the Sejong Institute\nprovides a holistic cultural experience."
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



