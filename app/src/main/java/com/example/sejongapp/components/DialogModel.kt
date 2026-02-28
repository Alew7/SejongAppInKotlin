package com.example.sejongapp.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.sejongapp.R


@Composable
fun showError(errorMessage: String, onDismiss: () -> Unit) {
    val context = LocalContext.current

    val theMessage = if (errorMessage.contains("Failed to connect")) {
        context.getString(R.string.Server_issue)
    } else {
        context.getString(R.string.Error_fetching_data)
    }

    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("Error.lottie")
    )

    AlertDialog(
        onDismissRequest = { onDismiss() },
        containerColor = Color.White,
        shape = RoundedCornerShape(32.dp),
        modifier = Modifier.fillMaxWidth(0.85f),
        confirmButton = {},
        title = null,
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. Анимация
                LottieAnimation(
                    composition = composition,
                    iterations = 1,
                    modifier = Modifier.size(130.dp)
                )

                // 2. Заголовок
                Text(
                    text = stringResource(R.string.error).uppercase(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFD32F2F),
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 3. Сообщение
                Text(
                    text = theMessage,
                    fontSize = 15.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(32.dp))


                Button(
                    onClick = { onDismiss() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD32F2F),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = "OK",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    )
}


@Composable
fun LoadingDialog(
    message: String = "Loading..."
) {
    val context = LocalContext.current


    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("Loading.lottie")
    )

    AlertDialog(
        onDismissRequest = {  },
        confirmButton = {},
        containerColor = Color.White,
        shape = RoundedCornerShape(28.dp),
        modifier = Modifier.width(280.dp),
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                //  Lottie анимация
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever, // Крутится бесконечно
                    modifier = Modifier.size(120.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Текст сообщения
                Text(
                    text = message,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFBFA353),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}
@Composable
fun showSuccess(onFinished: () -> Unit) {


    val context = LocalContext.current
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("SuccessForGradebook.lottie")
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1
    )

    LaunchedEffect(progress) {
        if (progress == 1f) {
            onFinished()
        }
    }

    AlertDialog(
        onDismissRequest = { },
        confirmButton = {},
        containerColor = Color.White,
        shape = RoundedCornerShape(28.dp),
        modifier = Modifier.width(300.dp),
        properties = DialogProperties(usePlatformDefaultWidth = false),
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Анимация Lottie
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(140.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))


                Text(
                    text = context.getString(R.string.Success),
                    fontFamily = FontFamily(Font(R.font.montserrat_semibold)),
                    fontSize = 22.sp,
                    color = Color(0xFF1A1A1A),
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(8.dp))


                Text(
                    text = context.getString(R.string.Attendance_data_saved_successfully),
                    fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}