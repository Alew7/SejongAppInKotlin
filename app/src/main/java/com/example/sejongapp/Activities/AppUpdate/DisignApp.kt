package com.example.sejongapp.Activities.AppUpdate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.sejongapp.Activities.AnnousmentActivity.ui.theme.backgroundColor
import com.example.sejongapp.NavBar.getLocalized
import com.example.sejongapp.R
import com.example.sejongapp.models.DataClasses.ProgramUpdate
import com.example.sejongapp.models.DataClasses.ProgramUpdateData
import com.example.sejongapp.models.ViewModels.UserViewModels.ProgramUpdateViewModel
import com.example.sejongapp.retrofitAPI.NetworkResponse


@Composable
fun AppUpdateDesign() {
    val context = LocalContext.current
    val viewModel: ProgramUpdateViewModel = viewModel()
    val result = viewModel.programUpdate.observeAsState(NetworkResponse.Idle)

    LaunchedEffect(Unit) {
        viewModel.getProgramUpdate(context)
    }

    when (result.value) {
        is NetworkResponse.Loading -> {
            val composition by rememberLottieComposition(

                LottieCompositionSpec.Asset(
                    "Loading.lottie")
            )
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LottieAnimation(
                    composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier.size(120.dp)
                )
            }
        }
        is NetworkResponse.Success -> {
            val proData = (result.value as NetworkResponse.Success<ProgramUpdateData>).data
            val localVersion = context.packageManager.getPackageInfo(context.packageName, 0).versionName
            val serverVersion = proData[0].version

            if (localVersion == serverVersion) {
                AppIsUpdatedScreen(proData[0])
            } else {
                NewUpdateAvailableScreen(proData[0])
            }
        }
        else -> { /* Обработка ошибок */ }
    }
}

@Composable
fun NewUpdateAvailableScreen(update: ProgramUpdate) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp)
    ) {

        Box (
            Modifier.fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Surface(
                onClick = { (context as? appupdateactivity)?.finish() },
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.size(45.dp)
            ) {
                Box (
                    contentAlignment = Alignment.Center
                )  {
                    Icon (
                        Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp),
                        tint = Color.Black

                    )
                }
            }

        }


        Spacer(modifier = Modifier.height(30.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(12.dp)
        ) {
            Column(modifier = Modifier.padding(28.dp)) {
                // Иконка и Версия
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(Color(0xFFBEA96A).copy(0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.CloudDownload,
                            null,
                            tint = Color(0xFFBEA96A),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            stringResource(R.string.Updates),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            "${stringResource(R.string.Version)} ${update.version}",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Разделитель
                Box(
                    Modifier.fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFFEEEEEE)))

                Spacer(modifier = Modifier.height(24.dp))

                // Что нового
                Text(
                    stringResource(R.string.Whats_new),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = update.content.getLocalized(context),
                    fontSize = 15.sp,
                    color = Color.DarkGray,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { /* Логика скачивания */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBEA96A))
                ) {
                    Text(
                        stringResource(R.string.Update),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun AppIsUpdatedScreen(proData: ProgramUpdate) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(Modifier.fillMaxWidth().padding(top = 8.dp)) {
            Surface(
                onClick = { (context as? appupdateactivity)?.finish() },
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.size(45.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(0.4f))


        Box(contentAlignment = Alignment.Center) {

            Surface(
                modifier = Modifier.size(160.dp),
                shape = CircleShape,
                color = Color(0xFF4CAF50).copy(alpha = 0.05f)
            ) {}
            Surface(
                modifier = Modifier.size(120.dp),
                shape = CircleShape,
                color = Color(0xFF4CAF50).copy(alpha = 0.1f)
            ) {}


            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(80.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ТЕКСТОВЫЙ БЛОК
        Text(
            text = stringResource(R.string.app_up_to_date),
            fontSize = 26.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF2D3436),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.latest_version_installed),
            fontSize = 15.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.weight(0.6f))


        Surface(
            color = Color.White,
            shape = RoundedCornerShape(20.dp),
            shadowElevation = 4.dp,
            modifier = Modifier.wrapContentSize()
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color(0xFF4CAF50), CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "${stringResource(R.string.version_label)} ${proData.version}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}