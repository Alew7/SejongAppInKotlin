package com.example.sejongapp.ProfileActivity

import LocalData.getUserData
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sejongapp.NavBar.TAG
import com.example.sejongapp.ProfileActivity.ui.theme.backgroundColor
import com.example.sejongapp.ProfileActivity.ui.theme.secondaryColor
import com.example.sejongapp.R
import com.example.sejongapp.components.EditUserDialog
import com.example.sejongapp.components.LoadingDialog
import com.example.sejongapp.components.showError
import com.example.sejongapp.models.DataClasses.UserData
import com.example.sejongapp.models.ViewModels.UserViewModel
import com.example.sejongapp.retrofitAPI.NetworkResponse

@Composable
fun ProfilePage() {
    val userViewModel : UserViewModel = viewModel()
    val context = LocalContext.current
    val userData = remember { getUserData(context) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showLoadingDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Заголовок
        Text(
            text = context.getString(R.string.Profile),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF222222)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Аватар
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.White)
                .shadow(8.dp, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                tint = Color(0xFF555555),
                modifier = Modifier.size(64.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Имя и Email
        Text(
            text = userData.fullname ?: "Unknown User",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111111)
        )
        Text(
            text = userData.email ?: "email@example.com",
            fontSize = 16.sp,
            color = Color.Gray
        )



        Spacer(modifier = Modifier.height(32.dp))

        // Карточка с информацией
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column {
                ProfileItem(
                    icon = Icons.Default.VerifiedUser,
                    title = context.getString(R.string.status),
                    value = userData.status ?: "—"
                )
                Divider(color = Color(0xFFDDDDDD))
                ProfileItem(
                    icon = Icons.Default.Group,
                    title = context.getString(R.string.Groups),
                    value = userData.groups ?.joinToString (", ") ?: "-"
                )

            }
        }


        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                showEditDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = secondaryColor) // Golden color
        ) {
            Text(
                text = "Change Profile",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (showEditDialog){
            EditUserDialog(
                userData = userData,
                onDismiss = { showEditDialog = false },
                onSave = { newUserData ->
                    Log.d(TAG, "The UserData was changed to ${newUserData}")
                    userViewModel.changeUserData(LocalData.getSavedToken(context),newUserData)
                    showEditDialog = false
                    showLoadingDialog = true
                }
            )
        }

        if(showLoadingDialog){
            val result by userViewModel.userDataResult.observeAsState(NetworkResponse.Idle)

                when (result) {
                    is NetworkResponse.Error -> {
                        showError((result as NetworkResponse.Error).message) {
                            showLoadingDialog = false
                        }
                    }
                    NetworkResponse.Idle -> {}
                    NetworkResponse.Loading -> {
                        LoadingDialog("waiting for the response")
                    }
                    is NetworkResponse.Success ->{
                        Log.d(TAG, "Success on  changing the data ${(result as NetworkResponse.Success<UserData>).data}")
                        LocalData.setUserData(LocalContext.current, (result as NetworkResponse.Success<UserData>).data)
                        Toast.makeText(LocalContext.current, "The data has been successfully changed", Toast.LENGTH_LONG)

                    }
                    else ->{}
                }
        }
    }
}

@Composable
fun ProfileItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color(0xFF555555),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = title, fontSize = 14.sp, color = Color.Gray)
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfilePreview() {
    ProfilePage()
}
