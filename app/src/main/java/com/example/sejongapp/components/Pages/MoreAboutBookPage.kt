package com.example.sejongapp.components.Pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.ModifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.sejongapp.NavBar.getLocalized
import com.example.sejongapp.models.DataClasses.ElectronicBookData
import com.example.sejongapp.models.ViewModels.UserViewModels.DownloadViewModel



@Composable
fun ShowBook(book: ElectronicBookData, dowloadVM: DownloadViewModel){

    val context = LocalContext.current
    val bookUrl = book.file
    val fileName = "${book.title.getLocalized(context)}.pdf"

    LaunchedEffect (bookUrl) {
        dowloadVM.restoreDownload(context, bookUrl,fileName)
    }
    val isDownloaded = dowloadVM.isDownloadedMap[bookUrl] ?: false
    val isDownloading = dowloadVM.isDownloadingMap[bookUrl] ?: false
    val progress = dowloadVM.progressMap[bookUrl] ?: 0


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF5)), // light background
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Book Title
            Text(
                text = book.title.getLocalized(context),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Book Image
            Image(
                painter =  rememberImagePainter(data = book.cover),
                contentDescription = "Book cover",
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .aspectRatio(0.7f)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(
                text = book.description.getLocalized(context),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))



            Button(
                onClick = {
                    if (isDownloaded) {
                        // Если файл уже есть — открываем его
                        dowloadVM.openFile(context, fileName)
                    } else {
                        // Если файла нет — начинаем загрузку
                        dowloadVM.startDownload(context, bookUrl, fileName)
                    }
                },

                // Кнопка активна всегда, кроме момента самой загрузки
                enabled = !isDownloading,
                modifier = Modifier
                .padding(horizontal = 32.dp)
                .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDownloaded) Color(0xFF4CAF50) else Color(0xFFD2B47C)
                )
            ) {
                Text(
                    when {
                        isDownloaded -> "Открыть"
                        isDownloading -> "Загрузка $progress%"
                        else -> "Скачать"
                    },
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium

                )
            }

            Spacer (modifier = Modifier.height(80.dp))

        }
    }
}






