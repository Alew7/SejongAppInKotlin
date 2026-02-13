package com.example.sejongapp.components


import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sejongapp.R
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.darkGray
import com.example.sejongapp.ui.theme.deepBlack
import com.example.sejongapp.ui.theme.primaryColor

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
                    strokeWidth = 3.dp,
                    modifier = Modifier
                        .padding(top = 5.dp)

                )
                Text(
                    text = message,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = darkGray,
                    modifier = Modifier.padding(start = 15.dp,top = 5.dp)
                )
            }
        },
        containerColor = backgroundColor,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 8.dp
    )
}







