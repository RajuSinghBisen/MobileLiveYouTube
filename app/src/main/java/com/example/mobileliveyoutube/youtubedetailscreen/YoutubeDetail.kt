package com.example.mobileliveyoutube.youtubedetailscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.mobileliveyoutube.R
import com.example.mobileliveyoutube.databinding.FragmentYoutubeDetailBinder
import com.example.mobileliveyoutube.modules.youtubemainscreen.YoutubeSnippet
import com.squareup.picasso.Picasso

class YoutubeDetail : DialogFragment(), YoutubeDetailController.View {
    private lateinit var binder: FragmentYoutubeDetailBinder
    private lateinit var controller: YoutubeDetailController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_youtube_detail, container, false)
        binder.lifecycleOwner = this
        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller = YoutubeDetailController(this, binder)
        binder.newsDetailToolbar.setNavigationOnClickListener{
            dismiss()
        }

    }

    override fun updateImage(content: YoutubeSnippet) {
        val thumbnailImgUrl = content.snippet?.thumbnails?.medium?.url
        if(!thumbnailImgUrl.isNullOrEmpty()) {
           binder.constraintArticleImg.visibility = View.VISIBLE
            Picasso.get()
                .load(thumbnailImgUrl)
                .into(binder.constraintArticleImg)
        }else{
            binder.constraintArticleImg.visibility = View.GONE
        }
        binder.newsDetailToolbarTitle.text = content.snippet?.title

    }

}
