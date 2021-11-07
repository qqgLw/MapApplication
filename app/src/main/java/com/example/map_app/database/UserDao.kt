package com.example.map_app.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Query("SELECT * FROM  user_table")
    fun getUsers() : LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM user_table WHERE login = :login AND password = :password ")
    fun authenticateUser(login : String, password : String) : LiveData<User>

}