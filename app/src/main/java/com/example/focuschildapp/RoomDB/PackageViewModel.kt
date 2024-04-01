package com.example.websocket.RoomDB

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PackageViewModel (private val packagesDao : PackagesDAO) : ViewModel() {

    val allPackages: LiveData<List<PackageEntity>> = packagesDao.getAllPackages().asLiveData()

    fun insert(packageEntity: PackageEntity) = viewModelScope.launch {
        packagesDao.insert(packageEntity)
    }
}

