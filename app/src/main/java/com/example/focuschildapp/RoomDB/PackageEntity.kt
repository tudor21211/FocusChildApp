package com.example.websocket.RoomDB

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

