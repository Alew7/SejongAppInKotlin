package com.example.sejongapp.Pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sejongapp.R
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.primaryColor

@Composable
fun AnnousmentPage () {



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
                    .padding(start = 25.dp)
            )


        }
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .clickable {

                }
                .size(150.dp)
                .background(backgroundColor).padding(top = 45.dp),
            shape = RoundedCornerShape(15.dp),
            elevation = CardDefaults.cardElevation(20.dp)

        ) {
            Row () {
                Image (
                    painter = painterResource(R.drawable.annousment_img),
                    contentDescription = "annousment_img",
                    modifier = Modifier
                        .padding(top = 15.dp)
                )
            Column {

                Text (
                    text = "Announcment title",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 5.dp)


                )
                Text (
                    text = "part of content\nLorem ipsum dolor sit amet,\nconsenctetur adiposcing elit...",
                    modifier = Modifier
                        .padding(start = 5.dp, top = 5.dp)

                )
                Text (
                    text = "11.02.2025",
                    modifier = Modifier
                        .padding(start = 210.dp)

                )
            }


            }




        }
    }

    }

}




@Preview (showBackground = true, showSystemUi = true)
@Composable
private fun Preview () {
    AnnousmentPage()
}