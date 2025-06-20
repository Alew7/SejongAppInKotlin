package com.example.sejongapp.AnnousmentActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class AnnousmentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnnousmentDetailPage(AnnousmentActivity())
        }
    }
}

/*
@Composable
fun AnnousmentDetailPage() {

    val text_size = 15.sp
    val scrollState = rememberScrollState()



    BoxWithConstraints  (
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        val screenWidth = maxWidth
        val startPadding = screenWidth * 0.5f
        val endPadding = screenWidth * 0.5f

        Column  (
            modifier = Modifier
                .verticalScroll(scrollState)
        ){
            Box(
                modifier = Modifier

                    .fillMaxWidth()
                    .padding(top = 25.dp)
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
                    painter = painterResource(R.drawable.ic_head),
                    contentDescription = "ic_had",
                    modifier = Modifier
                        .size(65.dp)
                        .align(Alignment.TopEnd)
                        .padding(end = 25.dp)
                )
                Image(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = "ic_back",
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .clickable  (
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        )
                        {
                            AnnousmentActivity().finish()
                            exitProcess(0)
                        }


                )
            }
            Text(
                text = "11.02.2025",
                modifier = Modifier
                    .padding(top = 10.dp,start = 15.dp)
            )
            Text (
                text = "Dushanbe 3 Sejong Institute: A\nHub of Cultural Exchange and\nLanguage Mastery",
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                modifier = Modifier
                    .padding(start = 15.dp)
            )

            Column (
                modifier = Modifier
                    .padding(bottom = 40.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Image(
                    painter = painterResource(R.drawable.annousment_img),
                    contentDescription = "annousment_img",
                    modifier = Modifier
                        .size(350.dp)

                )
                Text (
                    text = "In the heart of Dushanbe, the Dushanbe 3 Sejong\nInstitute stands as a vibrant center for the promotion\nof Korean language and culture, fostering a deeper\nunderstanding between Tajikistan and South Korea.\nSince its establishment,the instite has witnessed\n a remarkable surge in interest, with students of all ages\neagerto explore the rich tapestry of Korean\ntraditions.\n   The institute's comprehensive curreculum extends\nbeyond basic language instruction, offering a diverse\nrange of programs that delve  into the intricaries of\nKorean history, art, and contemporary culture.From\ntraditional calligraphy and cooking classes to modern\nK-pop dance workshops,the Sejong Institute\nprovides a holistic cultural experience.",
                    fontSize = text_size,

                    )

            }
        }
    }
}


*/