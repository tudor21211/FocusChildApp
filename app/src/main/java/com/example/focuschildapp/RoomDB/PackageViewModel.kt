package com.example.websocket.RoomDB

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.focuschildapp.com.example.focuschildapp.RoomDB.AppTimeSpentEntity
import com.example.focuschildapp.com.example.focuschildapp.RoomDB.BlockedAppEntity
import com.example.focuschildapp.com.example.focuschildapp.RoomDB.BlockedWebsiteEntity
import com.example.focuschildapp.com.example.focuschildapp.RoomDB.PackageStatsEntity
import com.example.focuschildapp.com.example.focuschildapp.RoomDB.RestrictedKeywordEntity
import com.example.focuschildapp.com.example.focuschildapp.RoomDB.SpecialFeaturesEntity
import kotlinx.coroutines.launch

class PackageViewModel (private val packagesDao : PackagesDAO) : ViewModel() {

    fun insert(packageEntity: PackageEntity) = viewModelScope.launch {
        packagesDao.insert(packageEntity)
    }

    fun insertBlockedApp (blockedAppEntity: BlockedAppEntity) = viewModelScope.launch {
        packagesDao.insertBlockedApp(blockedAppEntity)
    }

    fun insertSpecialFeaturesRestriction(specialFeaturesEntity: SpecialFeaturesEntity) = viewModelScope.launch {
        packagesDao.updateReelsRestriction(specialFeaturesEntity)
    }

    suspend fun isAppBlocked(packageName : String) : Int {
        return packagesDao.isAppBlocked(packageName)
    }

    suspend fun areReelsBlocked() : Boolean {
        return packagesDao.areReelsBlocked()
    }

    suspend fun areShortsBlocked() : Boolean {
        return packagesDao.areShortsBlocked()
    }

    suspend fun insertBlockedWebsite(blockedWebsiteEntity: BlockedWebsiteEntity) = viewModelScope.launch {
        packagesDao.insertBlockedWebsite(blockedWebsiteEntity)
    }


    suspend fun insertRestrictedKeyword(restrictedKeywordsEntity: RestrictedKeywordEntity) = viewModelScope.launch {
        packagesDao.insertRestrictedKeyword(restrictedKeywordsEntity)
    }

    suspend fun getRestrictedWebsites() : List<String>{
        return packagesDao.getBlockedWebsites()
    }

    suspend fun getRestrictedKeywords() : List<String>{
        return packagesDao.getRestrictedKeywords()
    }

    suspend fun isWebsiteBlocked(websiteUrl: String): Boolean {
        return packagesDao.isWebsiteBlocked(websiteUrl)
    }

    suspend fun isRestrictedKeyword(restrictedKeyword: String): Boolean {
        return packagesDao.isWebsiteBlocked(restrictedKeyword)
    }

    suspend fun removeBlockedWebsite(websiteUrl: String) {
        packagesDao.removeBlockedWebsite(websiteUrl)
    }

    suspend fun removeRestrictedKeyword(keyword: String) {
        packagesDao.removeRestrictedKeyword(keyword)
    }

    suspend fun insertPackageStats(packageStatsEntity: PackageStatsEntity) {
        packagesDao.insertPackageStats(packageStatsEntity)
    }

    suspend fun insertAppTimeSpentEntity(appTimeSpentEntity: AppTimeSpentEntity) {
        packagesDao.insertAppTimeSpent(appTimeSpentEntity)
    }
}

