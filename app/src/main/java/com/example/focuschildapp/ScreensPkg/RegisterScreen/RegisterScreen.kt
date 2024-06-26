package com.example.focuschildapp.ScreensPkg.RegisterScreen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.focuschildapp.Firebase.domain.Response
import com.example.focuschildapp.Navigation.Screens
import com.example.focuschildapp.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    context: Context,
    signUp: (email: String, password: String) -> Unit,
    viewModel : SignUpViewModel = hiltViewModel()
) {
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

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = screenWidth * 0.05f, end = screenWidth * 0.05f)
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(.05f))
        Box() {
            Column() {
                Text(
                    text = "Create account",
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
        val lifecycleOwner = LocalLifecycleOwner.current
        Spacer(modifier = Modifier.fillMaxHeight(.1f))
        Box() {
            Column {
                registerCards(
                    topText = "Your email",
                    prompt = "Enter your email",
                    icon = Icons.Default.AccountCircle,
                    false,
                    onValueChanged = { email = it }
                )
                registerCards(
                    topText = "Password",
                    prompt = "Enter your password",
                    icon = Icons.Default.Lock,
                    true,
                    onValueChanged = { password = it }
                )
                registerCards(
                    topText = "Repeat password",
                    prompt = "Repeat your password",
                    icon = Icons.Default.Lock,
                    true,
                    onValueChanged = { repeatPassword = it }
                )
                registerButton(
                    registerButtonGradient,
                    "Register",
                    Color.White,
                    onClick = {
                        handleRegisterClick(password, repeatPassword, context, viewModel, navController, email, lifecycleOwner)
                    })
                Spacer(modifier = Modifier.fillMaxHeight(.2f))
                Text(
                    text = "Already have an account?",
                    fontFamily = FontFamily(Font(R.font.opensans_res)),
                    fontSize = 15.sp, color = Color.Black,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.fillMaxHeight(.05f))
                registerButton(
                    linearGradient = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFD0FDFF),
                            Color(0xFFA8EFF3),
                            Color(0xFF6DF1F8)
                        )
                    ),
                    "Login",
                    Color.Black,
                    onClick = {
                        navController.navigate(Screens.LoginScreen.route)
                    }
                )

            }

        }
    }


}

fun handleRegisterClick(
    password: String,
    repeatPassword: String,
    context: Context,
    viewModel: SignUpViewModel,
    navController: NavController,
    email: String,
    lifecycleOwner: LifecycleOwner
) {
    if (password != repeatPassword) {
        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
    } else {
        viewModel.signUpWithEmailAndPassword(email, password)
        viewModel.signUpResponse.observe(lifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> {
                    //TODO Handle loading state
                }
                is Response.Success -> {
                    navController.navigate(Screens.PermissionsScreen.route)
                }
                is Response.Failure -> {
                    handleSignUpFailure(context, response)
                }
            }
        }
    }
}

fun handleSignUpFailure(context: Context, response: Response.Failure) {
    val exception: Exception = response.e
    when (exception) {
        is FirebaseAuthInvalidCredentialsException ->
            Toast.makeText(context, "Please input a valid email address", Toast.LENGTH_SHORT).show()
        is FirebaseAuthUserCollisionException ->
            Toast.makeText(context, "Email is already in use", Toast.LENGTH_SHORT).show()
        is FirebaseAuthWeakPasswordException ->
            Toast.makeText(context, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show()
        else ->
            Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun LoadingIndicator() {
    CircularProgressIndicator(
        modifier = Modifier
            .height(50.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun registerCards(topText: String, prompt: String, icon: ImageVector, isPasswordField : Boolean, onValueChanged: (String) -> Unit = {}, modifier: Modifier = Modifier) {
    val inputValue = remember { mutableStateOf(TextFieldValue()) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column() {
        Text(
            text = topText,
            fontFamily = FontFamily(Font(R.font.opensans_res)),
            fontSize = 15.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            modifier = Modifier.padding(bottom = 5.dp),
        )
        TextField(
            value = inputValue.value,
            onValueChange = {
                inputValue.value = it
                onValueChanged(it.text) },
            placeholder = { Text(prompt) },
            singleLine = true,
            visualTransformation = if (isPasswordField && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                IconButton(
                    onClick = {
                        passwordVisible = !passwordVisible
                    }) {
                    Icon(
                        icon,
                        contentDescription = "",
                        tint = Color.Black
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth(1f)
                .testTag(if (isPasswordField) "passwordField" else "emailField"),
            colors = TextFieldDefaults.textFieldColors(
                focusedTextColor = Color.Black,
                containerColor = Color(0xFFF0EDED),
                cursorColor = Color.Green,
                focusedIndicatorColor = Color(0xFF39E913),
                unfocusedIndicatorColor = Color(0xFF4FFDDA),
                disabledIndicatorColor = Color.Gray,
                focusedLabelColor = Color.White,
                unfocusedTrailingIconColor = Color(0xFF4FFDDA),
                focusedTrailingIconColor = Color(0xFF39E913)
            )
        )
        Spacer(modifier = Modifier.fillMaxHeight(.1f))
    }
}


@Composable
fun registerButton(
    linearGradient: Brush,
    text: String,
    buttonTextColor : Color,
    onClick : () -> Unit = { }){

    Column() {
        Spacer(modifier = Modifier.fillMaxHeight(.05f))
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(10.dp))
                .background(brush = linearGradient)
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(Color.Transparent)
        ) {
            Text(
                text = text,
                color = buttonTextColor,
                fontFamily = FontFamily(Font(R.font.opensans_res)),
                fontSize = 20.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
        }
    }
}

