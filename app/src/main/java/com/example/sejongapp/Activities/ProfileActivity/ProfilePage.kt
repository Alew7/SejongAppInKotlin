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
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.sejongapp.DialogModels.EditAvatarUser
import com.example.sejongapp.DialogModels.EditUserDialog
import com.example.sejongapp.DialogModels.EditUserPasswordDialog
import com.example.sejongapp.R
import com.example.sejongapp.components.LoadingDialog
import com.example.sejongapp.components.showError
import com.example.sejongapp.models.DataClasses.UserDataClasses.ChangeUserAvatarInfo
import com.example.sejongapp.models.DataClasses.UserDataClasses.ChangeUserInfo
import com.example.sejongapp.models.DataClasses.UserDataClasses.UserData
import com.example.sejongapp.models.DataClasses.UserDataClasses.tokenData
import com.example.sejongapp.models.ViewModels.UserViewModels.UserViewModel
import com.example.sejongapp.retrofitAPI.NetworkResponse
import com.example.sejongapp.room.RoomUserViewModel
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.primaryColor
import com.example.sejongapp.utils.UserStatusEnum
import kotlinx.coroutines.delay

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ProfilePage() {
    val userViewModel: UserViewModel = viewModel()
    val context = LocalContext.current

    // ВАЖНО: используем state для данных, чтобы UI реагировал на изменения внутри этой функции
    var userDataState by remember { mutableStateOf(getUserData(context)) }

    var showEditDialog by remember { mutableStateOf(false) }
    var showLoadingDialog by remember { mutableStateOf(false) }
    var fetchingNewUserData by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var showUserAvatarDialog by remember { mutableStateOf(false) }
    var avatarChanged by remember { mutableStateOf(false) }
    var showSuccessAnomation by remember { mutableStateOf(false) }
    var isChangingPassword by remember { mutableStateOf(false) }

    val roomviewModel: RoomUserViewModel = viewModel()
    val token = LocalData.getSavedToken(context)

    LaunchedEffect(Unit) {
        roomviewModel.loadUser(token)
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- Header ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { (context as? Activity)?.finish() },
                    tint = Color(0xFF1A1A1A)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // --- Avatar Section ---
            Box(contentAlignment = Alignment.BottomEnd) {
                Surface(
                    modifier = Modifier
                        .size(135.dp)
                        .border(4.dp, Color.White, CircleShape)
                        .shadow(25.dp, CircleShape),
                    shape = CircleShape,
                    color = Color.White
                ) {
                    if (userDataState.avatar.isNotEmpty()) {
                        Image(
                            painter = rememberImagePainter(userDataState.avatar),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.padding(35.dp),
                            tint = Color(0xFFE0E0E0)
                        )
                    }
                }

                Surface(
                    onClick = { showUserAvatarDialog = true },
                    shape = CircleShape,
                    color = primaryColor,
                    shadowElevation = 6.dp,
                    modifier = Modifier.size(38.dp).border(3.dp, Color.White, CircleShape)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = userDataState.username ?: "username",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )

            Text(
                text = userDataState.fullname ?: "Fullname not set",
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(35.dp))

            // --- Info Card (Modern Style) ---
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    ProfileItemModern(
                        icon = Icons.Default.VerifiedUser,
                        title = context.getString(R.string.status),
                        value = when(userDataState.status){
                            UserStatusEnum.STUDENT -> context.getString(R.string.Student)
                            UserStatusEnum.TEACHER -> "Teacher"
                            else -> "Admin"
                        },
                        accentColor = Color(0xFF4CAF50)
                    )
                    Divider(modifier = Modifier.padding(vertical = 16.dp).alpha(0.3f))

                    val groupsValue = userDataState.groups?.toString()?.replace("[", "")?.replace("]", "") ?: "-"
                    ProfileItemModern(
                        icon = Icons.Default.Group,
                        title = context.getString(R.string.Groups),
                        value = groupsValue,
                        accentColor = Color(0xFF2196F3)
                    )
                    Divider(modifier = Modifier.padding(vertical = 16.dp).alpha(0.3f))
                    ProfileItemModern(
                        icon = Icons.Default.Email,
                        title = context.getString(R.string.Email),
                        value = userDataState.email ?: "-",
                        accentColor = Color(0xFFFF9800)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // --- Buttons ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(bottom = 30.dp)
            ) {
                Button(
                    onClick = { showEditDialog = true },
                    modifier = Modifier.fillMaxWidth().height(58.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                ) {
                    Text(context.getString(R.string.Edit_profile), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(

                    onClick = { showPasswordDialog = true },
                    modifier = Modifier.fillMaxWidth().height(58.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                ) {
                    Text(context.getString(R.string.Change_password), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }


        if (showEditDialog) {
        EditUserDialog(userDataState, { showEditDialog = false }, { new ->
            userViewModel.changeUserName(token, new)
            showEditDialog = false; showLoadingDialog = true; isChangingPassword = false
        })
    }
        if (showPasswordDialog) {
        EditUserPasswordDialog({ showPasswordDialog = false }, { pass ->
            userViewModel.changeUserPassword(token, pass)
            showPasswordDialog = false; showLoadingDialog = true; isChangingPassword = true
        })
    }
        if (showUserAvatarDialog) {
        EditAvatarUser(userDataState, { showUserAvatarDialog = false; avatarChanged = false }, { uri ->
            userViewModel.changeUserAvatar(context, token, uri)
            showUserAvatarDialog = false; showLoadingDialog = true; avatarChanged = true
        })
    }


    if (showLoadingDialog) {
        if (avatarChanged) {
            val result by userViewModel.userAvatarResult.observeAsState(NetworkResponse.Idle)
            when (result) {
                is NetworkResponse.Error -> showError((result as NetworkResponse.Error).message) { showLoadingDialog = false }
                NetworkResponse.Loading -> LoadingDialog(context.getString(R.string.applying_changes))
                is NetworkResponse.Success -> {
                    val fetched = (result as NetworkResponse.Success<ChangeUserAvatarInfo>).data
                    val updated = userDataState.copy(avatar = fetched.avatar)
                    LocalData.setUserData(context, updated)
                    userDataState = updated
                    showSuccessAnomation = true
                    showLoadingDialog = false
                    avatarChanged = false
                    fetchingNewUserData = true
                }
                else -> {}
            }
        } else {
            val result by userViewModel.userChangeResult.observeAsState(NetworkResponse.Idle)
            when (result) {
                is NetworkResponse.Error -> showError((result as NetworkResponse.Error).message) { showLoadingDialog = false }
                NetworkResponse.Loading -> LoadingDialog(context.getString(R.string.applying_changes))
                is NetworkResponse.Success -> {
                    if (isChangingPassword) {
                        val tData = (result as NetworkResponse.Success<tokenData>).data
                        LocalData.setToken(context, tData.auth_token)
                    } else {
                        val fetched = (result as NetworkResponse.Success<ChangeUserInfo>).data
                        val updated = userDataState.copy(username = fetched.username, email = fetched.email)
                        LocalData.setUserData(context, updated)
                        userDataState = updated
                    }
                    showSuccessAnomation = true
                    showLoadingDialog = false
                    fetchingNewUserData = true
                }
                else -> {}
            }
        }
    }

    if (fetchingNewUserData) {
        val result by userViewModel.userDataResult.observeAsState(NetworkResponse.Idle)
        if (result is NetworkResponse.Success) {
            val fresh = (result as NetworkResponse.Success<UserData>).data
            LocalData.setUserData(context, fresh)
            userDataState = fresh
            fetchingNewUserData = false
        }
    }

    if (showSuccessAnomation) {
        Box(Modifier.fillMaxSize().background(Color.Black.copy(0.7f)), Alignment.Center) {
            SuccessAnimation()
            LaunchedEffect(Unit) {
                delay(2500)
                showSuccessAnomation = false
                (context as? Activity)?.recreate()
            }
        }
    }
}

@Composable
fun ProfileItemModern(icon: ImageVector, title: String, value: String, accentColor: Color) {
    Row(
        modifier = Modifier
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .background(accentColor.copy(0.12f),
                    shape = RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier
                    .size(22.dp)
            )
        }

        Spacer(Modifier.width(16.dp))

        Column {

            Text(
                text = title,
                fontSize = 11.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = value,
                fontSize = 16.sp,
                color = Color(0xFF2D2D2D),
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
fun SuccessAnimation(modifier: Modifier = Modifier) {
    val comp by rememberLottieComposition(
        LottieCompositionSpec.Asset("Sucesso.lottie")
    )

    val prog by animateLottieCompositionAsState(comp, iterations = 1)
    LottieAnimation(comp, { prog }, modifier = modifier.size(250.dp))
}