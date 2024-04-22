package com.example.focuschildapp.com.example.focuschildapp.BroadcastReceivers

import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.focuschildapp.com.example.focuschildapp.Services.MyAccessibilityService
import com.example.focuschildapp.com.example.focuschildapp.Services.ServerService

class RebootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            val serviceIntent = Intent(
                context,
                MyAccessibilityService::class.java
            )// LA REBOOT SE RETRIMITE UN INTENT DE PORNIRE AL SERVICIULUI DE BLOCARE APLICATII
            val connectivityServiceIntent = Intent(
                context,
                ServerService::class.java
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d(ContentValues.TAG, "I REBOOTED DO SOMETHING")
                context?.startForegroundService(serviceIntent)
                context?.startForegroundService(connectivityServiceIntent)
            } else {
                context?.startService(serviceIntent)
                context?.startService(connectivityServiceIntent)
            }
        }
    }

}