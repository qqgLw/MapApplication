package com.example.map_app.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import java.io.Serializable

@Entity(
    tableName = "articles",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("ownerId"),
        onDelete = CASCADE
    )]
)
data class Article(
    var ownerId: Int? = null,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    @PrimaryKey
    val url: String,
    val urlToImage: String?
) : Serializable