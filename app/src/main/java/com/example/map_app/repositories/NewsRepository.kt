package com.example.map_app.repositories

import com.example.map_app.api.NewsAPIRepository
import com.example.map_app.database.dao.ArticleDao
import com.example.map_app.database.tables.Article
import com.example.map_app.services.AuthSharedPreferenceService

class NewsRepository(
    private val articleDAO : ArticleDao,
    private val apiRepository: NewsAPIRepository,
    private val preferencesService: AuthSharedPreferenceService
    ) {

    suspend fun insertOrUpdateArticle(article: Article) {
        article.ownerId = preferencesService.loadCurrentUser().id
        articleDAO.insertOrUpdate(article)
    }

    fun getSavedNews() = articleDAO.getAllArticles(preferencesService.loadCurrentUser().id)

    suspend fun deleteArticle(article: Article) = articleDAO.deleteArticle(article)

    suspend fun getTopHeadlines(countryCode: String, pageNumber: Int) =
        apiRepository.getTopHeadlines(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        apiRepository.searchNews(searchQuery, pageNumber)
}