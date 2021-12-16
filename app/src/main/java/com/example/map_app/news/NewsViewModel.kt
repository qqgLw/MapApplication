package com.example.map_app.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.map_app.api.NewsResponse
import com.example.map_app.database.tables.Article
import com.example.map_app.repositories.NewsRepository
import com.example.map_app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val newsRepository: NewsRepository) :ViewModel(){

    val topHeadlines : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val topHeadlinesPage = 1

    init {
        getTopHeadlines("ru")
    }

    fun getTopHeadlines(countryCode: String) = viewModelScope.launch {
        topHeadlines.postValue((Resource.Loading()))
        val response = newsRepository.getTopHeadlines(countryCode, topHeadlinesPage)
        topHeadlines.postValue(handleTopHeadlinesResponse(response))
    }

    private fun handleTopHeadlinesResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful){
            response.body()?.let{result -> return Resource.Success(result)}
        }
        return Resource.Error(response.message())
    }
}