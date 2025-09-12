package com.example.sejongapp.AnnousmentActivity

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sejongapp.R
import com.example.sejongapp.components.ImageGalleryDialog
import com.example.sejongapp.models.DataClasses.AnnouncementData
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.primaryColor


@Composable
fun AnnousmentDetailPage(annData : AnnouncementData) {

    val text_size = 15.sp
    val scrollState = rememberScrollState()
    val context = LocalContext.current


    val images = listOf(
        R.drawable.annousment_img,
        R.drawable.annousment_img,
        R.drawable.annousment_img,
        R.drawable.annousment_img,
        R.drawable.annousment_img
    )
    var showDialog by remember { mutableStateOf(false) }



    BoxWithConstraints  (
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        val screenWidth = maxWidth
        val startPadding = screenWidth * 0.5f
        val endPadding = screenWidth * 0.5f

        Column  (
            modifier = Modifier
                .verticalScroll(scrollState)
        ){
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

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(top = 20.dp, start = 10.dp)
                        .clickable  (
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null

                        ){
                            (context as? ComponentActivity)?.finish()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = {
                        (context as? AnnousmentActivity)?.finish()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "ic_back",
                            modifier = Modifier.align(Alignment.TopStart)

                        )
                    }
                }

            }
            Text(
                text = annData.time_posted,
                modifier = Modifier
                    .padding(top = 10.dp,start = 15.dp)
            )
            Text (
                text = annData.title?: "NULL",
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
                Row (

                ) {

                    Image(
                        painter = painterResource(R.drawable.annousment_img),
                        contentDescription = "annousment_img",
                        modifier = Modifier
                            .size(100.dp) /// 350.dp
                            .padding(end = 5.dp)
                            .clickable { showDialog = true }

                    )

                    Column {

                        Image(
                            painter = painterResource(R.drawable.annousment_img),
                            contentDescription = "annousment_img",
                            modifier = Modifier
                                .size(100.dp)
                                .clickable { showDialog = true }

                        )

                        Image(
                            painter = painterResource(R.drawable.annousment_img),
                            contentDescription = "annousment_img",
                            modifier = Modifier
                                .size(100.dp)
                                .clickable { showDialog = true }

                        )
                    }


                }
                Text (
                    text = annData.content?: "NULL",
                    fontSize = text_size,

                    )

            }
        }
    }

    if (showDialog) {
        ImageGalleryDialog(images = images, onDismiss = { showDialog = false })
    }
}




//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//private fun Preview () {
//    AnnousmentDetailPage()
//}



