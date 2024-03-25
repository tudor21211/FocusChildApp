package com.example.focuschildapp.Navigation

sealed class Screens (val route : String){
    object LandingScreen : Screens(route = "landingScreen")
    object PermissionsScreen : Screens(route = "permissionsScreen")
    object MainPage : Screens(route = "mainPage")
}