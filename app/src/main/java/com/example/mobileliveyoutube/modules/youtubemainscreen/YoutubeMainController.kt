package com.example.mobileliveyoutube.modules.youtubemainscreen

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mobileliveyoutube.databinding.FragmentYoutubeMainBinder
import com.example.mobileliveyoutube.utils.viewModelFactory

class YoutubeMainController(fragment: YoutubeMain, binder: FragmentYoutubeMainBinder) {

    interface  View :  ArticleItemClick {
        override fun onItemClicked(item: YoutubeModel)
        override fun onContentData(items: List<YoutubeModel>)
    }
    private val view: View = fragment
    private val viewModel = ViewModelProvider(fragment, viewModelFactory { YoutubeMainViewModel() }).get(YoutubeMainViewModel::class.java)


    init {
        viewModel.results.observe(fragment.viewLifecycleOwner, Observer { view.onContentData(it) })
        viewModel.requestCategories()
    }
}

interface ArticleItemClick {
    fun onItemClicked(item: YoutubeModel)
    fun onContentData(items: List<YoutubeModel>)
}

