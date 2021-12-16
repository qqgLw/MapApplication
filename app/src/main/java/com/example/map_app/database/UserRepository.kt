package com.example.map_app.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class UserRepository(private val userDAO : UserDao) {

    val users = userDAO.getUsers()

    fun  authenticateUser(
        login : String,
        password : String
    ) : LiveData<User> =  userDAO.authenticateUser(login, password)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun addUser(user: User) { userDAO.addUser(user) }
}