package com.example.sejongapp.Activities.AnnousmentActivity

import android.graphics.Bitmap
import android.graphics.Color.parseColor
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.widget.TextView
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.sejongapp.DialogModels.ImageGalleryDialog
import com.example.sejongapp.NavBar.getLocalized
import com.example.sejongapp.R
import com.example.sejongapp.models.DataClasses.AnnouncementDateItem
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.primaryColor

@Composable
fun AnnousmentDetailPage(annData: AnnouncementDateItem) {
    val context = LocalContext.current
    val images = annData.images ?: emptyList()
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(backgroundColor)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, start = 10.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_back),
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .clickable { (context as? ComponentActivity)?.finish() }
                            .padding(10.dp)
                    )
                }
            }

            // 2. Заголовок и Дата
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
                    Text(
                        text = annData.time_posted?.substring(0, 10) ?: "",
                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = annData.title.getLocalized(context) ?: "Без названия",
                        fontFamily = FontFamily(Font(R.font.montserrat_semibold)),
                        fontSize = 26.sp,
                        lineHeight = 32.sp,
                        color = primaryColor
                    )
                }
            }

            // 3. Блок с изображениями (Улучшенный)
            if (images.isNotEmpty()) {
                item {
                    Column(modifier = Modifier.padding(vertical = 10.dp)) {
                        if (images.size == 1) {
                            // Одиночное большое фото
                            ImageCard(url = images[0]) { showDialog = true }
                        } else {
                            // Ряд из нескольких фото (вместо тяжелого Grid)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                images.take(3).forEachIndexed { index, url ->
                                    Box(modifier = Modifier.weight(1f)) {
                                        ImageCard(url = url, isSmall = true) { showDialog = true }

                                        if (images.size > 3 && index == 2) {
                                            Box(
                                                modifier = Modifier
                                                    .matchParentSize()
                                                    .background(Color.Black.copy(0.4f), RoundedCornerShape(12.dp))
                                                    .clickable { showDialog = true },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("+${images.size - 3}", color = Color.White, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 4. Текст объявления в красивой карточке
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Детали объявления",
                            fontFamily = FontFamily(Font(R.font.montserrat_semibold)),
                            fontSize = 14.sp,
                            color = primaryColor.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
//                        Text(
//                            text = annData.content.getLocalized(context) ?: "",
//                            fontFamily = FontFamily(Font(R.font.montserrat_medium)),
//                            fontSize = 16.sp,
//                            lineHeight = 26.sp,
//                            color = Color(0xFF333333)
//                        )
                        LinkifyText(
                            text = annData.content.getLocalized(context) ?: "",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        if (showDialog) {
            ImageGalleryDialog(images = images, onDismiss = { showDialog = false })
        }
    }
}

@Composable
fun ImageCard(url: String, isSmall: Boolean = false, onClick: () -> Unit) {
    val height = if (isSmall) 100.dp else 250.dp
    Image(
        painter = rememberAsyncImagePainter(url),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .padding(horizontal = if (isSmall) 0.dp else 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        contentScale = ContentScale.Crop
    )
}


@Composable
fun LinkifyText(text: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val customTypeface = remember { ResourcesCompat.getFont(context, R.font.montserrat_medium) }
    val linkColor = "#0047AB"

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            TextView(ctx).apply {
                this.typeface = customTypeface
                this.textSize = 16f
                this.setTextColor(parseColor("#333333"))
                this.setLinkTextColor(parseColor(linkColor))
                this.setLineSpacing(0f, 1.2f)


                this.autoLinkMask = Linkify.WEB_URLS
                this.linksClickable = true
                this.movementMethod = LinkMovementMethod.getInstance()


                this.setPadding(0, 0, 0, 0)
                this.text = text
            }
        },
        update = {
            it.text = text

            Linkify.addLinks(it, Linkify.WEB_URLS)
        }
    )
}

