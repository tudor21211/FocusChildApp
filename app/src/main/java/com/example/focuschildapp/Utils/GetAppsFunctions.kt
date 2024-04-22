package com.example.focuschildapp.com.example.focuschildapp.Utils

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

class GetAppsFunctions(
    private val packageManager: PackageManager,
    private val usageStatsManager: UsageStatsManager,
    private val context : Context
) {
    private var nonSystemApps: List<ApplicationInfo> = listOf()

    private fun getNonSystemApps(): List<ApplicationInfo> {
        return nonSystemApps
    }

    private fun getInstalledApps() {
        var listInstalledApps: List<ApplicationInfo> = packageManager.getInstalledApplications(
            PackageManager.GET_ACTIVITIES
        )

        nonSystemApps = listInstalledApps.filter { appInfo ->
            (appInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0) || (appInfo.packageName == "com.google.android.youtube") || (appInfo.packageName == "com.android.chrome")
        }
    }

    fun createAppList(): List<AppInfoDataNoTime> {

        val myApps = GetAppsFunctions(
            packageManager,
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager,
            context
        )
        myApps.getInstalledApps()
        val nonSystemApps = myApps.getNonSystemApps()

        for (name in nonSystemApps) {
            println("NAME OF THE APPS ${name.packageName}")
        }


        val appInfoList = nonSystemApps.map { appInfo ->

            val icon: Drawable = packageManager.getApplicationIcon(appInfo.packageName)
            val appName: CharSequence = packageManager.getApplicationLabel(appInfo)
            val appPackageName: String = appInfo.packageName

            AppInfoDataNoTime(icon, appName, appPackageName)
        }


        return appInfoList
    }
}