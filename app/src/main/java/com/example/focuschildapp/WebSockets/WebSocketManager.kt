package com.example.focuschildapp.com.example.focuschildapp.WebSockets

import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.focuschildapp.com.example.focuschildapp.Utils.GetAppsFunctions
import com.example.focuschildapp.com.example.focuschildapp.Utils.QrGenerate
import com.example.websocket.RoomDB.AppDatabase
import com.example.websocket.RoomDB.PackageViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONArray
import org.json.JSONObject

class WebSocketManager(private val context: Context, private val userUid: String, private val email: String) : WebSocketListener() {

    private val _messages: MutableState<List<String>> = mutableStateOf(emptyList())
    private val messages: MutableState<List<String>> = _messages
    private var appDatabase: AppDatabase = AppDatabase.getDatabase(context.applicationContext)
    private var packagesViewModel: PackageViewModel = PackageViewModel(appDatabase.packagesDao())

    private fun getMessages(): MutableState<List<String>> {
        return messages
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        webSocket.send("$userUid:child")
        println("WebSocket connection established.")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        if (text.contains("SALUT")) {
            webSocket.send("IM STILL RUNNING IN BACKGROUND")
        }
        if(text == "send binding details") {
            webSocket.send("${Build.MANUFACTURER} ${Build.MODEL}:{device_manufacturer}")
        }

        /*
        _messages.value += listOf(text)
        val androidId: String =
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

        if (text.contains("appName")) {
            val jsonObject = JSONObject(text)
            val sendToId = jsonObject.getString("sendToId")
            println("THE SEND TO ID IS $sendToId AND THE ANDROID ID IS $androidId")
            if(sendToId != androidId) return //sending only back to the requester else returning
            val appName = jsonObject.getString("appName")
            val packageName = jsonObject.getString("packageName")
            val icon = jsonObject.getString("icon")

            GlobalScope.launch(Dispatchers.Default) {
                packagesViewModel.insert(
                    PackageEntity(0, packageName, appName, icon)
                )
            }
        }

        if (text.startsWith("$androidId sendImage")) {
            webSocket.send("Received all icons: \n")
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
                        //put("icon", drawableToByteString(apps.icon))
                    }
                    jsonObject.put("sendToId", text.split(" ")[2])
                    webSocket.send(jsonObject.toString())
                    //drawableToByteString(apps.icon)?.let { webSocket.send("data:image,$it") }
                }
            }

        }*/

        when {
            text.startsWith("$userUid SEND_FIRST_TIME_APPS_DETAILS") -> {
                val allApps = GetAppsFunctions(
                    context.packageManager,
                    context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager,
                    context = context
                ).createAppList()
                val jsonArray = JSONArray()
                GlobalScope.launch(Dispatchers.Default) {
                    for (apps in allApps) {
                        val jsonObject = JSONObject().apply {
                            put("userId", userUid)
                            put("email", email)
                            put("appName", apps.appName)
                            put("packageName", apps.appPackageName)
                            put("icon", QrGenerate.drawableToByteString(apps.icon))
                        }
                        jsonArray.put(jsonObject)
                    }
                    val finalJsonObject = JSONObject().apply {
                        put("addUserToDatabase", jsonArray) // Add the array to a final JSON object
                    }
                    webSocket.send(finalJsonObject.toString())
                }
            }

        }
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)

        println("Message received: ${bytes.hex()}")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        webSocket.send("Closing connection...")
        webSocket?.close(code, reason)
        println("WebSocket connection closed.")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        println("WebSocket connection failed: ${t.message}")
    }
}