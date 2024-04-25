package com.example.websocket.RoomDB

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "packages", indices = [Index(value = ["packageName"], unique = true)])
data class PackageEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val packageName: String,
    val appName: String,
    val icon : String
)

@Entity(tableName = "blockedApps")
data class BlockedAppEntity(
    @PrimaryKey(autoGenerate = false)
    val packageName: String,
    val timeBlocked : Int
)
