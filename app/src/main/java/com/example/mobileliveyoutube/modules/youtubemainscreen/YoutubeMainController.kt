package com.example.mobileliveyoutube.modules.youtubemainscreen

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mobileliveyoutube.databinding.FragmentYoutubeMainBinder
import com.example.mobileliveyoutube.utils.viewModelFactory

class YoutubeMainController(fragment: YoutubeMain, binder: FragmentYoutubeMainBinder) {

    interface  View :  ArticleItemClick {
        override fun onItemClicked(item: YoutubeSnippet)
        override fun onContentData(items: List<YoutubeSnippet>)
        override fun onTotalContentData(items: YouTubePageInfo)
    }
    private val view: View = fragment
    private val viewModel = ViewModelProvider(fragment, viewModelFactory { YoutubeMainViewModel() }).get(YoutubeMainViewModel::class.java)


    init {
        viewModel.results.observe(fragment.viewLifecycleOwner, Observer { view.onContentData(it) })
        viewModel.totalResults.observe(fragment.viewLifecycleOwner, Observer { view.onTotalContentData(it) })

        viewModel.requestCategories()
    }
}

interface ArticleItemClick {
    fun onItemClicked(item: YoutubeSnippet)
    fun onContentData(items: List<YoutubeSnippet>)
    fun onTotalContentData(items: YouTubePageInfo)
}

