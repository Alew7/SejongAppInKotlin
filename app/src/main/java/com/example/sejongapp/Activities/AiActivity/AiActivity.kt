package com.example.sejongapp.Activities.AiActivity

import LocalData.getUserData
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.ModifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sejongapp.Activities.AiActivity.AiViewModel.AIChatViewModel
import com.example.sejongapp.Activities.AiActivity.DataClass.Message
import com.example.sejongapp.Activities.ProfileActivity.ui.theme.AiBubble
import com.example.sejongapp.Activities.ProfileActivity.ui.theme.SejongCard
import com.example.sejongapp.Activities.ProfileActivity.ui.theme.SejongText
import com.example.sejongapp.Activities.ProfileActivity.ui.theme.UserBubble
import com.example.sejongapp.models.ViewModels.UserViewModels.ScheduleViewModel
import com.example.sejongapp.retrofitAPI.NetworkResponse
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.primaryColor






class AiActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AIChatScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIChatScreen(
    viewModel: AIChatViewModel = viewModel(),
    scheduleViewModel: ScheduleViewModel = viewModel()


) {
    var inputText by remember { mutableStateOf("") }
    val activity = LocalActivity.current
    val listState = rememberLazyListState()
    val context = LocalContext.current
    val userData = remember { getUserData(context ) }




    val scheduleState by scheduleViewModel.scheduleResult.observeAsState(NetworkResponse.Idle)

    LaunchedEffect(Unit) {
        scheduleViewModel.getAllSchedules(context)
    }


    LaunchedEffect(scheduleState) {
        if (scheduleState is NetworkResponse.Success) {
            val data = (scheduleState as NetworkResponse.Success).data
            viewModel.prepareAiContext(userData, data)

        }
    }



    LaunchedEffect(viewModel.messages.size, viewModel.isLoading) {
        if (viewModel.messages.isNotEmpty() || viewModel.isLoading) {
            val totalItems = viewModel.messages.size + (if (viewModel.isLoading) 1 else 0)
            listState.animateScrollToItem(totalItems - 1)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)

            .statusBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // 1. Шапка
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = primaryColor)

                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ali AI", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = SejongText)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { activity?.finish() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = primaryColor)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )

            // 2. Список (Сверху вниз)
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 10.dp, bottom = 20.dp)
            ) {
                itemsIndexed(viewModel.messages) { _, msg ->
                    AnimatedMessageItem(msg)
                }

                if (viewModel.isLoading) {
                    item {
                        TypingIndicator()
                    }
                }
            }


            Column(modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = SejongCard),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = { Text("Спроси у Али ИИ...", color = SejongText.copy(alpha = 0.5f)) },
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = SejongText,
                                unfocusedTextColor = SejongText,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )

                        FloatingActionButton(
                            onClick = {
                                if (inputText.isNotBlank()) {
                                    viewModel.sendMessage(inputText)
                                    inputText = ""
                                }
                            },
                            containerColor = primaryColor,
                            shape = CircleShape,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, tint = Color.White)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun AnimatedMessageItem(message: Message) {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible = true }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(500)) +
                slideInVertically(initialOffsetY = { 40 }, animationSpec = tween(500)) +
                scaleIn(initialScale = 0.85f, animationSpec = tween(500, easing = FastOutSlowInEasing)),
        exit = fadeOut()
    ) {
        ChatBubble(message)
    }
}



@Composable
fun ChatBubble(message: Message) {
    val isAI = message.isFromAI
    val align = if (isAI) Alignment.CenterStart else Alignment.CenterEnd

    val bubbleColor = if (isAI) Color.White else UserBubble
    val textColor = if (isAI) Color(0xFF2D2D2D) else Color.White


    Box (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = align

    ) {
        Card (
            shape = RoundedCornerShape(
                topStart = 20.dp,
                topEnd = 20.dp,
                bottomStart = if (isAI) 4.dp else 20.dp
            ),
            modifier = Modifier.widthIn(max = 310.dp),
            colors = CardDefaults.cardColors(containerColor = bubbleColor),
            elevation = CardDefaults.cardElevation(3.dp)

        ) {
            Column (
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)

            ) {
                   if (isAI) {
                       Text (
                           text = "Ali AI ✨",
                           color = primaryColor,
                           fontSize = 12.sp,
                           fontWeight = FontWeight.Bold,
                           modifier = Modifier
                               .padding(bottom = 4.dp)

                       )
                   }
                Text (
                    text = message.text,
                    color = textColor,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    style = LocalTextStyle.current.copy(
                        letterSpacing = 0.3.sp
                    )
                )

                Spacer (modifier = Modifier.height(8.dp))

                Text (
                    text = message.time ?: "",
                    color = textColor.copy(alpha = 0.4f),
                    fontSize = 10.sp,
                    modifier = Modifier.align(Alignment.End)


                )
            }
        }
    }
}

@Composable
fun TypingIndicator() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(18.dp, 18.dp, 18.dp, 2.dp),
            colors = CardDefaults.cardColors(containerColor = AiBubble),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Али ИИ печатает",
                    color = SejongText.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    LoadingDot(0)
                    LoadingDot(200)
                    LoadingDot(400)
                }
            }
        }
    }
}

@Composable
fun LoadingDot(delay: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "dot")
    val dy by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -6f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, delayMillis = delay, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dy"
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, delayMillis = delay),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .offset(y = dy.dp)
            .size(6.dp)
            .clip(CircleShape)
            .background(primaryColor.copy(alpha = alpha))
    )
}

