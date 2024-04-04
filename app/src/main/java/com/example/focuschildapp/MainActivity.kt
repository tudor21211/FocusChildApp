package com.example.focuschildapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.focuschildapp.Navigation.Screens
import com.example.focuschildapp.Navigation.SetupNavGraph
import com.example.focuschildapp.ui.theme.FocusChildAppTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalAnimationApi::class)
    private lateinit var navController: NavHostController
    private val viewModel by viewModels<MainViewModel>()

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            navController = rememberAnimatedNavController()
            SetupNavGraph(navController = navController, this)
            AuthState()
        }
    }

    @Composable
    private fun AuthState() {
        val isUserSignedOut = viewModel.getAuthState().collectAsState().value
        if (isUserSignedOut) {
            NavigateToSignInScreen()
        } else {
            NavigateToMainScreen()
        }
    }

    @Composable
    private fun NavigateToSignInScreen() {
        navController.navigate(Screens.LoginScreen.route) {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
        }
    }

    @Composable
    private fun NavigateToMainScreen() {
        navController.navigate(Screens.MainPage.route) {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
        }
    }
}

