package com.example.sejongapp.Activities.AppUpdate

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sejongapp.Activities.AnnousmentActivity.ui.theme.backgroundColor
import com.example.sejongapp.NavBar.getLocalized
import com.example.sejongapp.R
import com.example.sejongapp.models.DataClasses.ProgramUpdate
import com.example.sejongapp.models.DataClasses.ProgramUpdateData
import com.example.sejongapp.models.ViewModels.UserViewModels.ProgramUpdateViewModel
import com.example.sejongapp.retrofitAPI.NetworkResponse

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppUpdateDesign() {

    val context = LocalContext.current
    val viewModel: ProgramUpdateViewModel = viewModel()
    val result = viewModel.programUpdate.observeAsState(NetworkResponse.Idle)


    LaunchedEffect (Unit){
        viewModel.getProgramUpdate(context)
    }


    when(result.value){
        is NetworkResponse.Error -> {

        }
        NetworkResponse.Idle ->{

        }
        NetworkResponse.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 100.dp),
                contentAlignment = Alignment.Center

            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }
        is NetworkResponse.Success -> {
            val proData = (result.value as NetworkResponse.Success<ProgramUpdateData>).data

            val localVersion = context.packageManager.getPackageInfo(context.packageName, 0).versionName

            val serverVersion = proData[0].version

            if ( localVersion == serverVersion ) {
                AppIsUpdatedScreen(proData[0])
                return
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .padding(20.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "ic_ArrowBack",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(bottom = 16.dp,top = 10.dp)
                        .clickable (
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null

                        ) {
                            (context as? appupdateactivity)?.finish()
                        }
                )


                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudDownload,
                                contentDescription = "Download",
                                tint = Color(0xFFBEA96A),
                                modifier = Modifier.size(60.dp)
                            )

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    text = context.getString(R.string.Updates),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text =context.getString(R.string.Version) + proData[0].version,
                                    fontSize = 17.sp,
                                    color = Color.Black
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = context.getString(R.string.New_update_available),
                                    fontSize = 15.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Что нового
                        Text(
                            text = context.getString(R.string.Whats_new),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = proData[0].content.getLocalized(context),
                            fontSize = 16.sp,
                            color = Color.DarkGray,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))


                        Button(
                            onClick = { },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(55.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFBEA96A),
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = context.getString(R.string.Update), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)

                        }
                        Spacer(modifier = Modifier.height(10.dp))

                    }
                }
            }
        }
    }


}






@Composable
fun AppIsUpdatedScreen(proData: ProgramUpdate ) {
    val context = LocalContext.current

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(20.dp)
    ) {
        Icon (
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "ic_ArrowBack",
            tint = Color.Black,
            modifier = Modifier
                .size(60.dp)
                .padding(bottom = 16.dp,top = 10.dp)
                .clickable (
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    (context as? appupdateactivity)?.finish()

                }
        )
        Box (
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(8.dp)

            ) {
                Column (
                    modifier = Modifier.padding(24.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Icon (
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "ic_Check",
                        tint = Color(0xFFBEA96A),
                        modifier = Modifier.size(60.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text (
                        text = "Приложение актуально",
                        fontSize = 22.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold

                    )
                    Spacer (modifier = Modifier.height(5.dp))

                    Text (
                        text = "У вас установлена \n последняя версия",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,

                    )
                    Spacer (modifier = Modifier.height(5.dp))

                    Text (
                        text = "Версия: " + proData.version,
                        fontSize = 16.sp,
                        color = Color.Black,

                    )

                }
            }
        }
    }
}












