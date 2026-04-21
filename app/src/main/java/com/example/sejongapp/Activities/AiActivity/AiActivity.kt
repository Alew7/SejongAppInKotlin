package com.example.sejongapp.Activities.AiActivity

import LocalData.getUserData
import android.R.attr.text
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import com.example.sejongapp.R
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.ModifierLocalReadScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.sejongapp.Activities.AiActivity.AiViewModel.AIChatViewModel
import com.example.sejongapp.Activities.AiActivity.AiViewModel.AIChatViewModelFactory
import com.example.sejongapp.Activities.AiActivity.AiViewModel.AiRepository
import com.example.sejongapp.Activities.AiActivity.DataClass.Message
import com.example.sejongapp.Activities.ProfileActivity.ui.theme.AiBubble
import com.example.sejongapp.Activities.ProfileActivity.ui.theme.SejongCard
import com.example.sejongapp.Activities.ProfileActivity.ui.theme.SejongText
import com.example.sejongapp.Activities.ProfileActivity.ui.theme.UserBubble
import com.example.sejongapp.Activities.ProfileActivity.ui.theme.WarmBeige
import com.example.sejongapp.models.ViewModels.UserViewModels.ScheduleViewModel
import com.example.sejongapp.retrofitAPI.NetworkResponse
import com.example.sejongapp.retrofitAPI.RetrofitInstance
import com.example.sejongapp.ui.theme.backgroundColor
import com.example.sejongapp.ui.theme.primaryColor
import com.example.sejongapp.utils.LocaleHelper
import kotlinx.coroutines.launch


val lottieEmojiMap = mapOf(
    "👋" to "hand_wave.lottie",
    "🕒" to "Alarm_clock.lottie",
    "⏰" to "Alarm_clock.lottie",
    "🚪" to "door_open.lottie",
    "📍" to "door_open.lottie",
    "📅" to "Calendar_animation.lottie",
    "🗓️" to "Calendar_animation.lottie",
    "😊" to "blushing_emoji.lottie",
    "✨" to "Calendar_animation.lottie"
)
class AiActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            AIChatScreen()
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val lang = LocalData.getSavedLanguage(newBase) ?: "ENG" // store selected lang
        val context = LocaleHelper.setLocale(newBase, lang)
        super.attachBaseContext(context)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AIChatScreen(

        scheduleViewModel: ScheduleViewModel = viewModel()

    ) {


        val focusManager = LocalFocusManager.current
        val context = LocalContext.current
        val activity = LocalActivity.current

        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()


        val api = remember { RetrofitInstance.aiApi }
        val repository = remember { AiRepository(api) }


        val viewModel: AIChatViewModel = viewModel(
            factory = AIChatViewModelFactory(repository)
        )

        // Состояние ввода и данных
        var inputText by remember { mutableStateOf("") }
        val listState = rememberLazyListState()
        val userData = remember { getUserData(context) }
        val userName = userData?.fullname ?: ""
        val scheduleState by scheduleViewModel.scheduleResult.observeAsState(NetworkResponse.Idle)
        val isContextReady = scheduleState is NetworkResponse.Success




        LaunchedEffect(Unit) {
            scheduleViewModel.getAllSchedules(context)
            viewModel.loadHistory(context)
        }


        LaunchedEffect(scheduleState) {
            if (scheduleState is NetworkResponse.Success) {
                val data = (scheduleState as NetworkResponse.Success).data
                viewModel.prepareAiContext(userData!!, data)

            }
        }



        LaunchedEffect(viewModel.messages.size, viewModel.isLoading) {
            if (viewModel.messages.isNotEmpty() || viewModel.isLoading) {
                val totalItems = viewModel.messages.size + (if (viewModel.isLoading) 1 else 0)
                listState.animateScrollToItem(totalItems - 1)
            }
        }

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier
                        .width(330.dp)
                        .fillMaxHeight(),

                    drawerShape = RoundedCornerShape(topEnd = 32.dp, bottomEnd = 32.dp),

                    drawerContainerColor = backgroundColor,
                    drawerTonalElevation = 0.dp
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .drawBehind {

                                val brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.15f),
                                        Color.White.copy(alpha = 0.05f)
                                    )
                                )
                                drawRect(brush = brush)
                            }
                            .drawBehind {
                                // Тонкая белая рамка слева для эффекта стекла
                                val strokeWidth = 1.dp.toPx()
                                drawLine(
                                    color = Color.White.copy(alpha = 0.2f),
                                    start = Offset(0f, 0f),
                                    end = Offset(0f, size.height),
                                    strokeWidth = strokeWidth
                                )
                            }
                            .padding(horizontal = 16.dp)
                    ) {
                        Spacer(modifier = Modifier.height(70.dp))

                        // 1. Элитная шапка Профиля
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 30.dp)
                        ) {
                            // Аватарка с градиентной рамкой
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .drawBehind {
                                        val strokeWidth = 2.dp.toPx()
                                        // Градиент от твоегоprimaryColor к WarmBeige
                                        val brush = Brush.linearGradient(
                                            colors = listOf(primaryColor, WarmBeige),
                                            start = Offset(0f, 0f),
                                            end = Offset(size.width, size.height)
                                        )
                                        drawCircle(
                                            brush = brush,
                                            radius = size.minDimension / 2 + strokeWidth / 2,
                                            style = Stroke(width = strokeWidth)
                                        )
                                    }
                                    .background(Color.White.copy(alpha = 0.2f), CircleShape)
                                    .padding(2.dp)
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(userData?.avatar),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = userName,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SejongText,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    letterSpacing = (-0.5).sp
                                )
                                Text(
                                    text = context.getString(R.string.ali_history), // "твоя история Ali AI"
                                    fontSize = 13.sp,
                                    color = SejongText.copy(alpha = 0.7f),
                                    letterSpacing = (-0.2).sp
                                )
                            }
                        }


                        Card(
                            onClick = {
                                viewModel.startNewChat()
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        primaryColor
                                    )
                                    .padding(horizontal = 20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = context.getString(R.string.start_new_chat), // "Начать Новый Чат"
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 16.sp,
                                        letterSpacing = (-0.3).sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        // 3. Список истории
                        Text(
                            text = context.getString(R.string.start_new_chat),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SejongText.copy(alpha = 0.5f),
                            modifier = Modifier.padding(start = 8.dp, bottom = 10.dp),
                            letterSpacing = 1.sp
                        )

                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(bottom = 20.dp)
                        ) {

                            val groupedHistory = viewModel.historyMessages
                                .filter { !it.isFromAI }
                                .distinctBy { it.chatID }
                                .reversed()

                            itemsIndexed(groupedHistory) { index, msg ->

                                val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                                val scale by infiniteTransition.animateFloat(
                                    initialValue = 1f,
                                    targetValue = 1.01f,
                                    animationSpec = infiniteRepeatable(
                                        animation = tween(2000),
                                        repeatMode = RepeatMode.Reverse
                                    ),
                                    label = "scale"
                                )

                                Card(
                                    onClick = {
                                        viewModel.openHistoryChat(msg.chatID)


                                        scope.launch { drawerState.close() }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .graphicsLayer {
                                            scaleX = scale
                                            scaleY = scale
                                        }
                                        .drawBehind {

                                            val strokeWidth = 1.dp.toPx()
                                            drawRoundRect(
                                                color = Color.White.copy(alpha = 0.15f),
                                                size = size,
                                                cornerRadius = CornerRadius(16.dp.toPx()),
                                                style = Stroke(width = strokeWidth)
                                            )
                                        },
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = SejongCard.copy(alpha = 0.3f),
                                        contentColor = SejongText
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 14.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.Menu,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp),
                                            tint = SejongText.copy(alpha = 0.5f)
                                        )

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Text(
                                            text = msg.text,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = SejongText,
                                            modifier = Modifier.weight(1f),
                                            letterSpacing = (-0.3).sp
                                        )

                                        Text(
                                            text = msg.time ?: "",
                                            fontSize = 11.sp,
                                            color = SejongText.copy(alpha = 0.5f),
                                            modifier = Modifier.padding(start = 8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )

        {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .statusBarsPadding()
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            focusManager.clearFocus()
                        })
                    }
            ) {
                Column(modifier = Modifier.fillMaxSize()) {

                    // 1. Шапка
                    CenterAlignedTopAppBar(
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = primaryColor
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    "Ali AI",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = SejongText
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch { drawerState.open() }
                            }) {
                                Icon(
                                    Icons.Default.Menu,
                                    contentDescription = null,
                                    tint = primaryColor
                                )
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
                    )

                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        if (viewModel.messages.isEmpty()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 24.dp),
                                verticalArrangement = Arrangement.Top,
                            ) {

                                Spacer(modifier = Modifier.weight(0.2f))

                                Text(
                                    text = context.getString(R.string.hello) + "$userName!", // "Здравствуйте, $userName!"
                                    color = SejongText,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Medium
                                )

                                Text(
                                    text = context.getString(R.string.where_start), //"С чего начнём?"
                                    fontSize = 32.sp,
                                    color = SejongText,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(32.dp))

                                val chips = listOf(
                                    "📅" to context.getString(R.string.lesson_today),
                                )

                                chips.forEach { (emoji, description) ->
                                    Card(
                                        onClick = {
                                            if (isContextReady) {
                                                viewModel.sendMessage(description, context)
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    context.getString(R.string.loading_schedule),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 6.dp),
                                        shape = RoundedCornerShape(20.dp),
                                        colors = CardDefaults.cardColors(containerColor = SejongCard),
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(16.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            // Показываем и эмодзи, и текст
                                            Text(text = emoji, fontSize = 20.sp)
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(
                                                text = description,
                                                color = SejongText,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.weight(1f))

                            }
                        } else {
                            // 2. Список
                            LazyColumn(
                                state = listState,
                                modifier = Modifier
                                    .fillMaxSize()
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

                        }
                    }

                    Column(
                        modifier = Modifier
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
                                    placeholder = {
                                        Text(
                                            text = context.getString(R.string.ask_ali),
                                            color = SejongText.copy(alpha = 0.5f)
                                        )
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = TextFieldDefaults.colors(
                                        focusedTextColor = SejongText,
                                        unfocusedTextColor = SejongText,
                                        focusedContainerColor = Color.Transparent,
                                        cursorColor = primaryColor,
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    )
                                )

                                FloatingActionButton(
                                    onClick = {
                                        if (inputText.isNotBlank()) {
                                            viewModel.sendMessage(inputText, context)
                                            inputText = ""
                                        }
                                    },
                                    containerColor = primaryColor,
                                    shape = CircleShape,
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Send,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
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
                    scaleIn(
                        initialScale = 0.85f,
                        animationSpec = tween(500, easing = FastOutSlowInEasing)
                    ),
            exit = fadeOut()
        ) {
            ChatBubble(message)
        }
    }


    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    fun ChatBubble(message: Message) {
        val isAI = message.isFromAI
        val align = if (isAI) Alignment.CenterStart else Alignment.CenterEnd
        val bubbleColor = if (isAI) Color.White else UserBubble
        val textColor = if (isAI) Color(0xFF2D2D2D) else Color.White

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            contentAlignment = align
        ) {
            Card(
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomStart = if (isAI) 4.dp else 20.dp,
                    bottomEnd = if (isAI) 20.dp else 4.dp
                ),
                modifier = Modifier
                    .widthIn(max = 320.dp)
                    .wrapContentWidth(),

                colors = CardDefaults.cardColors(containerColor = bubbleColor),
                elevation = CardDefaults.cardElevation(3.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    if (isAI) {
                        Text(
                            text = "Ali AI ✨",
                            color = primaryColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }

                    FlowRow(
                        modifier = Modifier.wrapContentWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalArrangement = Arrangement.Center
                    ) {

                        val words = message.text.replace("\n", " \n ").split(" ")


                        words.forEach { word ->
                            if (word == "\n") {
                                Spacer(modifier = Modifier.fillMaxWidth().height(4.dp))
                            } else {

                                var currentWord = word.replace("*", "")
                                var lottieToDraw: String? = null

                                lottieEmojiMap.forEach { (emoji, asset) ->
                                    if (currentWord.contains(emoji)) {
                                        lottieToDraw = asset
                                        currentWord = currentWord.replace(emoji, "")
                                    }
                                }

                                if (lottieToDraw != null) {
                                    LottieEmoji(assetName = lottieToDraw!!, size = 50.dp)
                                }

                                if (currentWord.trim().isNotEmpty()) {
                                    Text(
                                        text = "$currentWord ",
                                        color = textColor,
                                        fontSize = 16.sp,
                                        lineHeight = 24.sp,
                                        modifier = Modifier.align(Alignment.CenterVertically)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun TypingIndicator() {

        val context = LocalContext.current
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
                        text = context.getString(R.string.ali_typing),
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


    @Composable
    fun LottieEmoji(assetName: String, size: Dp = 50.dp) {

        val composition by rememberLottieComposition(LottieCompositionSpec.Asset(assetName))

        val isCalendar = assetName == "Calendar_animation.lottie"

        val scaleX = if (isCalendar) 1.4051f else 1f
        val scaleY = if (isCalendar) 1.4556f else 1f

        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = 1,
            restartOnPlay = false
        )

        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .size(size)
                .padding(horizontal = 4.dp)
                .graphicsLayer {
                    this.scaleX = scaleX
                    this.scaleY = scaleY

                    transformOrigin = androidx.compose.ui.graphics.TransformOrigin.Center
                }
        )
    }
}

