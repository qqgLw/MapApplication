package com.example.map_app.news.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.map_app.R
import com.example.map_app.news.NewsAdapter
import com.example.map_app.news.NewsViewModel
import com.example.map_app.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.map_app.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {
    private val newsViewModel: NewsViewModel by viewModels()
    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        var job:Job? = null //job coroutine for delaying search request

        editTextSearch.addTextChangedListener {
            job?.cancel() //restart delay on input
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                it?.let{
                    val input = it.toString()
                    if(input.isNotEmpty())
                        newsViewModel.searchNews(input)
                }
            }
        }

        newsViewModel.searchNews.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let{newsResponse ->
                        newsAdapter.differ.submitList((newsResponse.articles))
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    it.message?.let{message ->
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> showProgressBar()
            }
        }
    }

    private fun hideProgressBar() {
        searchPaginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        searchPaginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        recViewSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}