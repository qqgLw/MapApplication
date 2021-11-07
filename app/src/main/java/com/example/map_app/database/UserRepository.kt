package com.example.map_app.database

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class UserRepository(application: Application) {

    private val userDao = AppDatabase.getDatabase(application).userDao()

    val users = userDao.getUsers()

    fun  authenticateUser(
        login : String,
        password : String
    ) : LiveData<User> =  userDao.authenticateUser(login, password)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun addUser(user: User) { userDao.addUser(user) }
}