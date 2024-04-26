package com.example.focuschildapp.com.example.focuschildapp.Services

object UserPreferences {

    private var navigateUrl = "https://www.google.com/"

    fun setNavigateUrl(url : String) {
        this.navigateUrl = url
    }

    fun getNavigateUrl() : String {
        return navigateUrl
    }

}