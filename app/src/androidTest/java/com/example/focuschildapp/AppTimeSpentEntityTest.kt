package com.example.focuschildapp

import com.example.focuschildapp.com.example.focuschildapp.RoomDB.AppTimeSpentEntity
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test

class AppTimeSpentEntityTest {

    @Test
    fun testAppTimeSpentEntityJsonMapping() {
        val json = """
            {
                "packageName": "com.example.app",
                "usageTime": 3600
            }
        """.trimIndent()

        val gson = Gson()
        val entity = gson.fromJson(json, AppTimeSpentEntity::class.java)

        assertEquals("com.example.app", entity.packageName)
        assertEquals(3600, entity.usageTime)
    }
}