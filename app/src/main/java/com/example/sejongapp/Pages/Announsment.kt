package com.example.sejongapp.Pages

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sejongapp.R
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.primaryColor
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.example.sejongapp.AnnousmentActivity.AnnousmentActivity
import com.example.sejongapp.ui.theme.brightBackgroundColor
import com.example.sejongapp.ui.theme.darkGray



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnousmentPage(onChangeScreen: (Int) -> Unit = {}) {


    val context = LocalContext.current
    var isSeraching by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }



    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        val screenWidth = maxWidth

        Column {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
                    .drawBehind {
                        val strokeWidth = 2.dp.toPx()
                        val y = size.height - strokeWidth / 2
                        drawLine(
                            color = primaryColor,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = strokeWidth
                        )
                    }
            ) {
            if (!isSeraching) {
                Image(
                    painter = painterResource(R.drawable.ic_hed),
                    contentDescription = "ic_had",
                    modifier = Modifier
                        .size(65.dp)
                        .padding(start = 25.dp)
                )

                Image (
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = "ic_search",
                    modifier = Modifier
                        .padding(end = 20.dp,top = 20.dp)
                        .align(Alignment.TopEnd)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null

                        ) {
                            isSeraching = true
                        }
                )

            }

                    if (isSeraching) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                                .padding(horizontal = 16.dp, vertical = 16.dp)
                        ) {


                            Spacer(modifier = Modifier.width(20.dp))

                            OutlinedTextField(
                                value = searchText,
                                onValueChange = { searchText = it },
                                placeholder = { Text("Search") },
                                leadingIcon = {
                                    IconButton(onClick = {isSeraching = false}) {
                                        Icon (
                                            imageVector = Icons.Default.ArrowBack,
                                            contentDescription = "Back",
                                            tint = Color.Black
                                        )
                                    }
                                },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedTextColor = Color.Black,
                                    focusedBorderColor = darkGray,
                                    cursorColor = Color.Black

                                ),
                                modifier = Modifier
                                    .padding(end = 15.dp)
                                    .fillMaxWidth()
                                    .height(60.dp),
                                shape = RoundedCornerShape(15.dp)

                            )


                        }
                    }

            }


        }
    }




            // Список карточек
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 150.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(10) { index ->
                    AnnousmentCard {
                        val intent = Intent(context, AnnousmentActivity::class.java)
                        context.startActivity(intent)
                    }
                }
            }

}



@Composable
fun AnnousmentCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            },
        shape = RoundedCornerShape(15.dp),
//        elevation = CardDefaults.cardElevation(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = brightBackgroundColor,
        )
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(R.drawable.annousment_img),
                contentDescription = "annousment_img",
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Announcment title",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "part of content\nLorem ipsum dolor sit amet,\nconsenctetur adiposcing elit...",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = "11.02.2025",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    AnnousmentPage()
}


@Preview(
    name = "📱 Small Phone",
    showBackground = true,
    showSystemUi = true,
    widthDp = 320,
    heightDp = 568
)
@Composable
private fun PreviewSmallPhone() {
    AnnousmentPage()
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
    AnnousmentPage()
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
    AnnousmentPage()
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
    AnnousmentPage()
}