package com.example.websocket.RoomDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PackagesDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: PackageEntity)
    @Update
    suspend fun update(note: PackageEntity)
    @Query("SELECT * FROM packages")
    fun getAllPackages(): Flow<List<PackageEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBlockedApp(note : BlockedAppEntity)

    @Query("SELECT timeBlocked FROM blockedApps WHERE packageName=:packageName")
    fun isAppBlocked(packageName : String) : Int
}