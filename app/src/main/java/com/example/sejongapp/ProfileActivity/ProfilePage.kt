package com.example.sejongapp.ProfileActivity

import LocalToken.getUserData
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sejongapp.MainActivity
import com.example.sejongapp.ProfileActivity.ui.theme.backgroundColor
import com.example.sejongapp.R

@Composable
fun ProfilePage () {

    val text_size = 14.sp
    val context = LocalContext.current
    val userData = remember { getUserData(context) }




    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column {
            Row(
                modifier = Modifier
                .padding(start = 10.dp, top = 15.dp),
                verticalAlignment = Alignment.CenterVertically



            ) {
                Image(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = "ic_back",
                    modifier = Modifier
                        .size(50.dp)
                        .padding(top = 16.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null

                        ) {
                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)

                        }
                )

                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Profile",
                    fontSize = 26.sp,
                    modifier = Modifier
                        .padding(start = 15.dp, top = 15.dp )


                )
            }
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 50.dp, top = 20.dp)
            ) {
                Image (
                    painter = painterResource(R.drawable.ic_profile),
                    contentDescription = "ic_profile",
                    modifier = Modifier
                        .size(120.dp)
                        .padding(end = 30.dp)


                )
                Column {
                    Text(
                        text = " ${userData.fullname}",
                        fontSize = 26.sp,
                        modifier = Modifier
                            .padding(start = 25.dp)


                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text (
                        text = "${userData.phone}",
                        fontSize = text_size,
                        modifier = Modifier
                            .padding(start = 25.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text (
                        text = "Email: ",
                        fontSize = text_size,
                        modifier = Modifier
                            .padding(start = 25.dp)
                    )
                }
            }
            Column (
                modifier = Modifier
                    .padding(start = 50.dp, top = 35.dp)
            ) {
                Text (
                    text = "status: Student",
                    fontSize = text_size,
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text (
                    text = "Groups: 3B",
                    fontSize = text_size,
                )
            }
        }

    }

}



@Preview (showSystemUi = true, showBackground = true)
@Composable
private fun Preview () {
    ProfilePage()
}


@Preview(name = "Small Phone", widthDp = 320, heightDp = 640, showBackground = true, showSystemUi = true)
@Composable
fun PreviewSmall() { ProfilePage() }

@Preview(name = "Medium Phone", widthDp = 360, heightDp = 740, showBackground = true, showSystemUi = true)
@Composable
fun PreviewMedium() { ProfilePage() }

@Preview(name = "Large Phone", widthDp = 412, heightDp = 892, showBackground = true, showSystemUi = true)
@Composable
fun PreviewLarge() { ProfilePage() }

@Preview(name = "XL Phone", widthDp = 480, heightDp = 1000, showBackground = true, showSystemUi = true)
@Composable
fun PreviewXL() { ProfilePage() }


@Preview(
    name = "📱 Small Phone",
    showBackground = true,
    showSystemUi = true,
    widthDp = 320,
    heightDp = 568
)
@Composable
private fun PreviewSmallPhone() {
    ProfilePage()
}

@Preview(
    name = "📱 Standard Phone",
    showBackground = true,
    showSystemUi = true,
    widthDp = 393,
    heightDp = 851
)
@Composable
private fun PreviewStandardPhone() {
    ProfilePage()
}

@Preview(
    name = "📱 Large Phone / Fold",
    showBackground = true,
    showSystemUi = true,
    widthDp = 600,
    heightDp = 960
)
@Composable
private fun PreviewLargePhone() {
    ProfilePage()
}

@Preview(
    name = "💻 Tablet",
    showBackground = true,
    showSystemUi = true,
    widthDp = 800,
    heightDp = 1280
)
@Composable
private fun PreviewTablet() {
    ProfilePage()
}

@Preview (
    name = "📱 Small Phone",
    showBackground = true,
    showSystemUi = true,
    widthDp = 320,
    heightDp = 568
)
@Composable
private fun smallPreview() {
    ProfilePage()
}