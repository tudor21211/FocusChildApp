package com.example.focus.Presentation.Screens.Landing

import android.content.Context
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.focuschildapp.Navigation.Screens
import com.example.focuschildapp.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LandingScreen(navController: NavController) {

    val sharedPreferences = LocalContext.current.getSharedPreferences("TutorialPermissionsFinished", Context.MODE_PRIVATE)
    val tutorialPermissionsFinished = sharedPreferences.getBoolean("TutorialPermissionsFinished", false)

    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setSystemBarsColor(Color(0xFF3ACED3))
        systemUiController.setNavigationBarColor(Color.Black)
    }


    val openSans = FontFamily(
        Font(R.font.opensans_res),
    )
    val colorStops = arrayOf(
        0.2f to Color(0xFF3ACED3),
        0.5f to Color(0xFF34B4B8),
        1f to Color(0xFF0E4B4D)
    )
    Box(
        modifier = Modifier
            .fillMaxSize(1f)
            .background(Brush.linearGradient(colorStops = colorStops))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(1f)
                .padding(top = 50.dp)
        ) {


            Spacer(modifier = Modifier.weight(.6f))

            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .align(Alignment.CenterHorizontally)
                    .wrapContentSize()
            ) {
                Column {
                    Text(
                        text = "Focus.",
                        color = Color.White,
                        fontFamily = openSans,
                        fontSize = 100.sp
                    )
                    Text(
                        text = "child app",
                        color = Color.White,
                        fontFamily = openSans,
                        fontSize = 40.sp
                    )
                }
            }
            Spacer(modifier = Modifier.weight(.9f))


            Button(
                onClick = {
                    println("tutorial permission $tutorialPermissionsFinished")
                    navController.navigate(Screens.LoginScreen.route)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A0A05)),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 20.dp)
                    .fillMaxWidth(.9f)
                    .fillMaxHeight(.08f)
                    .clip(shape = RoundedCornerShape(10.dp))
            ) {
                Text(
                    text = "Get Started",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.opensans_res))
                )
            }
            Spacer(modifier = Modifier.weight(.1f))
        }


    }


}


@ExperimentalAnimationApi
fun addAnimation(duration: Int = 150): ContentTransform {
    return slideInVertically(animationSpec = tween(durationMillis = duration)) { height -> height } + fadeIn(
        animationSpec = tween(durationMillis = duration)
    ) with slideOutVertically(animationSpec = tween(durationMillis = duration)) { height -> -height } + fadeOut(
        animationSpec = tween(durationMillis = duration)
    )
}





