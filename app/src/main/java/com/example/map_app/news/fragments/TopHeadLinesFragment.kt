package com.example.map_app.news.fragments

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.map_app.R
import com.example.map_app.news.NewsAdapter
import com.example.map_app.news.NewsViewModel
import com.example.map_app.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.map_app.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.android.synthetic.main.fragment_top_headlines.*

@AndroidEntryPoint
class TopHeadLinesFragment : Fragment(R.layout.fragment_top_headlines) {

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
                R.id.action_topHeadLinesFragment_to_articleFragment,
                bundle
            )
        }

        newsViewModel.topHeadlines.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let{newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2 //+2 because of truncation and empty last response element
                        isLastPage = newsViewModel.topHeadlinesPage == totalPages
                        if(isLastPage) {

                            recViewTopHeadlines.setPadding(0,0,0,paddingDp)
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
        feedPaginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        feedPaginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    val scrollListener = object : RecyclerView.OnScrollListener(){ //custom scroll listener to perform query on paginating\
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
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
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE

            val shouldPaginate = isNotLoadingAndNotLastPage
                    && isAtLastItem
                    && isNotAtBeginning
                    && isTotalMoreThanVisible
                    && isScrolling

            if(shouldPaginate){
                newsViewModel.getTopHeadlines("ru")
                isScrolling=false
            }
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        recViewTopHeadlines.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@TopHeadLinesFragment.scrollListener)
        }
    }
}