package com.example.sejongapp.DialogModels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
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
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.sejongapp.R
import com.example.sejongapp.models.DataClasses.UserDataClasses.ChangeUserInfo
import com.example.sejongapp.models.DataClasses.UserDataClasses.ChangeUserPassword
import com.example.sejongapp.models.DataClasses.UserDataClasses.UserData
import com.example.sejongapp.ui.theme.primaryColor



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
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = Color(0xFFEEEEEE),
                        cursorColor = primaryColor,
                        focusedLabelColor = primaryColor
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Новый пароль
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text(context.getString(R.string.new_password)) },
                    leadingIcon = { Icon(Icons.Default.VpnKey, contentDescription = null, tint = primaryColor) },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
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



@Composable
fun EditAvatarUser(
    userData: UserData,
    onDismiss: () -> Unit,
    onSave: (Bitmap) -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var tempAvatar by remember { mutableStateOf(userData.avatar) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            selectedUri = uri
            tempAvatar = uri.toString()
        }
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        containerColor = Color.White,
        shape = RoundedCornerShape(32.dp),
        modifier = Modifier.fillMaxWidth(0.95f),
        confirmButton = {},
        dismissButton = {},
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(40.dp, 4.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray.copy(alpha = 0.4f))
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = context.getString(R.string.Change_Avatar),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF1A1A1A),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(30.dp))


                Box(
                    modifier = Modifier
                        .size(170.dp)
                        .drawBehind {
                            drawCircle(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color(0xFFBFA353), Color(0xFFE5D192), Color(0xFFBFA353))
                                ),
                                style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }
                        .padding(10.dp)
                        .shadow(15.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            imagePickerLauncher.launch("image/*")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(tempAvatar)
                            .crossfade(true)
                            .transformations(coil.transform.CircleCropTransformation())
                            .build(),
                        contentDescription = "Avatar Preview",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    if (selectedUri == null) {
                        Box(
                            modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(35.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(35.dp))

                // Кнопка Сохранить
                Button(
                    onClick = {
                        selectedUri?.let { uri ->
                            val fixedBitmap = fixRotation(context, uri)
                            onSave(fixedBitmap)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    enabled = selectedUri != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFBFA353),
                        disabledContainerColor = Color(0xFFF0F0F0)
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = context.getString(R.string.Save),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedUri != null) Color.White else Color.Gray
                    )
                }

                // Кнопка Отмена
                TextButton(
                    onClick = { onDismiss() },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Text(
                        text = context.getString(R.string.Cancel),
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        }
    )
}


fun fixRotation(context: Context, uri: Uri): Bitmap {

    val input = context.contentResolver.openInputStream(uri)
    val bitmap = BitmapFactory.decodeStream(input)
    input?.close()

    val exifInput = context.contentResolver.openInputStream(uri)
    val exif = ExifInterface(exifInput!!)
    val orientation = exif.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL
    )
    exifInput.close()

    val matrix = Matrix()

    when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
    }

    return Bitmap.createBitmap(
        bitmap,
        0,
        0,
        bitmap.width,
        bitmap.height,
        matrix,
        true
    )
}
