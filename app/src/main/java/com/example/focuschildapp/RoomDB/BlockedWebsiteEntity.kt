package com.example.focuschildapp.com.example.focuschildapp.RoomDB

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "blockedWebsites")
data class BlockedWebsiteEntity(

    @PrimaryKey(autoGenerate = false)
    val websiteURL: String,
    val userId : String

)


@Entity(tableName = "restrictedKeywords")
data class RestrictedKeywordEntity(
    @PrimaryKey(autoGenerate = false)
    val restrictedKeyword : String,
    val userId : String

)
