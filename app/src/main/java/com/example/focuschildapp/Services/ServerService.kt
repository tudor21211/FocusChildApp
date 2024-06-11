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
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.focuschildapp.Firebase.domain.AuthRepository
import com.example.focuschildapp.MainActivity
import com.example.focuschildapp.Navigation.Screens
import com.example.focuschildapp.com.example.focuschildapp.BroadcastReceivers.NetworkChangeReceiver
import com.example.focuschildapp.com.example.focuschildapp.ScreensPkg.MainPage.MainPageViewModel
import com.example.focuschildapp.com.example.focuschildapp.WebSockets.WebSocketManager
import com.example.websocket.RoomDB.AppDatabase
import com.example.websocket.RoomDB.PackageViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

@AndroidEntryPoint
class ServerService : Service() {

    private lateinit var networkChangeReceiver: NetworkChangeReceiver
    @Inject
    lateinit var authRepository: AuthRepository
        companion object {
            var instance: ServerService? = null
        }
    private var isConnected = false


    override fun onBind(intent: Intent?): IBinder? {
        // A client is binding to the service with bindService()
        return null
    }

    override fun onCreate(){
        super.onCreate()
        instance = this
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
        val userUid = authRepository.currentUser?.uid ?: return
        val email = authRepository.currentUser?.email ?: return
        val request = Request.Builder().url("ws://192.168.195.44:8200/ws/$userUid").build()
        //val request = Request.Builder().url("wss://fastapi-project-zgaflnvvcq-ey.a.run.app/ws/$userUid").build()
        val listener = WebSocketManager(this, userUid, email)
        println("USER ID IS $userUid")
        val webSocket = client.newWebSocket(request, listener)
        webSocket.send("CONNECTED_CHILD_WITH_ID_$userUid")
    }

    private fun start(): Notification {
        return NotificationCompat.Builder(this, "ServerService")
            .setContentTitle("FocusChild")
            .setContentText("FocusChild is running")
            .setSmallIcon(R.drawable.ic_dialog_info)
            .setOngoing(true)
            .build()
    }


    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        // The service is no longer used and is being destroyed
        Toast.makeText(this, "Service destroyed", Toast.LENGTH_SHORT).show()
        unregisterReceiver(networkChangeReceiver)
    }



}