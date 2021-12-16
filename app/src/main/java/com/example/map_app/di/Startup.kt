package com.example.map_app.di

import android.content.Context
import com.example.map_app.api.NewsAPIRepository
import com.example.map_app.database.AppDatabase
import com.example.map_app.database.dao.UserDao
import com.example.map_app.repositories.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class Startup {
    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context) = AppDatabase.getDatabase(context)

    @Provides
    fun provideUserDao(database: AppDatabase) = database.userDao()

    @Provides
    fun provideArticleDao(database: AppDatabase) = database.articleDao()

    @Provides
    fun provideUserRepository(userDao: UserDao) =
        UserRepository(userDao)

    @Provides
    @Singleton
    fun provideNewsAPIRepository() = NewsAPIRepository()
}