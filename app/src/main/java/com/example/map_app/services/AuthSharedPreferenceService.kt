package com.example.map_app.services

import android.content.Context
import android.content.SharedPreferences
import com.example.map_app.models.UserModel

class AuthSharedPreferenceService(appContext: Context) {

    private val APP_PREFERENCES = "AuthorizedUser"

    private var sharedPreferences : SharedPreferences = appContext.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    fun saveCurrentUserData(user : UserModel){
        sharedPreferences.edit().apply{
            putInt("AuthorizedUser id", user.id)
            putString("AuthorizedUser login", user.login)
            putString("AuthorizedUser email", user.email)
        }.apply()
    }

    fun deleteCurrentUserData(){
        sharedPreferences.edit().apply{
            clear()
        }.apply()
    }

    fun loadCurrentUser(): UserModel {
        val userId = sharedPreferences.getInt("AuthorizedUser id", 0)
        val userLogin = sharedPreferences.getString("AuthorizedUser login", "").toString()
        val userEmail = sharedPreferences.getString("AuthorizedUser email", "").toString()

        return UserModel(userId, userLogin, userEmail)
    }

    val isAuthorized =  sharedPreferences.getString("AuthorizedUser email", "") != ""
}