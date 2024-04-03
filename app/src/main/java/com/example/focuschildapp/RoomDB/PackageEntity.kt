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

@Entity(tableName = "users", indices = [Index(value = ["unique_id"], unique = true)])
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val username: String,
    val passwordHash: String,
    val salt: String? = null,
)
