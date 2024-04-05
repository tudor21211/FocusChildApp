package com.example.focuschildapp.Navigation

sealed class Screens (val route : String){
    object LandingScreen : Screens(route = "landingScreen")
    object PermissionsScreen : Screens(route = "permissionsScreen")
    object MainPage : Screens(route = "mainPage")
    object DisplayOverOtherApps : Screens(route = "displayOverOtherApps")
    object Accessibility : Screens(route = "accessibility")
    object UsageAccess : Screens(route = "usageAccess")
    object RegisterScreen : Screens (route = "registerScreen")
    object LoginScreen : Screens (route = "loginScreen")
    object LoadingScreen : Screens (route = "loadingScreen")
}