package com.example.sejongapp.DialogModels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.sejongapp.R
import com.example.sejongapp.models.DataClasses.UserDataClasses.ChangeUserInfo
import com.example.sejongapp.models.DataClasses.UserDataClasses.ChangeUserPassword
import com.example.sejongapp.models.DataClasses.UserDataClasses.UserData
import com.example.sejongapp.ui.theme.WarmBeige
import com.example.sejongapp.ui.theme.primaryColor
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropActivity.SCALE
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserDialog(
    userData: UserData,
    onDismiss: () -> Unit,
    onSave: (ChangeUserInfo) -> Unit
) {
    var UsernameState by remember { mutableStateOf(userData.username) }
    var emailState by remember { mutableStateOf(userData.email) }

    val context = LocalContext.current
    val isFormValid = UsernameState.isNotBlank() && emailState.isNotBlank()

    AlertDialog(
        onDismissRequest = { onDismiss() },
        containerColor = Color.White,
        shape = RoundedCornerShape(30.dp), // Солидные закругления
        modifier = Modifier.fillMaxWidth(0.95f),
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {

                Box(
                    modifier = Modifier
                        .size(40.dp, 4.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray.copy(0.4f)))

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = context.getString(R.string.Edit_profile),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF1A1A1A)
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {

                Spacer(modifier = Modifier.height(10.dp))

                // Поле Username с иконкой
                OutlinedTextField(
                    value = UsernameState,
                    onValueChange = { UsernameState = it },
                    label = { Text("Username") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = primaryColor) },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = Color(0xFFEEEEEE),
                        cursorColor = primaryColor,
                        focusedLabelColor = primaryColor
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Поле Email с иконкой
                OutlinedTextField(
                    value = emailState,
                    onValueChange = { emailState = it },
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = primaryColor) },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = Color(0xFFEEEEEE),
                        cursorColor = primaryColor,
                        focusedLabelColor = primaryColor
                    )
                )
            }
        },
        confirmButton = {

            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)) {
                Button(
                    onClick = {
                        val newUserData = ChangeUserInfo(
                            username = UsernameState,
                            email = emailState,
                        )
                        onSave(newUserData)
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    enabled = isFormValid,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryColor,
                        disabledContainerColor = Color(0xFFF5F5F5)
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = context.getString(R.string.Save),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isFormValid) Color.White else Color.Gray
                    )
                }

                TextButton(
                    onClick = { onDismiss() },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Text(
                        text = context.getString(R.string.Cancel),
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        dismissButton = {}
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

    var passwordVisibilityForOldPassword by remember { mutableStateOf(false) }
    var passwordVisibilityForNewPassword by remember { mutableStateOf(false) }


    val isFormValid = oldPassword.isNotBlank() && newPassword.isNotBlank()
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { onDismiss() },
        containerColor = Color.White,
        shape = RoundedCornerShape(30.dp),
        modifier = Modifier.fillMaxWidth(0.95f),
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {

                Box(
                    modifier = Modifier
                        .size(40.dp, 4.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray.copy(0.4f)))

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = context.getString(R.string.Change_password),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF1A1A1A)
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {

                Spacer(modifier = Modifier.height(10.dp))

                // Старый пароль
                OutlinedTextField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    label = { Text(context.getString(R.string.old_password)) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = primaryColor) },
                    trailingIcon = {
                        val icon = if (passwordVisibilityForOldPassword) {
                            Icons.Filled.Visibility
                        }
                        else {
                            Icons.Filled.VisibilityOff

                        }
                        IconButton( onClick = {passwordVisibilityForOldPassword = !passwordVisibilityForOldPassword} ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = WarmBeige

                            )
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    visualTransformation = if (passwordVisibilityForOldPassword) VisualTransformation.None else PasswordVisualTransformation() ,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = Color(0xFFEEEEEE),
                        cursorColor = primaryColor,
                        focusedLabelColor = primaryColor,
                        containerColor = Color.White

                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Новый пароль
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text(context.getString(R.string.new_password)) },
                    leadingIcon = { Icon(Icons.Default.VpnKey, contentDescription = null, tint = primaryColor) },
                    trailingIcon = {
                        val icon = if (passwordVisibilityForNewPassword) {
                            Icons.Filled.Visibility
                        }
                        else {
                            Icons.Filled.VisibilityOff
                        }
                        IconButton( onClick = {passwordVisibilityForNewPassword = !passwordVisibilityForNewPassword} ) {
                            Icon (
                                imageVector = icon,
                                contentDescription = null,
                                tint = WarmBeige

                            )
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    visualTransformation = if (passwordVisibilityForNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = Color(0xFFEEEEEE),
                        cursorColor = primaryColor,
                        focusedLabelColor = primaryColor,
                        containerColor = Color.White

                    )
                )
            }
        },
        confirmButton = {

            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)) {
                Button(
                    onClick = {
                        val theChangedPassword = ChangeUserPassword(
                            check_password = oldPassword,
                            password = newPassword
                        )
                        onSave(theChangedPassword)
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    enabled = isFormValid,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryColor,
                        disabledContainerColor = Color(0xFFF5F5F5)
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = context.getString(R.string.Save),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isFormValid) Color.White else Color.Gray
                    )
                }

                TextButton(
                    onClick = { onDismiss() },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Text(
                        text = context.getString(R.string.Cancel),
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        dismissButton = {}
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAvatarUser(
    userData: UserData,
    onDismiss: () -> Unit,
    onSave: (Bitmap) -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Храним Uri выбранного изображения
    var selectedUri by remember { mutableStateOf<Uri?>(null) }

    // Модель для AsyncImage: если выбрали новое — показываем его, если нет — старый аватар
    val avatarModel = remember(selectedUri) {
        selectedUri ?: userData.avatar
    }

    val cropLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val resultUri = UCrop.getOutput(result.data!!)
            selectedUri = resultUri // Обновляем Uri, и remember(selectedUri) сработает
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { sourceUri ->
            val destinationUri = Uri.fromFile(File(context.cacheDir, "cropped_image_${System.currentTimeMillis()}.jpg"))

            val options = UCrop.Options().apply {
                setCircleDimmedLayer(true)
                setShowCropGrid(false)
                setShowCropFrame(false)
                setToolbarColor(android.graphics.Color.WHITE)
                setToolbarWidgetColor(android.graphics.Color.BLACK)
                setActiveControlsWidgetColor(0xFFBFA353.toInt()) // Твой золотистый
            }

            val uCrop = UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
                .withAspectRatio(1f, 1f)
                .withMaxResultSize(1000, 1000)

            cropLauncher.launch(uCrop.getIntent(context))
        }
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        modifier = Modifier.fillMaxWidth(0.95f),
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(32.dp),
            color = Color.White, // Чистый белый фон как в новом дизайне
            tonalElevation = 8.dp
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Декоративная полоска сверху
                Box(
                    modifier = Modifier
                        .size(40.dp, 4.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray.copy(alpha = 0.4f))
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = context.getString(R.string.Change_Avatar),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333) // Мягкий черный
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Круглый аватар с золотой обводкой
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .padding(8.dp)
                        .drawBehind {
                            drawCircle(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color(0xFFBFA353), Color(0xFFE5D192))
                                ),
                                style = Stroke(width = 4.dp.toPx())
                            )
                        }
                        .shadow(10.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            imagePickerLauncher.launch("image/*")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(avatarModel)
                            .crossfade(true)
                            .memoryCachePolicy(CachePolicy.DISABLED) // Отключаем кэш для мгновенного обновления
                            .build(),
                        contentDescription = "Avatar",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Показываем иконку редактирования только если фото еще не выбрано
                    if (selectedUri == null) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Кнопка Сохранить
                Button(
                    onClick = {
                        selectedUri?.let { uri ->
                            try {
                                val inputStream = context.contentResolver.openInputStream(uri)
                                val bitmap = BitmapFactory.decodeStream(inputStream)
                                inputStream?.close()
                                if (bitmap != null) onSave(bitmap)
                            } catch (e: Exception) {
                                Log.e("AVATAR", "Error: ${e.message}")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    enabled = selectedUri != null, // Активна только если есть изменения
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFBFA353),
                        disabledContainerColor = Color(0xFFF5F5F5),
                        disabledContentColor = Color.LightGray
                    )
                ) {
                    Text(text = context.getString(R.string.Save), fontWeight = FontWeight.Bold)
                }

                TextButton(
                    onClick = { onDismiss() },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Text(text = context.getString(R.string.Cancel), color = Color.Gray)
                }
            }
        }
    }
}


