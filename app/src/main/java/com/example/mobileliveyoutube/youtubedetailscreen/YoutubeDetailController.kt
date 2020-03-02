package com.example.mobileliveyoutube.youtubedetailscreen

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mobileliveyoutube.databinding.FragmentYoutubeDetailBinder
import com.example.mobileliveyoutube.modules.youtubemainscreen.YoutubeSnippet
import com.example.mobileliveyoutube.utils.viewModelFactory

class YoutubeDetailController(
    fragment: YoutubeDetail,
    binder: FragmentYoutubeDetailBinder
) {

    interface View: NewsArticleItemClick {
        override fun updateImage(content: YoutubeSnippet)
    }
    private val view: View = fragment


    private val viewModel = ViewModelProvider(
        fragment, viewModelFactory { YoutubeDetailViewModel(YoutubeDetailArgs.fromBundle(fragment.arguments!!).article) }).get(YoutubeDetailViewModel::class.java)

    init {
        binder.models = viewModel
        binder.controller = this
        viewModel.article.observe(
            fragment.viewLifecycleOwner,
            Observer {
                view.updateImage(it)
            }
        )
    }
}

interface NewsArticleItemClick {
    fun updateImage(content: YoutubeSnippet)
}
