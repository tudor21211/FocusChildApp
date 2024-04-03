package com.example.focuschildapp.ScreensPkg

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.focuschildapp.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, context: Context) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val systemUiController = rememberSystemUiController()
    val registerButtonGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF3ACED3),
            Color(0xFF338C8F),
            Color(0xFF336D6F)
        )
    )

    SideEffect {
        systemUiController.setSystemBarsColor(Color(0x0FFFFFFF))
        systemUiController.setNavigationBarColor(Color.Black)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = screenWidth * 0.05f, end = screenWidth * 0.05f)
        //.border(1.dp, Color.Black)
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(.05f))
        Box() {
            Column() {
                Text(
                    text = "Log in",
                    fontFamily = FontFamily(Font(R.font.opensans_res)),
                    fontSize = 30.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = Color(0xFF336D6F)
                )
                Text(
                    text = "Please enter your details",
                    fontFamily = FontFamily(Font(R.font.opensans_res)),
                    color = Color(0xFFA4AAAA)
                )
            }
        }
        Spacer(modifier = Modifier.fillMaxHeight(.1f))
        Box() {
            Column {
                registerCards(
                    topText = "Your username",
                    prompt = "Enter your username",
                    icon = Icons.Default.AccountCircle,
                    false
                )
                registerCards(
                    topText = "Password",
                    prompt = "Enter your password",
                    icon = Icons.Default.Lock,
                    true
                )
                registerButton(
                    linearGradient = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFD0FDFF),
                            Color(0xFFA8EFF3),
                            Color(0xFF6DF1F8)
                        )
                    ),
                    "Login",
                    Color.Black
                )
                Spacer(modifier = Modifier.fillMaxHeight(.1f))
                Text(
                    text = "Don't have an account?",
                    fontFamily = FontFamily(Font(R.font.opensans_res)),
                    fontSize = 15.sp, color = Color.Black,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.fillMaxHeight(.05f))

                registerButton(
                    registerButtonGradient,
                    "Register",
                    Color.White)
            }

        }
    }


}



