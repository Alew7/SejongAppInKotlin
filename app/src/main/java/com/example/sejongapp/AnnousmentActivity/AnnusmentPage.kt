package com.example.sejongapp.AnnousmentActivity

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.sejongapp.components.ImageGalleryDialog
import com.example.sejongapp.models.DataClasses.AnnouncementDateItem
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.primaryColor

@Composable
fun AnnousmentDetailPage(annData: AnnouncementDateItem) {
    val textSize = 15.sp
//    val imgSize = 120.dp
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val images = annData.images ?: emptyList()

    var showDialog by remember { mutableStateOf(false) }
    var selectedImage by remember { mutableStateOf<String?>(null) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor) // твой фон, я не трогаю
    ) {
        val columns = if (maxWidth < 400.dp) 2 else 3

        Column(
            modifier = Modifier.verticalScroll(scrollState)
        ) {
            // Верхняя панель
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
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
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

            // Дата
            Text(
                text = annData.time_posted?.substring(0, 10) ?: "NULL",
                modifier = Modifier.padding(top = 10.dp, start = 15.dp)
            )

            // Заголовок
            Text(
                text = annData.title.rus ?: "NULL",
                fontSize = 25.sp,
                modifier = Modifier.padding(start = 15.dp)
            )

            // Галерея
            if (images.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(columns),
                    modifier = Modifier
                        .padding(10.dp)
                        .heightIn(max = 400.dp)
                ) {
                    items(images) { url ->
                        Image(
                            painter = rememberImagePainter(url),
                            contentDescription = "announcment_img",
//                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(120.dp)
                                .padding(5.dp)
//                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    selectedImage = url
                                    showDialog = true
                                }
                        )
                    }
                }
            }

            // Контент
            Text(
                text = annData.content.rus ?: "NULL",
                fontSize = textSize,
                modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 15.dp)
            )
        }
    }

    // Диалог
    if (showDialog && selectedImage != null) {
        ImageGalleryDialog(images = images, onDismiss = { showDialog = false })
    }
}
