package com.example.focuschildapp.ScreensPkg

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.focuschildapp.Navigation.Screens
import com.example.focuschildapp.Permissions.PermissionFunctions
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.example.focuschildapp.R

sealed class Permission(
    val title: String,
    val description: String,
    val route: String,

    ) {
    @Composable
    abstract fun isApproved() : Boolean
}


object UsageAccessPermission : Permission(
    "Usage access",
    "This permission is required to retrieve statistics about apps",
    Screens.UsageAccess.route
) {
    @Composable
    override fun isApproved(): Boolean {
        return PermissionFunctions(LocalContext.current, LocalContext.current.packageName)
            .isPackageUsageStatsPermissionEnabled()
    }
}

object AccessibilityPermission : Permission(
    "Accessibility",
    "This permission is required to block apps and websites you select.",
    Screens.Accessibility.route
) {
    @Composable
    override fun isApproved(): Boolean {
        return PermissionFunctions(LocalContext.current, LocalContext.current.packageName)
            .isAccessibilityServiceEnabled("MyAccessibilityService")
    }
}

object DisplayOverOtherAppsPermission : Permission(
    "Display over other apps",
    "Since Android 10, this permission is required to block apps and websites.",
    Screens.DisplayOverOtherApps.route
) {
    @Composable
    override fun isApproved(): Boolean {
        return PermissionFunctions(LocalContext.current, LocalContext.current.packageName)
            .isOverlayPermissionEnabled()
    }
}

@Composable
fun PermissionScreen(navController: NavController) {
    val systemUiController = rememberSystemUiController()
    val colorStops = arrayOf(
        0.2f to Color(0xFF0E4B4D),
        0.5f to Color(0xFF10585A),
        1f to Color(0xFF126366)
    )
    val colorStopsCardsDenied = arrayOf(
        0.2f to Color(0xFFE7900F),
        0.5f to Color(0xFFF17617),
        1f to Color(0xFFE6640C)
    )
    val colorStopsCardsApproved = arrayOf(
        0.2f to Color(0xFF89F013),
        0.5f to Color(0xFF89EB19),
        1f to Color(0xFF6BBE0D)
    )

    SideEffect {
        systemUiController.setSystemBarsColor(Color(0xFF0E4B4D))
    }

    Column(
        modifier = Modifier
            .background(Brush.linearGradient(colorStops = colorStops))
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.settings),
            contentDescription = "Settings",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.4f)
                .padding(30.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "To enhance your app experience, the app requires the following permissions:",
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.opensans_res)),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
            )
        }

        val permissions = listOf(
            UsageAccessPermission,
            AccessibilityPermission,
            DisplayOverOtherAppsPermission
        )

        permissions.forEach { permission ->
            Spacer(modifier = Modifier.height(10.dp))
            permissionCard(
                permission.title,
                permission.description,
                navController,
                permission.route,
                if (permission.isApproved()) colorStopsCardsApproved else colorStopsCardsDenied,
                testTag = permission.title
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun permissionCard(
    title: String,
    content: String,
    navController: NavController,
    navigationRoute: String,
    colorStops: Array<Pair<Float, Color>>,
    testTag : String
) {
    val openSans = FontFamily(
        Font(R.font.opensans_res),
    )

    Card(
        modifier = Modifier
            .height(95.dp)
            .fillMaxWidth(.96f)
            .testTag(testTag)
            .semantics {
                       contentDescription = "Permission Card"
            },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
        ),

        onClick = { navController.navigate(navigationRoute) }
    ) {
        Column(
            Modifier
                .background(Brush.linearGradient(colorStops = colorStops))
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(0.97f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    color = Color(0xFF000000),
                    fontFamily = openSans,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .padding(top = 6.dp)
                )

                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "go to settings",
                    tint = Color(0xFF000000),
                    modifier = Modifier
                        .size(25.dp)
                        .padding(top = 1.dp),
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = content,
                color = Color(0xFF444343),
                fontFamily = openSans,
                modifier = Modifier.padding(start = 9.dp)
            )
        }
    }
}