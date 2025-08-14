package com.example.sejongapp.Pages

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sejongapp.R
import com.example.sejongapp.models.DataClasses.ElectronicBookData
import com.example.sejongapp.models.ViewModels.ELibraryViewModel
import com.example.sejongapp.retrofitAPI.NetworkResponse
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.cardGreyBackground
import com.example.sejongapp.ui.theme.primaryColor
import com.example.sejongapp.utils.NavigationScreenEnum

@Composable
fun ElectronicLibraryPage(onChangeScreen: (NavigationScreenEnum) -> Unit = {}){

    val eLibViewModel: ELibraryViewModel = viewModel()

    LaunchedEffect(Unit){
        eLibViewModel.getAllBooks()
    }


    Column {
        //    The header with logo icon
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
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
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = "ic_head",
                    modifier = Modifier
                        .size(64.dp)
                        .padding(start = 25.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null

                        ) {
                            onChangeScreen(NavigationScreenEnum.HOMEPAGE)
                        }
                )
            }

            /////////////////////////////////////   BODY  /////////////////////////////////////


            getAndShowData(eLibViewModel)
        }
    }
}



@Composable
fun ElectronicBooksCard(book: ElectronicBookData){

    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF5)) // light background
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // Book Image
            Image(
                painter = painterResource(id = R.drawable.ic_book), // replace with your image
                contentDescription = "Book cover",
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = book.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    Button(
                        onClick = {downloadFile(url = book.file, context)},
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD2B47C)), // gold color
                        shape = RoundedCornerShape(
                            topStart = 8.dp,
                            bottomStart = 8.dp
                        ),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        Text("Read")
                    }

                    Button(
                        onClick = {  },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD2B47C)),
                        shape = RoundedCornerShape(
                            topEnd = 8.dp,
                            bottomEnd = 8.dp
                        ),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Menu")
                    }
                }
            }
        }
    }
}


@Composable
fun getAndShowData(eLibViewModel: ELibraryViewModel){

    val Books by eLibViewModel.libResult.observeAsState()

    when (val result = Books){
        is NetworkResponse.Error -> {
            Log.d(TAG, "the book result is Error")
            Log.e(TAG, "the book result is ${result.message}")

            Text(
                text = result.message,
                color = Color.Red
            )
        }
        NetworkResponse.Idle -> TODO()
        NetworkResponse.Loading -> {
            Log.d(TAG, "the book result is Loading")

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center

            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }
        is NetworkResponse.Success -> {
            LazyColumn(
                modifier = Modifier.padding(bottom = 105.dp)
            ) {
                items(result.data.size) {
                    ElectronicBooksCard(result.data[it])
                }
            }
        }
        null -> {}
    }
}



fun downloadFile(url: String, context: Context){
    var download= context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    var PdfUri = Uri.parse(url)
    var getPdf = DownloadManager.Request(PdfUri)
    getPdf.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
    download.enqueue(getPdf)
    Toast.makeText(context,"Download Started", Toast.LENGTH_LONG).show()
}