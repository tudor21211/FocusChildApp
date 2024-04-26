package com.example.focuschildapp.com.example.focuschildapp.RoomDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blockedApps")
data class BlockedAppEntity(
    @PrimaryKey(autoGenerate = false)
    val packageName: String,
    val timeBlocked : Int
)
