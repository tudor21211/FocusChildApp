package com.example.websocket.RoomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.focuschildapp.com.example.focuschildapp.RoomDB.BlockedAppEntity
import com.example.focuschildapp.com.example.focuschildapp.RoomDB.BlockedWebsiteEntity
import com.example.focuschildapp.com.example.focuschildapp.RoomDB.RestrictedKeywordEntity


@Database(entities = [PackageEntity::class, BlockedAppEntity::class, BlockedWebsiteEntity::class, RestrictedKeywordEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase(){
        abstract fun packagesDao(): PackagesDAO

        companion object {
                @Volatile
                private var Instance: AppDatabase? = null
                fun getDatabase(context: Context): AppDatabase {
                        return Instance ?: synchronized(this) {
                                Room.databaseBuilder(context, AppDatabase::class.java, "packages_database")
                                        .build()
                                        .also { Instance = it }
                        }
                }
        }
}