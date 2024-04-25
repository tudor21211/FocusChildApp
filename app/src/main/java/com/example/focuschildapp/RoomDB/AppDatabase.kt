package com.example.websocket.RoomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [PackageEntity::class, BlockedAppEntity::class], version = 2)
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