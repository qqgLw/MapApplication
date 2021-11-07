package com.example.map_app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    private class RoomDatabaseCallback : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(SupervisorJob()).launch {
                    populateDatabase(database.userDao())
                }
            }
        }

        suspend fun populateDatabase(userDao: UserDao) {
            val user = User(0,"test@mail.ru","testUser33","Qwerty123")
            userDao.addUser(user)
        }
    }

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "user_database"
                )
                .addCallback(RoomDatabaseCallback())
                .build()
                INSTANCE = instance

                instance
            }
        }
    }
}