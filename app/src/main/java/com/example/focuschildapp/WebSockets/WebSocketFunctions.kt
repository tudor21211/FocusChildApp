package com.example.focuschildapp.com.example.focuschildapp.WebSockets

import android.app.usage.UsageStatsManager
import android.content.Context
import com.example.focuschildapp.com.example.focuschildapp.Utils.GetAppsFunctions
import com.example.focuschildapp.com.example.focuschildapp.Utils.QrGenerate.Companion.drawableToByteString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.WebSocket
import org.json.JSONObject

class WebSocketFunctions(context: Context, webSocket: WebSocket) {

    private val context = context
    private val webSocket = webSocket

    private fun onMessageReceived(message: String, userUID: String) {
        when {
            message.startsWith("$userUID send default information") -> {
                val allApps = GetAppsFunctions(
                    context.packageManager,
                    context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager,
                    context = context
                ).createAppList()
                GlobalScope.launch(Dispatchers.Default) {
                    for (apps in allApps) {
                        val jsonObject = JSONObject().apply {
                            put("appName", apps.appName)
                            put("packageName", apps.appPackageName)
                            put("icon", drawableToByteString(apps.icon))
                        }
                        webSocket.send(jsonObject.toString())
                    }
                }
            }

        }
    }
}