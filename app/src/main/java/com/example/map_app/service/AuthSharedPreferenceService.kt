package com.example.map_app.service

import android.content.Context
import android.content.SharedPreferences

class AuthSharedPreferenceService(currentContext: Context) {

    private val APP_PREFERENCES = "AuthorizedUser"
    private val APP_PREFERENCES_STATUS = "Session"

    private var sharedPreferences : SharedPreferences = currentContext.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    fun saveCurrentUserData(){
        sharedPreferences.edit().apply{
            putBoolean(APP_PREFERENCES_STATUS, true)
        }.apply()
    }

    fun deleteCurrentUserData(){
        sharedPreferences.edit().apply{
            clear()
        }.apply()
    }

    val isAuthorized =  sharedPreferences.getBoolean(APP_PREFERENCES_STATUS, false)
}