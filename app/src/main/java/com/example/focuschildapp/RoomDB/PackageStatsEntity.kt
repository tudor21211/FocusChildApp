package com.example.focuschildapp.com.example.focuschildapp.RoomDB

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "packages_stats")
data class PackageStatsEntity(
    @PrimaryKey(autoGenerate = false)
    val packageName : String,
    val oneDay : Long,
    val threeDays : Long,
    val oneWeek : Long,
    val oneMonth : Long
)
