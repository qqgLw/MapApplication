package com.example.map_app.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.map_app.database.tables.User
import com.example.map_app.database.dao.UserDao

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