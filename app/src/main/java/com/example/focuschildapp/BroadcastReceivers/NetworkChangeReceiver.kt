package com.example.focuschildapp.com.example.focuschildapp.BroadcastReceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.example.focuschildapp.com.example.focuschildapp.Services.ServerService

class NetworkChangeReceiver(private val serverService: ServerService) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val serverService = ServerService.instance
        if (ConnectivityManager.CONNECTIVITY_ACTION == intent.action) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            if (networkInfo != null && networkInfo.isConnected) {
                serverService?.connectWebSocket()
            }

        }
    }
}