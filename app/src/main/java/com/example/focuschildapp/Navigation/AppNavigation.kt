package com.example.focuschildapp.Navigation

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.focus.Presentation.Screens.Landing.LandingScreen
import com.example.focuschildapp.ScreensPkg.Accessibility
import com.example.focuschildapp.ScreensPkg.DisplayOverOtherApps
import com.example.focuschildapp.ScreensPkg.PermissionScreen
import com.example.focuschildapp.ScreensPkg.RegisterScreen.RegisterScreen
import com.example.focuschildapp.ScreensPkg.UsageAccess
import com.example.focuschildapp.Utils.ProgressBar
import com.example.focuschildapp.com.example.focuschildapp.ScreensPkg.LoginScreen.LoginScreen
import com.example.focuschildapp.com.example.focuschildapp.ScreensPkg.MainPage.MainPage

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    context : Context,
) {
    NavHost(navController = navController, startDestination = Screens.LandingScreen.route) {

        composable(Screens.LandingScreen.route) {
            LandingScreen(navController)
        }
        composable(Screens.PermissionsScreen.route) {
            PermissionScreen(navController)
        }
        composable(Screens.MainPage.route) {
            MainPage(navController, context)
        }
        composable(Screens.DisplayOverOtherApps.route){
            DisplayOverOtherApps(navController)
        }
        composable(Screens.Accessibility.route){
            Accessibility(navController)
        }
        composable(Screens.UsageAccess.route){
            UsageAccess(navController)
        }
        composable(Screens.RegisterScreen.route){
            RegisterScreen(navController, context, signUp = { email, password ->
                navController.navigate(Screens.PermissionsScreen.route)
            })
        }
        composable(Screens.LoginScreen.route){
            LoginScreen(navController, context)
        }
        composable(Screens.LoadingScreen.route){
            ProgressBar(navController)
        }


    }
}