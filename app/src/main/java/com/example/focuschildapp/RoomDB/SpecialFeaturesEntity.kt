package com.example.focuschildapp.com.example.focuschildapp.RoomDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "specialFeatures")
data class SpecialFeaturesEntity(
    @PrimaryKey(autoGenerate = false)
    val userId: String,
    @ColumnInfo(defaultValue = "0")
    val reelsRestriction : Boolean = false,
    @ColumnInfo(defaultValue = "0")
    val shortsRestriction : Boolean = false
)