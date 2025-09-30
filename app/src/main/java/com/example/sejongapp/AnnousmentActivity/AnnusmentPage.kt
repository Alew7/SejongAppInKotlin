package com.example.sejongapp.AnnousmentActivity

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.sejongapp.NavBar.getLocalized
import com.example.sejongapp.R
import com.example.sejongapp.components.ImageGalleryDialog
import com.example.sejongapp.models.DataClasses.AnnouncementDateItem
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.primaryColor

@Composable
fun AnnousmentDetailPage(annData: AnnouncementDateItem) {
    val textSize = 15.sp
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val images = annData.images ?: emptyList()

    var showDialog by remember { mutableStateOf(false) }
    var selectedImage by remember { mutableStateOf<String?>(null) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),

        ) {
        val columns = if (maxWidth < 400.dp) 2 else 3

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
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
            }

            item {
                // Дата
                Text(
                    text = annData.time_posted?.substring(0, 10) ?: "NULL",
                    fontFamily = FontFamily(Font(R.font.variablefont_wght)),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 10.dp, start = 15.dp)
                )
            }

            item {
                // Заголовок
                Text(
                    text = annData.title.getLocalized(context) ?: "NULL",
                    fontFamily = FontFamily(Font(R.font.variablefont_wght)),
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    modifier = Modifier.padding(start = 15.dp)
                )
            }

            if (images.isNotEmpty()) {
                val maxVisible = if (images.size > 3) 3 else images.size
                item {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(columns),
                        modifier = Modifier
                            .padding(10.dp)
                            .heightIn(max = 400.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(maxVisible) { index ->
                            val url = images[index]

                            Card(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .size(120.dp)
                                    .clickable {
                                        selectedImage = url
                                        showDialog = true
                                    },
                                shape = RoundedCornerShape(12.dp),
                                colors = androidx.compose.material3.CardDefaults.cardColors(
                                    containerColor = androidx.compose.ui.graphics.Color.White
                                ),
                                elevation = androidx.compose.material3.CardDefaults.cardElevation(4.dp)
                            ) {
                                Box {
                                    Image(
                                        painter = rememberImagePainter(url),
                                        contentDescription = "announcement_img",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(12.dp)),
                                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                    )

                                    if (images.size > 3 && index == 2) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    androidx.compose.ui.graphics.Color.Black
                                                        .copy(alpha = 0.5f),
                                                    shape = RoundedCornerShape(12.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "+${images.size - 3}",
                                                color = androidx.compose.ui.graphics.Color.White,
                                                fontSize = 24.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                // Контент (длинный текст)
                Text(
                    text = annData.content.getLocalized(context) ?: "NULL",
                    fontFamily = FontFamily(Font(R.font.variablefont_wght)),
                    fontWeight = FontWeight.Bold,
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
}

