package com.example.focuschildapp.ScreensPkg

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.focuschildapp.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun DisplayOverOtherApps(navController: NavController) {
    val context = LocalContext.current
    val systemUiController = rememberSystemUiController()
    val colorStops = arrayOf(
        0.2f to Color(0xFF0E4B4D),
        0.5f to Color(0xFF10585A),
        1f to Color(0xFF126366)
    )

    val colorStopsButton = arrayOf(
        0.2f to Color(0xFF09F5A3),
        0.5f to Color(0xFF098358),
        1f to Color(0xFF012E1E)
    )

    val requestCode = 300
    SideEffect {
        systemUiController.setSystemBarsColor(Color(0xFF0E0653))
    }
    val listOfInstructions = listOf(
        "Click the button below to open the Settings app.",
        "Find Focus. in the list of apps and click on it.",
        "Turn on \"Display over other apps\".",
    )

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .background(Brush.linearGradient(colorStops = colorStops))
            .fillMaxSize()
            .verticalScroll(state = scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Text(
            text = "To effectively block inappropriate content, our app requires the 'App Overlay' permission. This allows us to display warnings and restrictions on top of other apps without interruption. Your child's privacy is paramount, and we only use this permission to ensure their safe browsing experience. Thank you for understanding and prioritizing your child's safety with our app!",
            color = Color.White,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(if (LocalConfiguration.current.screenWidthDp > 600) .9f else 1f),
            textAlign = TextAlign.Justify,
            fontSize = 19.sp,
            fontFamily = FontFamily(Font(R.font.opensans_res))
        )
        Spacer(modifier = Modifier.height(60.dp))
        Image(
            painter = painterResource(id = R.drawable.info),
            contentDescription = "info image",
            Modifier.fillMaxSize(.1f)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = "How to activate the permission?",
                color = Color.White,
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.opensans_res))
            )
        }

        Column {
            listOfInstructions.forEach {
                Row (Modifier.padding(start = if (LocalConfiguration.current.screenWidthDp > 600) 50.dp else 0.dp )) {
                    Text(
                        text = "\u2022",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(20.dp),
                        color = Color.White,
                        fontSize = 19.sp
                    )
                    Text(
                        text = it,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.weight(1f, fill = true),
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.opensans_res)),
                        fontSize = 16.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        GifImage(modifier = Modifier.fillMaxWidth(), R.drawable.overlay)
        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = {
                val settingsIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                (context as Activity).startActivityForResult(settingsIntent, requestCode)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF000000)),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp)
                .fillMaxWidth(.8f)
                .clip(shape = RoundedCornerShape(10.dp))
                .border(
                    BorderStroke(
                        1.dp,
                        brush = Brush.linearGradient(colorStops = colorStopsButton)
                    ), RoundedCornerShape(100.dp)
                )
        ) {
            Text(
                text = "Go to settings",
                color = Color.White,
                fontSize = 20.sp,
                fontFamily = FontFamily(
                    Font(R.font.opensans_res)
                )
            )
        }

    }


}