package com.example.sejongapp.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import com.example.sejongapp.models.DataClasses.ChangeUserData
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
    onSave: (ChangeUserData) -> Unit
) {
    var UsernameState by remember { mutableStateOf(userData.username) }
    var emailState by remember { mutableStateOf(userData.email) }
    val passwordChangeChecked = remember { mutableStateOf(false) }
    var oldpassword by  remember { mutableStateOf("") }
    var newpassword by remember { mutableStateOf("") }

    var isUserInfoExpanded by remember { mutableStateOf(false) }
    var isPasswordInfoExpanded by remember { mutableStateOf(false)}
    val context = LocalContext.current



    AlertDialog(
        onDismissRequest = { onDismiss() },
        containerColor = backgroundColor,
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 8.dp,
        title = {
            Text(
                text = context.getString(R.string.Edit_profile),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = deepBlack
            )
        },
        text = {
            Column(
            ) {

                Card (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding (vertical = 8.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
//                        modifier = Modifier.animateContentSize( animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                                ) {
                                    isUserInfoExpanded = !isUserInfoExpanded
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                modifier = Modifier.size(28.dp)
                            )

                            Text(
                                text = context.getString(R.string.Edit_profile),
                                modifier = Modifier.padding(start = 12.dp)
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Icon(
                                imageVector = if (isUserInfoExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Toggle Edit User",
                                modifier = Modifier.size(24.dp)
                            )
                        }


                        if (isUserInfoExpanded) {

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
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
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
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
//                            .animateContentSize(animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    isPasswordInfoExpanded = !isPasswordInfoExpanded
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Key, contentDescription = "Password")
                            Text(text = context.getString(R.string.Edit_User_Password), modifier = Modifier.padding(start = 12.dp))
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                imageVector = if (isPasswordInfoExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null
                            )
                        }

                        if (isPasswordInfoExpanded) {
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = oldpassword,
                                onValueChange = { oldpassword = it },
                                label = { Text("Old Password") },
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedTextColor = Color.Black,
                                    focusedBorderColor = primaryColor,
                                    focusedLabelColor = Color.Black,
                                )
                            )

                            OutlinedTextField(
                                value = newpassword,
                                onValueChange = { newpassword = it },
                                label = { Text("New Password") },
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedTextColor = Color.Black,
                                    focusedBorderColor = primaryColor,
                                    focusedLabelColor = Color.Black,
                                )
                            )
                        }
                    }
                }


            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newUserData = ChangeUserData(
                        username = UsernameState,
                        email = emailState,
                        check_password = newpassword,
                        password = oldpassword,
                        phone_number = ""
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
                Text(context.getString(R.string.Save), fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = { onDismiss() },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(45.dp),

                ) {
                Text(context.getString(R.string.Cancel), fontSize = 16.sp, color = Color.Black)
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
fun ImageGalleryDialog(
    images: List<String>,
    startIndex: Int = 0,
    onDismiss: () -> Unit
) {
    var selectedImageIndex by remember { mutableStateOf(startIndex) }
    val pagerState = rememberPagerState(initialPage = selectedImageIndex)

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.95f))
        ) {

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


            Text(
                text = "${pagerState.currentPage + 1} / ${images.size}",
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 24.dp)
            )


            IconButton(
                onClick = { onDismiss() },
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
                pagerState.scrollToPage(selectedImageIndex)
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
            )
    )
}

