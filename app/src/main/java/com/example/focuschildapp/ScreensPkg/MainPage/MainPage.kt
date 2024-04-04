package com.example.focuschildapp.com.example.focuschildapp.ScreensPkg.MainPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.focuschildapp.Navigation.Screens
import com.example.focuschildapp.R
import com.example.focuschildapp.com.example.focuschildapp.Utils.QrGenerate
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MainPage(
    navController: NavController,
    viewModel: MainPageViewModel = hiltViewModel()
) {

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    var showDialog by remember { mutableStateOf(false) }

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
            title = { Text(text = "Sign Out") },
            text = { Text(text = "Are you sure you want to sign out?") },
            confirmButton = {
                Button(
                    onClick = {
                        try {
                            navController.navigate(Screens.LoginScreen.route)
                            showDialog = false
                            FirebaseAuth.getInstance().signOut()

                        }
                        catch (e: Exception) {println("TRIED TO SIGN OUT ${e.message}")}
                    }
                ) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        //onNoClicked()
                        showDialog = false
                    }
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
            .border(1.dp, Color.Black),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ){
        Row(){
            Text(text = "User: ", fontSize = 25.sp)
            Text(text ="${viewModel.getUserEmail()}", fontSize = 25.sp)
        }
        Spacer(modifier = Modifier.fillMaxHeight(.2f))
        Text(
            text = "Please scan the following QR code to connect to the parent app",
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.opensans_medium)),
            fontSize = 25.sp
            )

        QRCodeFromByteArray(byteArray = viewModel.getUserUid()?.toByteArray() ?: byteArrayOf())
    }

}


@Composable
fun QRCodeFromByteArray(byteArray: ByteArray) {


    if (byteArray.isEmpty()) {
        // Handle the case when the byte array is empty
        return
    }

    var bitmap = QrGenerate.generateQRCodeBitmap(byteArray)

    Box(modifier = Modifier
        .padding(20.dp)
        .border(1.dp, Color.Black)
        .fillMaxHeight(.4f)) {
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(1f)
            )
        }
    }
}