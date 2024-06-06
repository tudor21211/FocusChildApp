package com.example.focuschildapp.com.example.focuschildapp.RoomDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "specialFeatures")
data class SpecialFeaturesEntity(
    @PrimaryKey(autoGenerate = false)
    val userId: String,
    val reelsRestriction : Boolean = false,
    val shortsRestriction : Boolean = false
)