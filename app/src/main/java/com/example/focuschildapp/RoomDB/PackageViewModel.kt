package com.example.websocket.RoomDB

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PackageViewModel (private val packagesDao : PackagesDAO) : ViewModel() {

    fun insert(packageEntity: PackageEntity) = viewModelScope.launch {
        packagesDao.insert(packageEntity)
    }

    fun insertBlockedApp (blockedAppEntity: BlockedAppEntity) = viewModelScope.launch {
        packagesDao.insertBlockedApp(blockedAppEntity)
    }

    suspend fun isAppBlocked(packageName : String) : Int {
        return packagesDao.isAppBlocked(packageName)
    }

}

