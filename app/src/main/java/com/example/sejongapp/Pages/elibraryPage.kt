package com.example.sejongapp.Pages

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import com.example.sejongapp.R
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sejongapp.AnnousmentActivity.AnnousmentActivity
import com.example.sejongapp.MainActivity
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.brightBackgroundColor
import com.example.sejongapp.ui.theme.darkGray
import com.example.sejongapp.ui.theme.primaryColor
import com.example.sejongapp.utils.NavigationScreenEnum


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun elaibaryPage (onChangeScreen : (NavigationScreenEnum) -> Unit = {}) {

      val context = LocalContext.current
      var isSeraching by remember { mutableStateOf(false) }
      var searchtext by remember { mutableStateOf("") }


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
                    .padding(top = 20.dp)
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
                        painter = painterResource(R.drawable.ic_back),
                        contentDescription = "ic_back",
                        modifier = Modifier
                            .size(64.dp)
                            .padding(start = 25.dp)
                            .clickable  (
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ){
                                val intent = Intent(context,MainActivity :: class.java)
                                context.startActivity(intent)
                            }
                    )

                    Image (
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = "ic_search",
                        modifier = Modifier
                            .size(48.dp)
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
                            value = searchtext,
                            onValueChange = { searchtext = it },
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
    LazyColumn  (
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 110.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)

    ){
        items(10) { index ->
            elibraryCard {
                val intent = Intent(context,AnnousmentActivity :: class.java)
                context.startActivity(intent)
            }
        }
    }


}


@Composable
fun elibraryCard (onClick : () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clickable  (
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ){
                onClick
            },
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = brightBackgroundColor,
        )
        
    ) {
        Column {
            Text (
                text = "Lorem Upsum",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 20.dp)
            )
            Row (modifier = Modifier.fillMaxWidth()) {
                Image (
                    painter = painterResource(R.drawable.ic_book),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 10.dp)
                )
                Column {
                    Text (
                        text = "Lorem ipsum dolor sit amet,\nconsectetur adipiscing elit.Sed",
                        modifier = Modifier
                            .padding(start = 30.dp)

                    )
                    Button(
                        shape = RoundedCornerShape(15.dp),
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryColor,
                            contentColor = backgroundColor,
                            disabledContentColor = primaryColor,
                            disabledContainerColor = backgroundColor
                        ),
                        modifier = Modifier
                            .padding(start = 100.dp)

                    ) {
                        Text(
                            text = "More",
                            color = Color.White

                        )
                    }
                }
            }
        }
    }

}


@Preview (showBackground = true, showSystemUi = true)
@Composable
fun showing ( ) {
    elibraryCard(onClick = {})
}