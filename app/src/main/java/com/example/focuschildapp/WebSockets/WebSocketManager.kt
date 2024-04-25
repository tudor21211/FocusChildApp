package com.example.focuschildapp.com.example.focuschildapp.WebSockets

import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.focuschildapp.com.example.focuschildapp.Utils.GetAppsFunctions
import com.example.focuschildapp.com.example.focuschildapp.Utils.QrGenerate
import com.example.websocket.RoomDB.AppDatabase
import com.example.websocket.RoomDB.BlockedAppEntity
import com.example.websocket.RoomDB.PackageViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONArray
import org.json.JSONException
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        var jsonObjectToProcess : JSONObject = JSONObject()
        if(text == "send binding details") {
            webSocket.send("${Build.MANUFACTURER} ${Build.MODEL}:{device_manufacturer}")
        }
        try {
             jsonObjectToProcess  = JSONObject(text)
        } catch (e: JSONException) {
            println("Invalid JSON string: $text")
        }
        println("JSON OBJECT IS $jsonObjectToProcess")
        when {

            text.startsWith("$userUid SEND_FIRST_TIME_APPS_DETAILS") -> {
                val allApps = GetAppsFunctions(
                    context.packageManager,
                    context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager,
                    context = context
                ).createAppListWithTimeSpent(0)
                val jsonArray = JSONArray()
                GlobalScope.launch(Dispatchers.Default) {
                    for (apps in allApps) {
                        val jsonObject = JSONObject().apply {
                            put("userId", userUid)
                            put("email", email)
                            put("deviceType", Build.MANUFACTURER+" "+Build.MODEL)
                            put("appName", apps.appName)
                            put("packageName", apps.packageName)
                            put("timeSpent", apps.timeSpentLong)
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

            jsonObjectToProcess.has("${userUid}_BLOCK_PACKAGE") ->{
               println("RECEIVED THE BLOCK")
               val jsonArray = jsonObjectToProcess.getJSONArray("${userUid}_BLOCK_PACKAGE")
               val data =   jsonArray.getJSONObject(0)
               val blockedPackage : String = data.getString("packageName")
               val timeBlocked : Int = data.getInt("timeBlocked")
               GlobalScope.launch(Dispatchers.Default) {
                   packagesViewModel.insertBlockedApp(BlockedAppEntity(blockedPackage, timeBlocked))
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