package com.example.sejongapp.ProfileActivity

import android.content.Intent
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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

    val context = LocalContext.current
    val scale = remember { androidx.compose.animation.core.Animatable(0.2f) }

    LaunchedEffect  (Unit){
        scale.animateTo(
                targetValue = 1.2f,
                animationSpec = tween(durationMillis = 800)
            )
    }

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(start = 10.dp, top = 15.dp)


            ) {
                Image(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = "ic_back",
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null

                        ) {
                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)

                        }
                )
                Text(
                    text = "Profile",
                    fontSize = 25.sp,
                    modifier = Modifier
                        .padding(start = 15.dp, top = 30.dp)


                )
            }
            Row (
                modifier = Modifier
                    .padding(start = 50.dp, top = 20.dp)
            ) {
                Image (
                    painter = painterResource(R.drawable.ic_profile),
                    contentDescription = "ic_profile",
                    modifier = Modifier
                        .padding(end = 5.dp)
//                        .scale(scale.value)

                )
                Column {
                    Text(
                        text = "Full name",
                        fontSize = 30.sp,
                        modifier = Modifier
                            .padding(start = 25.dp)


                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text (
                        text = "Number: +992 000-33-90-66",
                        modifier = Modifier
                            .padding(start = 25.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text (
                        text = "Email: ",
                        modifier = Modifier
                            .padding(start = 25.dp)
                    )
                }
            }
            Column (
                modifier = Modifier
                    .padding(start = 50.dp, top = 40.dp)
            ) {
                Text (
                    text = "status: Student"
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text (
                    text = "Groups: 3B"

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


//@Preview(name = "Small Phone", widthDp = 320, heightDp = 640, showBackground = true, showSystemUi = true)
//@Composable
//fun PreviewSmall() { ProfilePage() }
//
//@Preview(name = "Medium Phone", widthDp = 360, heightDp = 740, showBackground = true, showSystemUi = true)
//@Composable
//fun PreviewMedium() { ProfilePage() }
//
//@Preview(name = "Large Phone", widthDp = 412, heightDp = 892, showBackground = true, showSystemUi = true)
//@Composable
//fun PreviewLarge() { ProfilePage() }
//
//@Preview(name = "XL Phone", widthDp = 480, heightDp = 1000, showBackground = true, showSystemUi = true)
//@Composable
//fun PreviewXL() { ProfilePage() }