package com.example.sejongapp.DialogModels

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.sejongapp.R
import com.example.sejongapp.models.DataClasses.UserDataClasses.ChangeUserInfo
import com.example.sejongapp.models.DataClasses.UserDataClasses.ChangeUserPassword
import com.example.sejongapp.models.DataClasses.UserDataClasses.UserData
import com.example.sejongapp.models.ViewModels.UserViewModels.UserViewModel
import com.example.sejongapp.retrofitAPI.NetworkResponse
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.deepBlack
import com.example.sejongapp.ui.theme.primaryColor


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
                                    cursorColor = Color.Black,
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
                                    cursorColor = Color.Black,
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

    val userViewModel : UserViewModel = viewModel ()
    val userAvatarResult = userViewModel.userAvatarResult.observeAsState()

    var isLoading = userAvatarResult.value is NetworkResponse.Loading

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
                                label = { Text(context.getString(R.string.old_password))},
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                visualTransformation = PasswordVisualTransformation(),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedTextColor = Color.Black,
                                    cursorColor = Color.Black,
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
                                label = { Text(context.getString(R.string.new_password)) },
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                visualTransformation = PasswordVisualTransformation(),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedTextColor = Color.Black,
                                    cursorColor = Color.Black,
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
    onSave: (Uri) -> Unit
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
        title = { Text(text = context.getString(R.string.Change_Avatar))},
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
                    Text(text = context.getString(R.string.Choose_new_avatar))
                }
            }
        },
        confirmButton = {
            Button(

                onClick = {
                    selectedUri?.let { uri ->
                        onSave(uri)
                    } ?: Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text (text = context.getString(R.string.Save))
            }


        },
        dismissButton = {
            OutlinedButton(onClick = { onDismiss() }, shape = RoundedCornerShape(12.dp)) {
                Text(context.getString(R.string.Cancel), color = Color.Black)
            }

        }
    )
}