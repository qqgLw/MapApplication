package com.example.map_app.database

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User (
    @PrimaryKey(autoGenerate = true)
    val id : Int,

    @NonNull
    val email : String,

    val login : String = email,

    @NonNull
    val password : String
)


