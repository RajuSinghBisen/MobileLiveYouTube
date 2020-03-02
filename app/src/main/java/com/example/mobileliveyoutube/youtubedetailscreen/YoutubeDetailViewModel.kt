package com.example.mobileliveyoutube.youtubedetailscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobileliveyoutube.modules.youtubemainscreen.YoutubeSnippet

class YoutubeDetailViewModel(content: YoutubeSnippet): ViewModel() {
    private val _article = MutableLiveData<YoutubeSnippet>(content)
    val article: LiveData<YoutubeSnippet>
        get() = _article

    private val _next_article = MutableLiveData<List<YoutubeSnippet>>()
    val nextArticleContent: LiveData<List<YoutubeSnippet>>
        get() = _next_article

    fun updateArticleDetails(item: YoutubeSnippet) {
        _article.postValue(item)
    }

}