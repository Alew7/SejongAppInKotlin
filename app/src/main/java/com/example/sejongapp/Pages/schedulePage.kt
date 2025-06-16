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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sejongapp.R
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.darkGray
import com.example.sejongapp.ui.theme.lightGray
import com.example.sejongapp.ui.theme.mediumGray
import com.example.sejongapp.ui.theme.primaryColor
import com.example.sejongapp.utils.NavigationScreenEnum

@Composable
fun Schedule(onChangeScreen: (NavigationScreenEnum) -> Unit = {}){

    Column {
        //    The header with logo icon
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
                Image(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = "ic_head",
                    modifier = Modifier
                        .size(64.dp)
                        .padding(start = 25.dp)
                        .clickable (
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null

                        ) {
                            onChangeScreen(NavigationScreenEnum.HOMEPAGE)
                        }
                )
            }
        }

        /////////////////////////////////////   BODY  /////////////////////////////////////

        var selectedPage by remember { mutableStateOf(0) }

//        The pagination func for sorting data
        PaginationSelector(
            selectedIndex = selectedPage,
            onSelected = { selectedPage = it }
        )
    }
  


}

// a compose func for pagination (the one that sorts data from all to the specific group)
@Composable
fun PaginationSelector(
    pages: List<String> = listOf("All", "1", "2", "3", "4", "5", "6", "7"),
    selectedIndex: Int,
    onSelected: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(pages) { index, label ->
            val isSelected = index == selectedIndex

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) primaryColor
                        else lightGray
                    )
                    .clickable { onSelected(index) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable()
private fun Preview() {
    Schedule()
}