package com.example.sejongapp.SpleshLoginPages

import LocalToken
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import android.content.Intent
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.sizeIn
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sejongapp.ProfileActivity.ui.theme.WarmBeige
import com.example.sejongapp.models.UserViewModel
import com.example.sejongapp.models.tokenData
import com.example.sejongapp.retrofitAPI.NetworkResponse
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.primaryColor





const val TAG = "Login_TAG"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen () {



    val userViewModel : UserViewModel = viewModel()
    val userResult = userViewModel.userResult.observeAsState(NetworkResponse.Idle)  // добавил Network.Idle
    val context = LocalContext.current
    var passwordVisible by remember { mutableStateOf(false) }

    val isLoading = userResult.value is NetworkResponse.Loading
    val isScuccess = userResult.value is NetworkResponse.Success <*>









    if (LocalToken.getSavedToken(context) != "null"){
        Log.i(TAG, "The token is ${LocalToken.getSavedToken(context)}")
        val intent = Intent (context,MainActivity :: class.java)
        context.startActivity(intent)

    }



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
                .then(Modifier.sizeIn(maxWidth = 220.dp))
            )
            Spacer(modifier = Modifier.height(20.dp))
            // 35.dp < - this is a last dp

            OutlinedTextField(value = username,  onValueChange= {
                username = it

            }, label = {
                Text(text = "username")
            },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                  focusedTextColor = Color.Black,
                  focusedBorderColor = primaryColor,
                  focusedLabelColor = Color.Black,
                  cursorColor = Color.Black

                ))

            Spacer(modifier = Modifier.height(25.dp))

            OutlinedTextField(value = password, onValueChange = {
                password = it

            },label = {
                Text(text = "password")

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
                        text = "Sign In"
                    )
                }
            }



                LaunchedEffect(userResult.value) {

                    when (userResult.value) {
                        is NetworkResponse.Error -> {
                            Log.e (TAG,"Error")
                            Toast.makeText(context,"Error, Username pr Password is oncorrect",Toast.LENGTH_SHORT).show()
                        }
                        is NetworkResponse.Success -> {
                            Log.i(TAG, "The response is successful token is abtained")

                            val token = (userResult.value as? NetworkResponse.Success<tokenData>)?.data?.token
                            if (!token.isNullOrEmpty()) {
                                Log.i(TAG, "token was $token")
                                Log.i(TAG, "user result $token")

                                val intent = Intent(context, MainActivity::class.java)
                                LocalToken.setToken(
                                    context,
                                    (userResult.value as NetworkResponse.Success<tokenData>).data.token,
                                    intent

                                )
                                userViewModel.resetUserResult()
                            }

                        }
                        else ->  {  }
                    }
                }

        }
    }
}


@Preview (showSystemUi = true, showBackground = true)
@Composable
fun Preview () {
    LoginScreen()
}
