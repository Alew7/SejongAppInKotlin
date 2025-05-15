package com.example.sejongapp.SpleshLoginPages



import android.content.Intent
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.sejongapp.R
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sejongapp.MainActivity
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.primaryColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen () {
    val context = LocalContext.current

    var usernmae by remember {
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
                contentDescription = "ic_sejong"
            )
            Spacer(modifier = Modifier.height(35.dp))

            OutlinedTextField(value = usernmae, onValueChange = {
                usernmae = it

            }, label = {
                Text(text = "username")
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
                Text(text = "password")

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
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor,
                    contentColor = backgroundColor
                )
            ) {
                Text (
                    text = "Sign In"
                )
            }




        }
    }
}


@Preview (showSystemUi = true, showBackground = true)
@Composable
fun Preview ( ) {
    LoginScreen()
}




