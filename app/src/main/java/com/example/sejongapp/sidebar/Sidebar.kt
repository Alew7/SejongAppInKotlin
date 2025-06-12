package com.example.sejongapp.sidebar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun Sidebar (isOpen: Boolean,onClose: () -> Unit) {
    if (isOpen) {
        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { onClose() }
        ) {
            Box (
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .width(200.dp)
                    .fillMaxHeight()
                    .background(Color.White)
            ) {
                Column  (
                    modifier = Modifier
                        .padding(20.dp)
                ){
                    Text (
                        text = "icon1"
                    )
                    Spacer(modifier = Modifier.padding(bottom = 5.dp))
                    Text (
                        text = "icon2"
                    )
                    Spacer(modifier = Modifier.padding(bottom = 5.dp))
                    Text (
                        text = "icon3"
                    )
                    Text (
                        text = "Закрыть",
                        modifier = Modifier
                            .clickable {
                                onClose()
                            }

                    )
                }

            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun Preview () {
    Sidebar(isOpen = true, onClose = {})
}