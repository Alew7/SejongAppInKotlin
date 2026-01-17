package com.example.sejongapp.Activities.ProfileActivity

import LocalData
import LocalData.getUserData
import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.sejongapp.R
import com.example.sejongapp.components.EditAvatarUser
import com.example.sejongapp.components.EditUserDialog
import com.example.sejongapp.components.EditUserPasswordDialog
import com.example.sejongapp.components.LoadingDialog
import com.example.sejongapp.components.showError
import com.example.sejongapp.models.DataClasses.UserDataClasses.ChangeUserAvatarInfo
import com.example.sejongapp.models.DataClasses.UserDataClasses.ChangeUserInfo
import com.example.sejongapp.models.DataClasses.UserDataClasses.UserData
import com.example.sejongapp.models.DataClasses.UserDataClasses.tokenData
import com.example.sejongapp.models.ViewModels.UserViewModels.UserViewModel
import com.example.sejongapp.retrofitAPI.NetworkResponse
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.primaryColor
import com.example.sejongapp.ui.theme.secondaryColor
import com.example.sejongapp.utils.UserStatusEnum
import kotlinx.coroutines.delay

const val TAG = "TAG_ProfilePage"

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ProfilePage() {
    val userViewModel : UserViewModel = viewModel()
    val context = LocalContext.current
    val userData = remember { getUserData(context)}
    var showEditDialog by remember { mutableStateOf(false) }
    var showLoadingDialog by remember { mutableStateOf(false) }
    var fetchingNewUserData by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var showUserAvatarDialog by remember { mutableStateOf(false) }
    var avatarChanged by remember { mutableStateOf(false) }
    var showSuccessAnomation by remember { mutableStateOf(false) }


    var isChangingPassword by remember { mutableStateOf(false) }


    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight

        val paddingHorizontal = screenWidth * 0.05f
        val avatarSize = screenWidth * 0.3f
        val editButtonSize = avatarSize * 0.25f
        val titleFontSize = (screenWidth.value * 0.07).sp
        val subFontSize = (screenWidth.value * 0.045).sp
        val buttonHeight = screenHeight * 0.07f

        val painter = rememberImagePainter(
            data = userData.avatar,
            builder = {
                crossfade(true)
                allowHardware(false)
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = paddingHorizontal),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image (
                painter = painterResource(R.drawable.ic_back),
                contentDescription = "ic_back"
                ,
                modifier = Modifier
                    .size(screenWidth * 0.08f)
                    .offset(y = 30.dp)
                    .align(Alignment.Start)
                    .clickable (
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        (context as? ProfileActivity)?.finish()
                    }

            )
//            IconButton(
//                onClick = { (context as? ProfileActivity)?.finish() },
//                modifier = Modifier.align(Alignment.Start)
//                    .offset(y = 30.dp)
//            ) {
//                Icon(
//                    imageVector = Icons.Default.ArrowBack,
//                    contentDescription = "ic_back",
//                    modifier = Modifier.size(screenWidth * 0.08f)
//                )
//            }

            // Заголовок
            Text(
                text = context.getString(R.string.Profile),
                fontSize = titleFontSize,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF222222)
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.03f))

            // Аватар
            Box(
                modifier = Modifier
                    .size(avatarSize)
                    .clip(CircleShape)
                    .background(Color.White)
                    .shadow(8.dp, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (userData.avatar.isNotEmpty()) {

                    Image(
                        painter = rememberImagePainter(userData.avatar),
                        contentDescription = "userAvatar",
                        modifier = Modifier
                            .fillMaxSize()
                            .size(avatarSize * 0.9f)
                            .clip(CircleShape),
//                            .rotate(90f),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Default user icon",
                        tint = Color(0xFF555555),
                        modifier = Modifier.size(avatarSize * 0.5f)
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = -(avatarSize * 0.1f), y = -(avatarSize * 0.1f))
                        .size(editButtonSize)
                        .clip(CircleShape)
                        .background(primaryColor)
                        .border(2.dp, Color.White, CircleShape)
                        .shadow(6.dp, CircleShape)
                        .clickable { showUserAvatarDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "ic_Edit",
                        tint = Color.White,
                        modifier = Modifier.size(editButtonSize * 0.5f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(screenHeight * 0.02f))

            // Имя и Email
            Text(
                text = userData.username ?: "Unknown User",
                fontSize = titleFontSize,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111111)
            )
            Text(
                text = userData.fullname ?: "email@example.com",
                fontSize = subFontSize,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.03f))

            // Карточка с информацией
            Card(
                shape = RoundedCornerShape(screenWidth * 0.04f),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column {
                    ProfileItem(
                        icon = Icons.Default.VerifiedUser,
                        title = context.getString(R.string.status),
                        value = (
                            when(userData.status){
                                UserStatusEnum.STUDENT -> context.getString(R.string.Student)
                                UserStatusEnum.TEACHER -> "Teacher"
                                UserStatusEnum.ADMIN -> "Admin"
                                UserStatusEnum.UNKNOWN -> "Unauthorized"
                            }
                            ),
                        screenWidth = screenWidth
                    )
                    Divider(color = Color(0xFFDDDDDD))

                    val groupsValue = userData.groups
                        ?.toString()
                        ?.replace("[", "")
                        ?.replace("]", "")
                        ?: "-"

                    ProfileItem(
                        icon = Icons.Default.Group,
                        title = context.getString(R.string.Groups),
                        value = groupsValue,
                        screenWidth = screenWidth
                    )

                    Divider(color = Color(0xFFDDDDDD))

                    ProfileItem(
                        icon = Icons.Filled.Email,
                        title = context.getString(R.string.Email),
                        value = userData.email ?: "-",
                        screenWidth = screenWidth
                    )
                }
            }

            Spacer(modifier = Modifier.height(screenHeight * 0.03f))

            // Кнопки
            Button(
                onClick = { showEditDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(buttonHeight),
                shape = RoundedCornerShape(screenWidth * 0.03f),
                colors = ButtonDefaults.buttonColors(containerColor = secondaryColor)
            ) {
                Text(
                    text = context.getString(R.string.Edit_profile),
                    color = Color.White,
                    fontSize = subFontSize,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(screenHeight * 0.015f))

            Button(
                onClick = { showPasswordDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(buttonHeight),
                shape = RoundedCornerShape(screenWidth * 0.03f),
                colors = ButtonDefaults.buttonColors(containerColor = secondaryColor)
            ) {
                Text(
                    text = context.getString(R.string.Change_password),
                    color = Color.White,
                    fontSize = subFontSize,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }



//---------------------    The dialogs!  ---------------------//

        //        Edit dialog. For making changes of the user
        if (showEditDialog){

            EditUserDialog(
                userData = userData,
                onDismiss = { showEditDialog = false },
                onSave = { newUserData ->
                    Log.d(TAG, "The UserData was changed to ${newUserData}")
                    userViewModel.changeUserName(LocalData.getSavedToken(context),newUserData)
                    showEditDialog = false
                    showLoadingDialog = true
                    isChangingPassword = false

                }
            )
        }

        // Edit Password dialog
        if (showPasswordDialog) {
            EditUserPasswordDialog(
                onDismiss = { showPasswordDialog = false },
                onSave = { newPassword ->
                    Log.d("TAG_ProfilePage", "The new password is $newPassword")
                    userViewModel.changeUserPassword(LocalData.getSavedToken(context),newPassword)

                    showPasswordDialog = false
                    showLoadingDialog = true
                    isChangingPassword = true

                }
            )
        }

        // Edit Avatar dialog
        if (showUserAvatarDialog) {
            EditAvatarUser(
                userData = userData,
                onDismiss = {
                    showUserAvatarDialog = false
                    avatarChanged = false
                            },
                onSave = {uri ->
                    var token = LocalData.getSavedToken(context)
                    userViewModel.changeUserAvatar(context, token, uri)
                    showUserAvatarDialog = false
                    showLoadingDialog = true
                    avatarChanged = true
                }
            )
        }


        //     Loading dialogs for showing and handling the user changing requests
        if(showLoadingDialog){
            if (avatarChanged){
                val result by userViewModel.userAvatarResult.observeAsState(NetworkResponse.Idle)
                Log.i(TAG, "Started fetching for the avatar")
                when (result) {

                    is NetworkResponse.Error -> {
                        showError((result as NetworkResponse.Error).message) {
                            showLoadingDialog = false
                        }
                    }
                    NetworkResponse.Idle -> {}
                    NetworkResponse.Loading -> {
                        LoadingDialog(LocalContext.current.getString(R.string.applying_changes))
                    }
                    is NetworkResponse.Success ->{
                            var fetchedData = (result as NetworkResponse.Success<ChangeUserAvatarInfo>).data

                            if (fetchedData.message.isNullOrEmpty()){
                                showError("Server returned null token") { showLoadingDialog = false }
                            }
                            Toast.makeText(context, "Avatar updated successfully!", Toast.LENGTH_SHORT).show()
                            showSuccessAnomation = true
                            Log.i(TAG, "Success on  fetching the data")
                            fetchingNewUserData = true
                            showLoadingDialog = false
                            isChangingPassword = false
                            avatarChanged = false
                            var theUserData = getUserData(context)
                            LocalData.setUserData(context, UserData(
                                username = theUserData.username,
                                avatar = fetchedData.avatar,
                                fullname = theUserData.fullname,
                                email = theUserData.email,
                                status = theUserData.status,
                                groups = theUserData.groups

                            )
                            )



                    }
                    else ->{}


                }
            }
            else{
                val result by userViewModel.userChangeResult.observeAsState(NetworkResponse.Idle)
                Log.i(TAG, "Started fetching for the user data")

                when (result) {

                    is NetworkResponse.Error -> {
                        showError((result as NetworkResponse.Error).message) {
                            showLoadingDialog = false
                        }
                    }
                    NetworkResponse.Idle -> {}
                    NetworkResponse.Loading -> {
                        LoadingDialog(LocalContext.current.getString(R.string.applying_changes))
                    }
                    is NetworkResponse.Success ->{
                        Log.i(TAG, "Success on  fetching the data")
                        if (isChangingPassword){
                            var fetchedData = (result as NetworkResponse.Success<tokenData>).data
                            Log.v(TAG, "ProfileChangeDialog : The token is $fetchedData")
                            if (fetchedData.auth_token.isNullOrEmpty()){
                                showError("Server returned null token") { showLoadingDialog = false }
                            }

                            Toast.makeText(LocalContext.current, "The Password has been successfully updated", Toast.LENGTH_LONG)
                            showSuccessAnomation = true
                            LocalData.setToken(context, fetchedData.auth_token)
                            fetchingNewUserData = true
                            showLoadingDialog = false
                            isChangingPassword = false

                        }
                        else{
                            var fetchedData = (result as NetworkResponse.Success<ChangeUserInfo>).data
                            if (fetchedData.username.isNullOrEmpty()){
                                showError("Server returned null token") { showLoadingDialog = false }
                            }
                            Toast.makeText(LocalContext.current, "The data has been successfully updated", Toast.LENGTH_LONG)
                            showSuccessAnomation = true
                            Log.i(TAG, "Success on  fetching the data")
                            fetchingNewUserData = true
                            showLoadingDialog = false
                            isChangingPassword = false
                            avatarChanged = false
                            var theUserData = getUserData(context)
                            LocalData.setUserData(context, UserData(
                                username = fetchedData.username,
                                avatar = theUserData.avatar,
                                fullname = theUserData.fullname,
                                email = fetchedData.email,
                                status = theUserData.status,
                                groups = theUserData.groups
                            )
                            )
                        }

                    }
                    else ->{}


                }
            }



        }


        //     Loading dialogs for showing and handing the new user data that was changed
        if(fetchingNewUserData){
            val result by userViewModel.userDataResult.observeAsState(NetworkResponse.Idle)
            when (result) {
                is NetworkResponse.Error -> {
                    showError((result as NetworkResponse.Error).message) {
                        fetchingNewUserData = false
                    }
                }
                NetworkResponse.Idle -> {}
                NetworkResponse.Loading -> {
                        LoadingDialog(LocalContext.current.getString(R.string.refreshing_data))

                }
                is NetworkResponse.Success ->{
                    Log.d(TAG, "Success on  fetching the data ${(result as NetworkResponse.Success<UserData>).data}")
                    LocalData.setUserData(LocalContext.current,
                        (result as NetworkResponse.Success<UserData>).data
                    )
                    Toast.makeText(LocalContext.current, "The data has been successfully updated", Toast.LENGTH_LONG)
                    fetchingNewUserData = false



                }
                else ->{}
            }
        }
    if (showSuccessAnomation) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x66000000)),
            contentAlignment = Alignment.Center
        ) {
            val screenWidth = maxWidth

            SuccessAnimation(
                modifier = Modifier.size(screenWidth * 0.5f)
            )

            LaunchedEffect(Unit) {
                delay(3000)
                showSuccessAnomation = false

                if (context is Activity) {
                    context.recreate()
                }
            }
        }
    }

}

@Composable
fun ProfileItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, value: String, screenWidth: androidx.compose.ui.unit.Dp) {
    val iconSize = screenWidth * 0.06f
    val titleFontSize = (screenWidth.value * 0.04).sp
    val valueFontSize = (screenWidth.value * 0.045).sp
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = screenWidth * 0.03f, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color(0xFF555555),
            modifier = Modifier.size(iconSize)
        )
        Spacer(modifier = Modifier.width(screenWidth * 0.03f))
        Column {
            Text(text = title, fontSize = titleFontSize, color = Color.Gray)
            Text(
                text = value,
                fontSize = valueFontSize,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun SuccessAnimation(modifier: Modifier = Modifier) {


    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("Sucesso.lottie")
    )


    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
        speed = 1f
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier.size(200.dp)
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfilePreview() {
    ProfilePage()
}




