package com.example.mobileliveyoutube.modules.youtubemainscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileliveyoutube.YoutubeMainRepository
import com.example.mobileliveyoutube.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class YoutubeMainViewModel : ViewModel() {
    // processing is being used to have a spinner running till error or results appears.
    // result is used to catch the response after api processing
    // error to show error
    private val _processing = MutableLiveData<Boolean>()
    val processing: LiveData<Boolean>
        get() = _processing

    private val _results = MutableLiveData<MutableList<YoutubeSnippet>>()
    val results: LiveData<MutableList<YoutubeSnippet>>
        get() = _results

    private val _totalResults = MutableLiveData<YouTubePageInfo>()
    val totalResults: LiveData<YouTubePageInfo>
        get() = _totalResults

    private val _error = SingleLiveEvent<Throwable>()
    val error: LiveData<Throwable>
        get() = _error

    fun requestCategories() {
        viewModelScope.launch {
            _processing.postValue(true)
            _error.postValue(null)
            try {
                val totalResult = YoutubeMainRepository.getYoutubeCompleteData().pageInfo
                val result = YoutubeMainRepository.getYoutubeCompleteData().items
                val filter = ArrayList<YoutubeSnippet>()
                if (result != null) {
                    for(item in result) {
                        filter.add(item)
                    }
                }
                _totalResults.postValue(totalResult)
                _results.postValue(filter.toList() as MutableList<YoutubeSnippet>?)
            } catch (e: Throwable) {
                _results.postValue(null)
                _totalResults.postValue(null)
                _error.postValue(e)
            } finally {
                _processing.postValue(false)
            }
        }
    }
}
