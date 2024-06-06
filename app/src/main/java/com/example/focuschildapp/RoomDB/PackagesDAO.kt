package com.example.websocket.RoomDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.focuschildapp.com.example.focuschildapp.RoomDB.AppTimeSpentEntity
import com.example.focuschildapp.com.example.focuschildapp.RoomDB.BlockedAppEntity
import com.example.focuschildapp.com.example.focuschildapp.RoomDB.BlockedWebsiteEntity
import com.example.focuschildapp.com.example.focuschildapp.RoomDB.PackageStatsEntity
import com.example.focuschildapp.com.example.focuschildapp.RoomDB.RestrictedKeywordEntity
import com.example.focuschildapp.com.example.focuschildapp.RoomDB.SpecialFeaturesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PackagesDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: PackageEntity)
    @Update
    suspend fun update(note: PackageEntity)
    @Query("SELECT * FROM packages")
    fun getAllPackages(): Flow<List<PackageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlockedApp(note : BlockedAppEntity)

    @Query("SELECT timeBlocked FROM blockedApps WHERE packageName=:packageName")
    fun isAppBlocked(packageName : String) : Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBlockedWebsite (note : BlockedWebsiteEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRestrictedKeyword (note : RestrictedKeywordEntity)

    @Query("SELECT websiteURL FROM blockedWebsites")
    suspend fun getBlockedWebsites() : List<String>

    @Query("SELECT restrictedKeyword FROM restrictedKeywords")
    suspend fun getRestrictedKeywords() : List<String>

    @Query("SELECT COUNT(*) > 0 FROM blockedWebsites WHERE websiteURL = :websiteUrl")
    suspend fun isWebsiteBlocked(websiteUrl: String): Boolean

    @Query("SELECT COUNT(*) > 0 FROM restrictedKeywords WHERE restrictedKeyword = :restrictedKeyword")
    suspend fun isRestrictedKeyword(restrictedKeyword: String): Boolean


    @Query("DELETE FROM blockedWebsites WHERE websiteURL=:websiteUrl")
    suspend fun removeBlockedWebsite(websiteUrl: String)
    @Query("DELETE FROM restrictedKeywords WHERE restrictedKeyword=:restrictedKeyword")
    suspend fun removeRestrictedKeyword(restrictedKeyword: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPackageStats(packageStatsEntity: PackageStatsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppTimeSpent(appTimeSpentEntity: AppTimeSpentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateReelsRestriction(specialFeaturesEntity: SpecialFeaturesEntity)

    @Query("SELECT reelsRestriction FROM specialFeatures")
    suspend fun areReelsBlocked() : Boolean
    @Query("SELECT shortsRestriction FROM specialFeatures")
    suspend fun areShortsBlocked() : Boolean

}