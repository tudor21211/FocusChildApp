package com.example.focuschildapp.Navigation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable

import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.focus.Presentation.Screens.Landing.LandingScreen
import com.example.focuschildapp.ScreensPkg.Accessibility
import com.example.focuschildapp.ScreensPkg.DisplayOverOtherApps
import com.example.focuschildapp.ScreensPkg.MainPage
import com.example.focuschildapp.ScreensPkg.PermissionScreen
import com.example.focuschildapp.ScreensPkg.UsageAccess
import com.google.accompanist.navigation.animation.AnimatedNavHost

import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    context : Context
) {

    AnimatedNavHost(navController = navController, startDestination = Screens.LandingScreen.route) {
        composable(Screens.LandingScreen.route) {
            LandingScreen(navController)
        }
        composable(Screens.PermissionsScreen.route) {
            PermissionScreen(navController)
        }
        composable(Screens.MainPage.route) {
            MainPage(navController)
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
    }
}