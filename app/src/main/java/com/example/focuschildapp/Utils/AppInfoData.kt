package com.example.focuschildapp.com.example.focuschildapp.Utils

import android.graphics.drawable.Drawable


data class AppInfoData(
    val icon: Drawable,
    val appName: CharSequence,
    val timeSpent: String,
    val timeSpentLong: Long,
    val packageName: String
)
