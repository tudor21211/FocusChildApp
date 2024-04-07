package com.example.focuschildapp

import android.app.admin.DevicePolicyManager
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.focuschildapp.Navigation.Screens
import com.example.focuschildapp.Navigation.SetupNavGraph
import com.example.focuschildapp.Permissions.PermissionFunctions
import com.example.focuschildapp.ui.theme.FocusChildAppTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val sharedPreferences = this.getSharedPreferences("TutorialPermissionsFinished", MODE_PRIVATE)

        if (requestCode == 100) {
            handlePermissionResult(sharedPreferences, Screens.PermissionsScreen)
        } else if (requestCode == 200) {
            handlePermissionResult(sharedPreferences, Screens.PermissionsScreen)
        } else if (requestCode == 300) {
            handlePermissionResult(sharedPreferences, Screens.PermissionsScreen)
        }
    }

    private fun handlePermissionResult(sharedPreferences: SharedPreferences, screen: Screens) {
        println("ALL PERMISSIONS ENABLED ${PermissionFunctions(this, packageName).areAllPermissionsEnabled()}")
        if (PermissionFunctions(this, packageName).areAllPermissionsEnabled()) {
            val editor = sharedPreferences.edit()
            editor.putBoolean("TutorialPermissionsFinished", true)
            editor.apply()
            navController.navigate(Screens.MainPage.route)
        } else {
            navController.navigate(screen.route) {
                popUpTo(screen.route) {
                    inclusive = true
                }
            }
        }
    }

    @Composable
    private fun AuthState() {
        val isUserSignedOut = viewModel.getAuthState().collectAsState().value
        if (isUserSignedOut) { //we are not connected
            NavigateToLandingPage()
        } else if(!isUserSignedOut && !PermissionFunctions(this, packageName).areAllPermissionsEnabled()) { // we are connected but we didnt accept all the permissions
            NavigateToPermissions()
        }
        else {
            NavigateToMainScreen() //we are connected and we accepted the permissions -> navigate to mainPage
        }
    }

    @Composable
    private fun NavigateToLandingPage() {
        navController.navigate(Screens.LandingScreen.route) {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
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
    private fun NavigateToPermissions(){
        navController.navigate(Screens.PermissionsScreen.route){
            popUpTo(navController.graph.id){
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

