package com.example.map_app.news.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.map_app.R
import com.example.map_app.news.NewsAdapter
import com.example.map_app.news.NewsViewModel
import com.example.map_app.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_top_headlines.*

@AndroidEntryPoint
class TopHeadLinesFragment : Fragment(R.layout.fragment_top_headlines) {

    private val newsViewModel: NewsViewModel by viewModels()
    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsAdapter = NewsAdapter()
        recViewTopHeadlines.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        newsViewModel.topHeadlines.observe(viewLifecycleOwner){
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
        feedPaginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        feedPaginationProgressBar.visibility = View.VISIBLE
    }
}