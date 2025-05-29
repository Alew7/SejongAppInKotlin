package com.example.sejongapp.Pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import com.example.sejongapp.R
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.brightBackgroundColor
import com.example.sejongapp.ui.theme.primaryColor





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun librarypage () {

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
                        contentDescription = "ic_search",
                        modifier = Modifier
                            .size(65.dp)
                            .padding(start = 25.dp)

                    )
                    Image(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = "ic_search",
                        modifier = Modifier
                            .padding(end = 20.dp, top = 20.dp)
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
                    )
                    {
                        Spacer(modifier = Modifier.width(20.dp))

                        OutlinedTextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            placeholder = { Text("Search") },
                            leadingIcon = {
                                IconButton(onClick = { isSeraching = false }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Back",
                                        tint = Color.Black
                                    )
                                }
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedTextColor = Color.Black,
                                focusedBorderColor = Color.Black,
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
            cardlibrary()
        }
    }
}



@Composable
fun cardlibrary() {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    // onClick
                },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = brightBackgroundColor,
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Lorem Upsum",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.ic_book),
                        contentDescription = "Book Icon",
                        modifier = Modifier
                            .size(60.dp)
                            .padding(end = 12.dp)
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Lorem ipsum dolor sit amet,\nconsectetur adipiscing elit. Sed",
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Custom Button
                        Row(
                            modifier = Modifier
                                .height(36.dp)
                                .align(Alignment.End)
                                .padding(end = 10.dp)
                                .width(120.dp)
                                .background(
                                    color = Color(0xFFD1B17B),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { /* onClick */ }
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Read", color = Color.White)
                            }

                            // Вертикальная линия между кнопками
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .fillMaxHeight()
                                    .background(Color.White.copy(alpha = 0.6f))
                            )

                            Box(
                                modifier = Modifier
                                    .width(36.dp)
                                    .fillMaxHeight(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_arrow_down), // стрелка вниз
                                    contentDescription = "More",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }



@Preview (showBackground = true, showSystemUi = true)
@Composable
private fun Preview () {
    cardlibrary()
}









