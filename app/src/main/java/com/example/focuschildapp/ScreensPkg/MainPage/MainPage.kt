package com.example.focuschildapp.com.example.focuschildapp.ScreensPkg.MainPage

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun MainPage(
    navController: NavController,
    viewModel: MainPageViewModel = hiltViewModel()
) {

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Text(text = "Signed In as ${viewModel.getUserEmail()} ")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = screenWidth * 0.05f, end = screenWidth * 0.05f)
            .border(1.dp, Color.Black)
    ){

    }

}