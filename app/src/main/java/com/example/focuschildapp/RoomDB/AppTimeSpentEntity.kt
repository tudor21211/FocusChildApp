package com.example.focuschildapp.com.example.focuschildapp.RoomDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appsTimeSpent")
data class AppTimeSpentEntity(
    @PrimaryKey(autoGenerate = false)
    val packageName: String,
    var usageTime : Long = 0
)