package com.example.focuschildapp.com.example.focuschildapp.ScreensPkg.MainPage

import android.content.Context
import android.content.Intent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.focuschildapp.Navigation.Screens
import com.example.focuschildapp.R
import com.example.focuschildapp.Utils.ProgressBar
import com.example.focuschildapp.com.example.focuschildapp.Services.ServerService
import com.example.focuschildapp.com.example.focuschildapp.Utils.QrGenerate
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainPage(
    navController: NavController,
    context : Context,
    viewModel: MainPageViewModel = hiltViewModel()
) {


    val i: Intent = Intent(context, ServerService::class.java)
    context.startService(i)


    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    var showDialog by remember { mutableStateOf(false) }
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setSystemBarsColor(Color(0xFF34495e))
        systemUiController.setNavigationBarColor(Color.Black)
    }

    var connectionStatus by remember { mutableStateOf("Not connected") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF34495e))
    ) {
        IconButton(onClick = {
            showDialog = true
        }) {
            Icon(
                imageVector = Icons.Filled.Clear,
                contentDescription = "Sign Out",
                tint = Color(0xFF000000),
                modifier = Modifier
                    .size(40.dp)
                    .padding(top = 1.dp),
            )
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                containerColor = Color.Black,
                title = { Text(text = "Sign Out", color = Color.White) },
                text = { Text(text = "Are you sure you want to sign out?", color = Color.White) },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            FirebaseAuth.getInstance().signOut()
                            navController.navigate(Screens.LoginScreen.route)

                        },
                        colors = ButtonDefaults.buttonColors(Color.Transparent)
                    ) {
                        Text(text = "Yes")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            //onNoClicked()
                            showDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(Color.Transparent)
                    ) {
                        Text(text = "No")
                    }
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = screenWidth * 0.05f, end = screenWidth * 0.05f)
                .background(Color(0xFF34495e)),
            //.border(1.dp, Color.Black),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,

            ) {
            Row(
                modifier = Modifier.padding(top = 10.dp)
            ) {
                //Text(text = "User: ", fontSize = 25.sp, color = Color.White, fontFamily = FontFamily(Font(R.font.opensans_medium)))
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "go to settings",
                    tint = Color(0xFF000000),
                    modifier = Modifier
                        .size(35.dp)
                )
                Text(text = "${viewModel.getUserEmail()}", fontSize = 25.sp, color = Color.White, fontFamily = FontFamily(Font(R.font.opensans_medium)))
            }
            Row(
                modifier = Modifier.padding(top = 10.dp)
            ){
                Text(text = "Current status: ", fontSize = 25.sp, color = Color.White, fontFamily = FontFamily(Font(R.font.opensans_medium)))
                Text(
                    text = connectionStatus,
                    fontSize = 25.sp,
                    color = if(connectionStatus == "Not connected") Color.Red else Color.Green,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.opensans_medium))
                )
            }

            Spacer(modifier = Modifier.fillMaxHeight(.15f))

            var cardFace by remember { mutableStateOf(FaceCardType.Back) }

            //QRCodeFromByteArray(byteArray = viewModel.getUserUid()?.toByteArray() ?: byteArrayOf())
            CardFlipper(
                cardType = cardFace,
                onCardClick = {
                    newCardFace ->
                    cardFace = newCardFace.nextFace
                },
                modifier = Modifier
                    .fillMaxHeight(.8f)
                    .fillMaxWidth(1f)
                    .padding(20.dp),
                backContent = {
                    // Back content
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF3F5974)),
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                            Text(
                                text = "In order to connect to the parent app, scan the QR code",
                                textAlign = TextAlign.Center,
                                fontFamily = FontFamily(Font(R.font.opensans_medium)),
                                fontSize = 20.sp,
                                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                                color = Color.White
                            )
                            Text(
                                text = "Click the card to show the QR code",
                                textAlign = TextAlign.Center,
                                fontFamily = FontFamily(Font(R.font.opensans_medium)),
                                fontSize = 20.sp,
                                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                                color = Color.White
                            )
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Sign Out",
                            tint = Color(0xFF000000),
                            modifier = Modifier
                                .size(40.dp)
                                .padding(top = 1.dp),
                        )

                    }
                },
                frontContent = {
                    // Front content
                    ElevatedQrCard(viewModel = viewModel)
                }
            )
        }

    }
}


@Composable
fun QRCodeFromByteArray(byteArray: ByteArray) {


    if (byteArray.isEmpty()) {
        //TODO Handle the case when the byte array is empty
        return
    }

    var bitmap = QrGenerate.generateQRCodeBitmap(byteArray)

    Box(modifier = Modifier
        .padding(10.dp)
        //.border(1.dp, Color.Black)
        .fillMaxSize(1f)) {
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(1f)
            )
        }
    }
}


@Composable
fun ElevatedQrCard(
    viewModel: MainPageViewModel
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxHeight(1f)
            .fillMaxWidth(1f)
            ,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3F5974)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 40.dp,
        ),
    ) {
        Column (
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(1f)
        ){
            Text(
                text = "Scan the QR code",
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.opensans_medium)),
                fontSize = 25.sp,
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                color = Color.White,

                )
            Text(
                text = "Use the parent app to scan the QR code to connect to the child app.",
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.opensans_medium)),
                fontSize = 15.sp,
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                color = Color(0xFFA4AAAA),

                )
            QRCodeFromByteArray(byteArray = viewModel.getUserUid()?.toByteArray() ?: byteArrayOf())
        }


    }
}



enum class FaceCardType(val angle: Float) {
    Front(0f) {
        override val nextFace: FaceCardType
            get() = FaceCardType.Back
    },
    Back(180f) {
        override val nextFace: FaceCardType
            get() = FaceCardType.Front
    };

    abstract val nextFace: FaceCardType
}

enum class SpinAxis {
    X,
    Y,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardFlipper(
    cardType: FaceCardType,
    onCardClick: (FaceCardType) -> Unit,
    modifier: Modifier = Modifier,
    axis: SpinAxis = SpinAxis.Y,
    backContent: @Composable () -> Unit = {},
    frontContent: @Composable () -> Unit = {},
) {
    val spinAnimation = animateFloatAsState(
        targetValue = cardType.angle,
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing,
        ), label = ""
    )
    ElevatedCard(
        onClick = { onCardClick(cardType) },
        elevation = CardDefaults.cardElevation(40.dp),
        modifier = modifier
            .graphicsLayer {
                if (axis == SpinAxis.X) {
                    rotationX = spinAnimation.value
                } else {
                    rotationY = spinAnimation.value
                }
                cameraDistance = 10f * density
            },
    ) {
        if (spinAnimation.value <= 90f) {
            Box(
                Modifier.fillMaxSize()
            ) {
                frontContent()
            }
        } else {
            Box(
                Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        if (axis == SpinAxis.X) {
                            rotationX = 180f
                        } else {
                            rotationY = 180f
                        }
                    },
            ) {
                backContent()
            }
        }
    }
}
