package com.example.sejongapp.components

import LocalData
import LocalData.getUserData
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.contentReceiver
import androidx.compose.foundation.gestures.detectTapGestures
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
import com.example.sejongapp.models.DataClasses.UserDataClasses.UserData
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.darkGray
import com.example.sejongapp.ui.theme.deepBlack
import com.example.sejongapp.ui.theme.primaryColor
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.sejongapp.models.DataClasses.UserDataClasses.ChangeUserInfo
import com.example.sejongapp.models.DataClasses.UserDataClasses.ChangeUserPassword
import com.example.sejongapp.models.DataClasses.UserDataClasses.tokenData
import com.example.sejongapp.models.ViewModels.UserViewModel
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import java.io.ByteArrayOutputStream


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
    onSave: (ChangeUserInfo) -> Unit
) {
    var UsernameState by remember { mutableStateOf(userData.username) } /// userData.username
    var emailState by remember { mutableStateOf(userData.email)}  /// userData.email
    var isUserInfoExpanded by remember { mutableStateOf(false)}

    val context = LocalContext.current
    val isFormValid = UsernameState.isNotBlank() && emailState.isNotBlank()




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
                    shape = RoundedCornerShape(15.dp),
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
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newUserData = ChangeUserInfo(
                        username = UsernameState,
                        email = emailState,
                    )
                    onSave(newUserData)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFormValid) primaryColor else Color.Red,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(45.dp),
                enabled = isFormValid
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserPasswordDialog(
    onDismiss: () -> Unit,
    onSave: (ChangeUserPassword) -> Unit
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var isPasswordInfoExpanded by remember { mutableStateOf(false)}

    val isFormValid = oldPassword.isNotBlank() && newPassword.isNotBlank()

    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { onDismiss() },
        containerColor = backgroundColor,
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 8.dp,
        title = {
            Text(
                text = "Изменить пароль",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = deepBlack
            )
        },
        text = {
            Column {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    isPasswordInfoExpanded = !isPasswordInfoExpanded
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password",
                                modifier = Modifier.size(28.dp)
                            )

                            Text(
                                text = context.getString(R.string.Change_password),
                                modifier = Modifier.padding(start = 12.dp)
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Icon(
                                imageVector = if (isPasswordInfoExpanded)
                                    Icons.Default.KeyboardArrowUp
                                else
                                    Icons.Default.KeyboardArrowDown,
                                contentDescription = "Toggle Password Edit",
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        if (isPasswordInfoExpanded) {

                            OutlinedTextField(
                                value = oldPassword,
                                onValueChange = { oldPassword = it },
                                label = { Text("Старый пароль") },
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                visualTransformation = PasswordVisualTransformation(),
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
                                value = newPassword,
                                onValueChange = { newPassword = it },
                                label = { Text("Новый пароль") },
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                visualTransformation = PasswordVisualTransformation(),
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
            }
        },
        confirmButton = {
            Button(
                onClick = {
                        var TheChangedPassword =  ChangeUserPassword(
                            check_password = oldPassword,
                            password = newPassword
                        )
                        onSave(TheChangedPassword)
                          },
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(45.dp),
                enabled = isFormValid
            ) {
                Text(context.getString(R.string.Save), fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = { onDismiss() },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(45.dp)
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
fun EditAvatarUser(
    userData: UserData,
    onDismiss: () -> Unit,
    viewModel: UserViewModel
) {
    val context = LocalContext.current
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var tempAvatar by remember { mutableStateOf(userData.avatar) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            selectedUri = uri
            tempAvatar = uri.toString()
        }
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        containerColor = backgroundColor,
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 8.dp,
        title = { Text("Change Avatar") },
        text = {
            Column {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = rememberImagePainter(tempAvatar),
                        contentDescription = "userAvatar",
                        modifier = Modifier.size(100.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Choose new avatar")
                }
            }
        },
        confirmButton = {
            Button(

                onClick = {
                    var token = LocalData.getSavedToken(context)
                    selectedUri?.let { uri ->

                        viewModel.changeUserAvatar(context,token, uri)
                    } ?: Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = { onDismiss() }) {
                Text("Cancel", color = Color.Black)
            }
        }
    )
}







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

            // Основное изображение с зумом
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
                        .clip(RoundedCornerShape(16.dp))
                )
            }

            // Текст с номером страницы
            Text(
                text = "${pagerState.currentPage + 1} / ${images.size}",
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 28.dp)
            )

            // Кнопка закрытия
            IconButton(
                onClick = { onDismiss() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }

            // Миниатюры снизу — теперь квадратные и скроллятся
            LazyRow(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                itemsIndexed(images) { index, url ->
                    Card(
                        shape = RoundedCornerShape(8.dp), // квадратная форма
                        border = if (pagerState.currentPage == index)
                            BorderStroke(2.dp, Color.White)
                        else null,
                        modifier = Modifier
                            .size(80.dp)
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
fun ZoomableImage(
    url: String,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val state = rememberTransformableState { zoomChange, panChange, _ ->
        scale = (scale * zoomChange).coerceIn(1f, 5f) // зум от 1x до 5x
        offset += panChange // движение
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .clip(RoundedCornerShape(12.dp))
            .transformable(state = state)
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        // двойной тап — сброс масштаба
                        if (scale > 1f) {
                            scale = 1f
                            offset = Offset.Zero
                        } else {
                            scale = 2f
                        }
                    }
                )
            }
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offset.x,
                translationY = offset.y
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberImagePainter(url),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}

