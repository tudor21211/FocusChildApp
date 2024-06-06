package com.example.focuschildapp

import android.Manifest
import android.app.usage.UsageStatsManager
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.focuschildapp.Navigation.Screens
import com.example.focuschildapp.Navigation.SetupNavGraph
import com.example.focuschildapp.Permissions.PermissionFunctions
import com.example.focuschildapp.com.example.focuschildapp.RoomDB.AppTimeSpentEntity
import com.example.focuschildapp.com.example.focuschildapp.Services.ServerService
import com.example.focuschildapp.com.example.focuschildapp.Utils.GetAppsFunctions
import com.example.websocket.RoomDB.AppDatabase
import com.example.websocket.RoomDB.PackageViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var packagesViewModel: PackageViewModel
    private lateinit var appDatabase: AppDatabase
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appDatabase = AppDatabase.getDatabase(applicationContext)
        packagesViewModel = PackageViewModel(appDatabase.packagesDao())

        setContent {
            navController = rememberNavController()
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

            val allApps = GetAppsFunctions(
                this.packageManager,
                this.getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager,
                context = this
            ).createAppListWithTimeSpent(0)
            GlobalScope.launch(Dispatchers.Default) {
                allApps.forEach {
                    packagesViewModel.insertAppTimeSpentEntity(
                        AppTimeSpentEntity(
                            it.packageName,
                            it.timeSpentLong
                        )
                    )
                }
            }

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
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                        Manifest.permission.PACKAGE_USAGE_STATS
                    ),
                    100
                )
            }
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    println("Locatia este ${location?.latitude} si ${location?.longitude}")
                }
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

