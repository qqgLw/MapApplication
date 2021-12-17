package com.example.map_app.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.map_app.database.tables.Article
@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(article: Article)

    @Query("SELECT * FROM articles WHERE ownerId = :ownerId ORDER BY publishedAt")
    fun getAllArticles(ownerId:Int?): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}