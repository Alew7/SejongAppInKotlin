package com.example.sejongapp.Appupdate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sejongapp.ProfileActivity.ui.theme.backgroundColor

@Composable
fun Appupdate () {
    Box (
        modifier = Modifier.fillMaxSize()
            .background(backgroundColor)
    ) {
        Column (
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()

        ) {
            Text (
                text = "Обновления",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF222222),
                modifier = Modifier.padding(top = 20.dp,bottom = 16.dp)
            )
            Card (
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 12.dp),
                shape = RoundedCornerShape(22.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column (
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)

                ) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon (
                            imageVector = Icons.Default.CloudDownload,
                            contentDescription = null,
                            tint = Color(0xFF0A85FF),
                            modifier = Modifier.size(40.dp)
                        )
                        Column  {
                            Text (
                                text = "Версия 1.3.0",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp
                            )
                            Text (
                                text = "Доступно новая обновления",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                    Divider(color = Color(0xFFEAEAEA))

                    Text (
                        text = "Что нового:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "• Новый дизайн профиля\n" +
                                "• Повышена скорость загрузки\n" +
                                "• Исправлено множество ошибок\n" +
                                "• Улучшены чаты 1 на 1",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                    Button (
                        onClick = {  },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0A85FF),
                            contentColor = Color.White
                        )

                    ) {
                        Text (
                            text = "Обновить",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold

                        )
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppupdatePreview() {
    Appupdate()
}