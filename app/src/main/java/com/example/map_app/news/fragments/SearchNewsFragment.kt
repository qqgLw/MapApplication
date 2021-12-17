package com.example.map_app.news.fragments

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.map_app.R
import com.example.map_app.hideKeyboard
import com.example.map_app.news.NewsAdapter
import com.example.map_app.news.NewsViewModel
import com.example.map_app.util.Constants
import com.example.map_app.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.map_app.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.android.synthetic.main.fragment_top_headlines.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {
    private val newsViewModel: NewsViewModel by viewModels()
    lateinit var newsAdapter: NewsAdapter

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        val scale = resources.displayMetrics.density
        val paddingDp = (60*scale+0.5f).toInt()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment,
                bundle
            )
        }

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
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE + 2 //+2 because of truncation and empty last response element
                        isLastPage = newsViewModel.searchNewsPage == totalPages
                        if(isLastPage) {
                            recViewSearchNews.setPadding(0,0,0,paddingDp)
                        }
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
        isLoading = false
    }

    private fun showProgressBar() {
        searchPaginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private val scrollListener = object : RecyclerView.OnScrollListener(){ //custom scroll listener to perform query on paginating
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                view?.hideKeyboard()
                isScrolling = true
            }
        }
        // :(((
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager

            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE

            val shouldPaginate = isNotLoadingAndNotLastPage
                    && isAtLastItem
                    && isNotAtBeginning
                    && isTotalMoreThanVisible
                    && isScrolling

            if(shouldPaginate){
                newsViewModel.searchNews(editTextSearch.text.toString())
                isScrolling=false
            }
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        recViewSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchNewsFragment.scrollListener)

        }
    }
}