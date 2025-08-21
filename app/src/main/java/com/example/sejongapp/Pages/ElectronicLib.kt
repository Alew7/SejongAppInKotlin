package com.example.sejongapp.Pages

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.sejongapp.R
import com.example.sejongapp.models.DataClasses.ElectronicBookData
import com.example.sejongapp.models.ViewModels.ELibraryViewModel
import com.example.sejongapp.retrofitAPI.NetworkResponse
import com.example.sejongapp.ui.theme.primaryColor
import com.example.sejongapp.utils.NavigationScreenEnum

var chosenBook: ElectronicBookData = ElectronicBookData("", "", "", "","","","","")

@Composable
fun ElectronicLibraryPage(onChangeScreen: (NavigationScreenEnum) -> Unit = {}){

    val eLibViewModel: ELibraryViewModel = viewModel()

    val showOneBook = remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        eLibViewModel.getAllBooks()
    }

    BackHandler {
        if (showOneBook.value){
            showOneBook.value = false
        }
        else{
            onChangeScreen(NavigationScreenEnum.HOMEPAGE)
        }
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
                            if (showOneBook.value){
                                showOneBook.value = false
                            }else{
                                onChangeScreen(NavigationScreenEnum.HOMEPAGE)
                            }
                        }
                )
            }

            /////////////////////////////////////   BODY  /////////////////////////////////////

            if (showOneBook.value){
                ShowBook(chosenBook)
            }
            else{
                getAndShowData(eLibViewModel, showOneBook)
            }
        }
    }
}



@Composable
fun ElectronicBooksCard(book: ElectronicBookData, showOneBook: MutableState<Boolean>){




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
                painter = rememberImagePainter(data = book.cover),
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


                    Button(
                        modifier = Modifier.width(100.dp).align(Alignment.End),

                        onClick = {
                            chosenBook = book
                            showOneBook.value = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD2B47C)), // gold color
                        shape = RoundedCornerShape(
                            topStart = 8.dp,
                            bottomStart = 8.dp
                        ),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        Text("Read")
                    }
            }
        }
    }
}


@Composable
fun getAndShowData(eLibViewModel: ELibraryViewModel, showOneBook: MutableState<Boolean>){

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
                    ElectronicBooksCard(result.data[it], showOneBook)
                }
            }
        }
        null -> {}
    }
}



