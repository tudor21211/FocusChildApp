package com.example.focuschildapp.com.example.focuschildapp.Services

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.focuschildapp.MainActivity
import com.example.focuschildapp.com.example.focuschildapp.BroadcastReceivers.NetworkChangeReceiver
import com.example.focuschildapp.com.example.focuschildapp.WebSockets.WebSocketManager
import com.example.websocket.RoomDB.AppDatabase
import com.example.websocket.RoomDB.PackageViewModel
import okhttp3.OkHttpClient
import okhttp3.Request


class ServerService : Service() {

    private lateinit var networkChangeReceiver: NetworkChangeReceiver

    override fun onBind(intent: Intent?): IBinder? {
        // A client is binding to the service with bindService()
        return null
    }

    override fun onCreate(){
        super.onCreate()

        networkChangeReceiver = NetworkChangeReceiver(this)
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeReceiver, intentFilter)

        connectWebSocket()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "ServerService",
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
        startForeground(1, start())

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    fun connectWebSocket() {
        val client = OkHttpClient()
        val request = Request.Builder().url("ws://192.168.0.108:8200/ws").build()
        val listener = WebSocketManager(this)
        val webSocket = client.newWebSocket(request, listener)
        webSocket.send("Service Connected")
    }

    private fun start(): Notification {
        return NotificationCompat.Builder(this, "ServerService")
            .setContentTitle("FocusChild")
            .setContentText("FocusChild is running")
            .setSmallIcon(R.drawable.ic_dialog_info)
            .setOngoing(true)
            .build()
    }



    override fun onDestroy() {
        // The service is no longer used and is being destroyed
        unregisterReceiver(networkChangeReceiver)
    }



}