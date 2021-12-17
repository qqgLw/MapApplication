package com.example.map_app.api

import com.example.map_app.database.tables.Article

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)