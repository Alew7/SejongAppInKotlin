package com.example.sejongapp.ProfileActivity

import LocalData.getUserData
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sejongapp.ProfileActivity.ui.theme.backgroundColor
import com.example.sejongapp.R

@Composable
fun ProfilePage() {
    val context = LocalContext.current
    val userData = remember { getUserData(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(30.dp),    /// 16.dp
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton (onClick = {}) {
            Icon (
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "ic_back",
                tint = Color.Black,
                modifier = Modifier
                    .padding(end = 15.dp)
                    .size(30.dp)
            )
        }

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
            text = userData.username ?: "Unknown User",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111111)
        )
        Text(
            text = userData.fullname ?: "email@example.com",
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


                val groupsValue = userData.groups
                    ?.toString()
                    ?.replace("[", "")
                    ?.replace("]", "")
                    ?: "-"

                ProfileItem(
                    icon = Icons.Default.Group,
                    title = context.getString(R.string.Groups),
                    value = groupsValue

                )

                Divider(color = Color(0xFFDDDDDD))


                ProfileItem(
                    icon = Icons.Filled.Email,
                    title = context.getString(R.string.Email),
                    value = userData.email ?: "-"
                )

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
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Medium, maxLines = 1,
                overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f, fill = false)
                )


        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfilePreview() {
    ProfilePage()
}
