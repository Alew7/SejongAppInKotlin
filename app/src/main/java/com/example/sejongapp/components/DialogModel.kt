package com.example.sejongapp.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberImagePainter
import com.example.sejongapp.R
import com.example.sejongapp.models.DataClasses.UserData
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.darkGray
import com.example.sejongapp.ui.theme.deepBlack
import com.example.sejongapp.ui.theme.primaryColor
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.draw.clip
import com.google.accompanist.pager.*
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState



@Composable
fun showError(errorMessage: String, onDismiss: () -> Unit) {
    var theMessage: String
    if (errorMessage.contains("Failed to connect")){
        theMessage = LocalContext.current.getString(R.string.Server_issue)
    }
    else{
        theMessage = LocalContext.current.getString(R.string.Error_fetching_data)
    }
    

    AlertDialog(
        onDismissRequest = { },
        containerColor = backgroundColor, // soft background
        shape = RoundedCornerShape(20.dp), // elegant rounded shape
        tonalElevation = 8.dp, // subtle shadow
        title = {
            Text(
                text = stringResource(R.string.error),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = deepBlack
            )
        },
        text = {
            Text(
                text = theMessage,
                fontSize = 16.sp,
                color = darkGray,
                lineHeight = 20.sp
            )
        },
        confirmButton = {
            Button(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .height(45.dp)
            ) {
                Text(
                    text = "OK",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserDialog(
    userData: UserData,
    onDismiss: () -> Unit,
    onSave: (UserData) -> Unit
) {
    var UsernameState by remember { mutableStateOf(userData.username) }
    var emailState by remember { mutableStateOf(userData.email) }


    AlertDialog(
        onDismissRequest = { onDismiss() },
        containerColor = backgroundColor, // same soft background
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 8.dp,
        title = {
            Text(
                text = "Edit User Info",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = deepBlack
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                OutlinedTextField(
                    value = UsernameState,
                    onValueChange = { UsernameState = it },
                    label = { Text("Username") },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = Color.Black,
                        focusedBorderColor = primaryColor,
                        focusedLabelColor = Color.Black,
                        cursorColor = Color.Black

                    )
                )

                OutlinedTextField(
                    value = emailState,
                    onValueChange = { emailState = it },
                    label = { Text("Email") },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = Color.Black,
                        focusedBorderColor = primaryColor,
                        focusedLabelColor = Color.Black,
                        cursorColor = Color.Black

                ))
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newUserData = UserData(
                        username = UsernameState,
                        email = emailState,
                        status = userData.status,
                        groups = userData.groups,
                        avatar = userData.avatar,
                        fullname = userData.fullname
                    )
                    onSave(newUserData)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(45.dp)
            ) {
                Text("Save", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = { onDismiss() },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(45.dp),

            ) {
                Text("Cancel", fontSize = 16.sp, color = Color.Black)
            }
        }
    )
}

/*
 CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
 */

@Composable
fun LoadingDialog(
    message: String = "Loading..."
) {
    AlertDialog(
        onDismissRequest = { /* Block dismiss while loading */ },
        confirmButton = {}, // no buttons
        title = null,
        text = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CircularProgressIndicator(
                    color = primaryColor,
                    strokeWidth = 3.dp
                )
                Text(
                    text = message,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = darkGray
                )
            }
        },
        containerColor = backgroundColor,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 8.dp
    )
}










@Composable
fun ImageGalleryDialog(images: List<String>, onDismiss: () -> Unit) {
    var selectedImageIndex by remember { mutableStateOf<Int?>(null) }

    // Сетка
    Dialog(onDismissRequest = { onDismiss() }) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            items(images) { url ->
                Card(
                    modifier = Modifier
                        .padding(6.dp)
                        .size(120.dp)
                        .clickable { selectedImageIndex = images.indexOf(url) },
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Image(
                        painter = rememberImagePainter(url),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }


    if (selectedImageIndex != null) {
        val pagerState = rememberPagerState(initialPage = selectedImageIndex!!)

        Dialog(
            onDismissRequest = { selectedImageIndex = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.95f))
            ) {
                // Горизонтальный Pager для свайпа картинок
                HorizontalPager(
                    count = images.size,
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    ZoomableImage(
                        url = images[page],
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.85f)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }

                // Индикатор страницы сверху
                Text(
                    text = "${pagerState.currentPage + 1} / ${images.size}",
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 24.dp)
                )

                // Кнопка закрытия
                IconButton(
                    onClick = { selectedImageIndex = null },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }


                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 24.dp)
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    images.forEachIndexed { index, url ->
                        Card(
                            shape = RoundedCornerShape(50),
                            border = if (pagerState.currentPage == index) BorderStroke(2.dp, Color.White) else null,
                            modifier = Modifier
                                .size(60.dp)
                                .clickable {
                                    selectedImageIndex = index

                                }
                        ) {
                            Image(
                                painter = rememberImagePainter(url),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
                LaunchedEffect(selectedImageIndex) {
                    selectedImageIndex?.let { pagerState.scrollToPage(it) }
                }
            }
        }
    }
}

@Composable
fun ZoomableImage(url: String, modifier: Modifier = Modifier) {
    var scale by remember { mutableStateOf(1f) }
    val state = rememberTransformableState { zoomChange, _, _ ->
        scale *= zoomChange
    }

    Image(
        painter = rememberImagePainter(url),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = modifier
            .transformable(state = state)
            .graphicsLayer(
                scaleX = maxOf(1f, scale),
                scaleY = maxOf(1f, scale)
            ),

    )
}

