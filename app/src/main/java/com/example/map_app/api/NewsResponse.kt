package com.example.map_app.api

import com.example.map_app.database.tables.Article

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)