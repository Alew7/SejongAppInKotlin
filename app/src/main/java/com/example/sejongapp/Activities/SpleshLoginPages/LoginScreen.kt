package com.example.sejongapp.Activities.SpleshLoginPages

import LocalData
import android.content.Context
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.sejongapp.R
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sejongapp.MainActivity
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sejongapp.components.showError
import com.example.sejongapp.models.ViewModels.UserViewModels.UserViewModel
import com.example.sejongapp.models.DataClasses.UserDataClasses.tokenData
import com.example.sejongapp.retrofitAPI.NetworkResponse
import com.example.sejongapp.ui.theme.WarmBeige
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.primaryColor





const val TAG = "Login_TAG"



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen () {

    val userViewModel : UserViewModel = viewModel()
    val userTokenResult = userViewModel.userTokenResult.observeAsState()
    val context = LocalContext.current
    var passwordVisible by remember { mutableStateOf(false) }

    var isLoading = userTokenResult.value is NetworkResponse.Loading


    var username by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)

    ) {
        Column  (
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image (
                painter = painterResource(R.drawable.ic_sejong),
                contentDescription = "ic_sejong",
                modifier = Modifier
                    .size(200.dp),
                contentScale = ContentScale.Fit


            )
            Spacer(modifier = Modifier.height(35.dp))

            OutlinedTextField(value = username, onValueChange = {
                username = it

            }, label = {
                Text(text = context.getString(R.string.username))
            },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                  focusedTextColor = Color.Black,
                  focusedBorderColor = primaryColor,
                  focusedLabelColor = Color.Black,
                  cursorColor = Color.Black

                ))

            Spacer(modifier = Modifier.height(35.dp))

            OutlinedTextField(value = password, onValueChange = {
                password = it

            },label = {
                Text(text = context.getString(R.string.password))

            },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                  val image = if (passwordVisible)
                      Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = {passwordVisible = !passwordVisible}){
                        Icon (
                            imageVector = image,
                            contentDescription = description,
                            tint = WarmBeige
                        )
                    }



                },

                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = Color.Black,
                    focusedBorderColor = primaryColor,
                    focusedLabelColor = Color.Black,
                    cursorColor = Color.Black

                ))

            Spacer(modifier = Modifier.height(35.dp))



            Button (
                shape = RoundedCornerShape(10.dp),

                onClick = {
                    if (!isLoading) {
                        userViewModel.login(username, password)
                    }

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor,
                    contentColor = backgroundColor,
                    disabledContainerColor = primaryColor,
                    disabledContentColor = backgroundColor
                ),
                enabled = !isLoading
            ) {

                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier
                            .height(20.dp)
                            .size(24.dp)
                            .padding(bottom = 2.dp, start = 1.dp)
                    )
                }
                else {
                    Text (
                        text = context.getString(R.string.Log_in)
                    )
                }
            }

            when(userTokenResult.value){
                is NetworkResponse.Error -> {
                    Log.e(TAG, "${(userTokenResult.value as NetworkResponse.Error).message}")
                    isLoading = false
                    showError((userTokenResult.value as NetworkResponse.Error).toString()) {
                        userViewModel.resetUserResult()
                        username = ""
                        password = ""
                    }
                }
                NetworkResponse.Idle -> {}
                NetworkResponse.Loading -> isLoading = true
                is NetworkResponse.Success -> {
                    isLoading = false
                    Log.i(TAG, "token was ${(userTokenResult.value as NetworkResponse.Success<tokenData>).data}")

                    LocalData.setToken(context,(userTokenResult.value as NetworkResponse.Success<tokenData>).data.auth_token)

                    getAndSaveUserData(userViewModel = userViewModel, context = context)
                    userViewModel.resetUserResult()
                }
                null -> {}
            }

        }
    }

}



fun getAndSaveUserData(userViewModel: UserViewModel, context: Context) {
    userViewModel.getUserData(LocalData.getSavedToken(context))
    userViewModel.userDataResult.observeForever { result ->
        if (result is NetworkResponse.Success) {
            LocalData.setUserData(context, result.data)
            MoveToMainActivity(context)
        }
    }
}

fun MoveToMainActivity(context: Context){
    val intent = Intent (context,MainActivity :: class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intent)
}




@Preview (showSystemUi = true, showBackground = true)
@Composable
fun Preview () {
    LoginScreen()
}
