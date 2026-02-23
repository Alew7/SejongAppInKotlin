package com.example.sejongapp.Activities.GradeBookActivity

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.sejongapp.Activities.SpleshLoginPages.TAG
import com.example.sejongapp.Activities.SpleshLoginPages.getAndSaveUserData
import com.example.sejongapp.R
import com.example.sejongapp.components.showError
import com.example.sejongapp.models.DataClasses.UserDataClasses.UserData
import com.example.sejongapp.models.DataClasses.UserDataClasses.tokenData
import com.example.sejongapp.models.ViewModels.UserViewModels.UserViewModel
import com.example.sejongapp.retrofitAPI.NetworkResponse
import com.example.sejongapp.ui.theme.WarmBeige
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.primaryColor





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginCheck(callback: (correctPassword: Boolean) -> Unit) {
    val userViewModel: UserViewModel = viewModel()
    val teacherTokenResult = userViewModel.teacherTokenResult.observeAsState()
    val context = LocalContext.current
    var passwordVisible by remember { mutableStateOf(false) }


    var isLoading = teacherTokenResult.value is NetworkResponse.Loading
    val token = LocalData.getSavedTeacherToken(context)
    val userData: UserData = LocalData.getUserData(context)

    if (token != "null") {
        callback(true)
        return;
    }

    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {

        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.TopEnd)
                .padding(top = 0.dp)
                .offset(x = 80.dp, y = (-80).dp) // Смещаем за экран для стиля
                .background(primaryColor.copy(alpha = 0.15f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Круглое лого с мягкой тенью
            Surface(
                shape = RoundedCornerShape(32.dp),
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier.size(100.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_sejong),
                    contentDescription = "Logo",
                    modifier = Modifier.padding(16.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = context.getString(R.string.Journal_Login),
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 26.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Black,
                    color = Color(0xFF2D2D2D)
                )
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Основная карточка ввода
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = cardColors(
                    containerColor = Color.White.copy(alpha = 0.9f)
                ),
                elevation = cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(context.getString(R.string.Access_Password)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = image,
                                    contentDescription = null,
                                    tint = Color.LightGray
                                )
                            }
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = primaryColor,
                            unfocusedBorderColor = Color.LightGray,
                            focusedLabelColor = Color.Black,
                            cursorColor = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (!isLoading) {
                                userViewModel.GradeBookLogin(userData.username, password)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        } else {
                            Text(context.getString(R.string.Confirm), fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                        }
                    }
                }
            }

            val composition by rememberLottieComposition(
                LottieCompositionSpec.Asset("Loading.lottie"))



            when(teacherTokenResult.value){
                is NetworkResponse.Error -> {
                    Log.e(TAG, "${(teacherTokenResult.value as NetworkResponse.Error).message}")
                    isLoading = false
                    userViewModel.resetTeacherResult()
                    password = ""
                    callback(false)
                }
                NetworkResponse.Idle -> {}
                NetworkResponse.Loading -> {
                    Box (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        LottieAnimation(
                            composition = composition,
                            iterations = LottieConstants.IterateForever,
                            modifier = Modifier.size(100.dp)

                        )
                    }
                }
                is NetworkResponse.Success -> {
                    isLoading = false
                    Log.i(TAG, "token was ${(teacherTokenResult.value as NetworkResponse.Success<tokenData>).data}")
                    LocalData.setTeacherToken(context,(teacherTokenResult.value as NetworkResponse.Success<tokenData>).data.auth_token)
                    userViewModel.resetTeacherResult()
                    callback(true)
                }
                null -> {}
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginCheckPreview(){
    LoginCheck {  }
}

