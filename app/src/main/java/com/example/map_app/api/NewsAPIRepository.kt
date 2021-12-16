package com.example.map_app.api

import com.example.map_app.util.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsAPIRepository {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val requestMaker = retrofit.create(NewsAPI::class.java)

    suspend fun getTopHeadlines(countryCode: String, pageNumber: Int) =
        requestMaker.getTopHeadlines(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        requestMaker.searchForNews(searchQuery, pageNumber)
}