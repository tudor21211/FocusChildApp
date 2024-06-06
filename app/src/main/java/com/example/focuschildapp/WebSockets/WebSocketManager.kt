package com.example.focuschildapp.com.example.focuschildapp.WebSockets

import android.Manifest
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import com.example.focuschildapp.com.example.focuschildapp.RoomDB.BlockedAppEntity
import com.example.focuschildapp.com.example.focuschildapp.RoomDB.BlockedWebsiteEntity
import com.example.focuschildapp.com.example.focuschildapp.RoomDB.PackageStatsEntity
import com.example.focuschildapp.com.example.focuschildapp.RoomDB.RestrictedKeywordEntity
import com.example.focuschildapp.com.example.focuschildapp.RoomDB.SpecialFeaturesEntity
import com.example.focuschildapp.com.example.focuschildapp.Utils.GetAppsFunctions
import com.example.focuschildapp.com.example.focuschildapp.Utils.QrGenerate
import com.example.websocket.RoomDB.AppDatabase
import com.example.websocket.RoomDB.PackageViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WebSocketManager(private val context: Context, private val userUid: String, private val email: String) : WebSocketListener() {

    private val _messages: MutableState<List<String>> = mutableStateOf(emptyList())
    private val messages: MutableState<List<String>> = _messages
    private var appDatabase: AppDatabase = AppDatabase.getDatabase(context.applicationContext)
    private var packagesViewModel: PackageViewModel = PackageViewModel(appDatabase.packagesDao())
    private val sharedPreferences : SharedPreferences = context.getSharedPreferences("PARENT_DEVICE_CONNECTED", Context.MODE_PRIVATE)
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lastLocation: Location? = null
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

        try {
             jsonObjectToProcess  = JSONObject(text)
        } catch (e: JSONException) {
            println("Invalid JSON string: $text")
        }
        println("JSON OBJECT IS $jsonObjectToProcess")
        when {

            text.startsWith("CONNECTED_PARENT") -> {
                val editor = sharedPreferences.edit()
                editor.putBoolean("PARENT_DEVICE_CONNECTED", true)
                editor.apply()
            }

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

            text.startsWith("$userUid SEND_STATISTICS_TIME") -> {
                val getAppsFunctions = GetAppsFunctions(
                    context.packageManager,
                    context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager,
                    context = context
                )
                val jsonArray = JSONArray()
                val allApps = getAppsFunctions.createAppListWithTimeSpent(0)
                val threeDaysApps = getAppsFunctions.createAppListWithTimeSpent(3)
                val sevenDaysApps = getAppsFunctions.createAppListWithTimeSpent(7)
                val thirtyDaysApps = getAppsFunctions.createAppListWithTimeSpent(30)
                val time = getAppsFunctions.getTimeSpent(0)
                var screenTime24h = getAppsFunctions.getTotalTimeSpent(time)
                getAppsFunctions.getInstalledApps()
                val launchTracker = getAppsFunctions.allAppsLaunchTracker(getAppsFunctions.getNonSystemApps())

                    GlobalScope.launch(Dispatchers.Default) {
                    for (app in allApps) {
                        val oneDayUsage = app.timeSpentLong
                        val threeDaysUsage = threeDaysApps.find { it.packageName == app.packageName }?.timeSpentLong ?: 0
                        val sevenDaysUsage = sevenDaysApps.find { it.packageName == app.packageName }?.timeSpentLong ?: 0
                        val thirtyDaysUsage = thirtyDaysApps.find { it.packageName == app.packageName }?.timeSpentLong ?: 0

                        val packageStatsEntity = PackageStatsEntity(
                            packageName = app.packageName,
                            oneDay = oneDayUsage,
                            threeDays = threeDaysUsage,
                            oneWeek = sevenDaysUsage,
                            oneMonth = thirtyDaysUsage
                        )
                        val jsonObject = JSONObject().apply{
                            put("userId" , userUid)
                            put("packageName", app.packageName)
                            put("oneDay", oneDayUsage)
                            put("threeDays", threeDaysUsage)
                            put("oneWeek", sevenDaysUsage)
                            put("oneMonth", thirtyDaysUsage)
                        }
                        jsonArray.put(jsonObject)
                        packagesViewModel.insertPackageStats(packageStatsEntity)
                    }

                    jsonArray.put(
                        JSONObject().apply {
                            put("userId" , userUid)
                            put("launchTracker" , launchTracker)
                            put("screenTime",screenTime24h)
                        }
                    )
                    val finalJsonObject = JSONObject().apply {
                        put("ADD_STATISTICS_DETAILS", jsonArray) // Add the array to a final JSON object
                    }
                    webSocket.send(finalJsonObject.toString())
                }
            }

            text.startsWith("${userUid}_SEND_UPDATE_LOCATION_COORDINATES") -> {
                println("RECEIVED THE SEND UPDATE")
                sendLocation(webSocket, userUid)
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

            jsonObjectToProcess.has("${userUid}_BLOCK_WEBSITE") ->{
                val jsonArray = jsonObjectToProcess.getJSONArray("${userUid}_BLOCK_WEBSITE")
                val data =   jsonArray.getJSONObject(0)
                val blockedWebsite : String = data.getString("website")
                val userId : String = data.getString("userId")
                GlobalScope.launch(Dispatchers.Default) {
                    packagesViewModel.insertBlockedWebsite(BlockedWebsiteEntity(blockedWebsite, userId))
                }
            }

            jsonObjectToProcess.has("${userUid}_BLOCK_KEYWORD") ->{
                val jsonArray = jsonObjectToProcess.getJSONArray("${userUid}_BLOCK_KEYWORD")
                val data =   jsonArray.getJSONObject(0)
                val restrictedKeyword : String = data.getString("keyword")
                val userId : String = data.getString("userId")
                GlobalScope.launch(Dispatchers.Default) {
                    packagesViewModel.insertRestrictedKeyword(RestrictedKeywordEntity(restrictedKeyword, userId))
                }
            }

            jsonObjectToProcess.has("${userUid}_REMOVE_WEBSITE") ->{
                val jsonArray = jsonObjectToProcess.getJSONArray("${userUid}_REMOVE_WEBSITE")
                val data =   jsonArray.getJSONObject(0)
                val blockedWebsite : String = data.getString("website")
                val userId : String = data.getString("userId")
                GlobalScope.launch(Dispatchers.Default) {
                    packagesViewModel.removeBlockedWebsite(blockedWebsite)
                }
            }

            jsonObjectToProcess.has("${userUid}_REMOVE_KEYWORD") ->{
                val jsonArray = jsonObjectToProcess.getJSONArray("${userUid}_REMOVE_KEYWORD")
                val data =   jsonArray.getJSONObject(0)
                val restrictedKeyword : String = data.getString("keyword")
                val userId : String = data.getString("userId")
                GlobalScope.launch(Dispatchers.Default) {
                    packagesViewModel.removeRestrictedKeyword(restrictedKeyword)
                }
            }

            jsonObjectToProcess.has("${userUid}_UPDATE_REELS") -> {
                val jsonArray = jsonObjectToProcess.getJSONArray("${userUid}_UPDATE_REELS")
                val data =   jsonArray.getJSONObject(0)
                val update = data.getBoolean("REELS")
                GlobalScope.launch (Dispatchers.Default){
                    packagesViewModel.insertSpecialFeaturesRestriction(SpecialFeaturesEntity(userUid, update))
                }
            }

            jsonObjectToProcess.has("${userUid}_UPDATE_SHORTS") -> {
                val jsonArray = jsonObjectToProcess.getJSONArray("${userUid}_UPDATE_SHORTS")
                val data =   jsonArray.getJSONObject(0)
                val update = data.getBoolean("SHORTS")
                GlobalScope.launch (Dispatchers.Default){
                    packagesViewModel.insertSpecialFeaturesRestriction(SpecialFeaturesEntity(userUid, shortsRestriction = update))
                }
            }

            //UPDATE APPS DATA AFTER RECEIVING THE REQUEST
            text.startsWith("$userUid UPDATE_APPS_DATA") -> {
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
                            put("packageName", apps.packageName)
                            put("timeSpent", apps.timeSpentLong)
                        }
                        jsonArray.put(jsonObject)
                    }
                    jsonArray.put(JSONObject().apply {
                        val sdf = SimpleDateFormat("MMM dd , yyyy HH:mm")
                        val resultDate = Date(System.currentTimeMillis())
                        put("updateTime", sdf.format(resultDate))
                    })
                    val finalJsonObject = JSONObject().apply {
                        put("UPDATE_APPS_DATA", jsonArray) // Add the array to a final JSON object
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
        println("WebSocket connection failed: ${t.message} $response")
    }

    private fun sendLocation(webSocket: WebSocket, userId : String) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val sdf = SimpleDateFormat("M/dd/yyyy HH:mm", Locale.US)
        val currentTime = sdf.format(Date())
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val locationRequest = LocationRequest.create().apply {
            interval = 10000 // Set the desired interval for active location updates, in milliseconds.
            fastestInterval = 5000 // Set the fastest rate for active location updates, in milliseconds.
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    if (lastLocation != null && location.latitude == lastLocation!!.latitude && location.longitude == lastLocation!!.longitude) {
                        // Stop location updates if the new location is the same as the last location
                        //fusedLocationClient.removeLocationUpdates(this)
                        break
                    } else {
                        lastLocation = location // Update the last location
                        val jsonArray = JSONArray().apply {
                            put(userId)
                            put(location.latitude)
                            put(location.longitude)
                            put(currentTime)
                        }
                        val jsonObject = JSONObject().apply {
                            put("UPDATE_LOCATION_COORDINATES", jsonArray)
                        }
                        webSocket.send(jsonObject.toString())
                    }
                }

            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }
}