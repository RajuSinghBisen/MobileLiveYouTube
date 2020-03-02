package com.example.mobileliveyoutube.modules.youtubemainscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileliveyoutube.R
import com.example.mobileliveyoutube.databinding.FragmentYoutubeMainBinder
import com.example.mobileliveyoutube.repositories.LocalPreferences
import kotlinx.android.synthetic.main.fragment_youtube_main.*

class YoutubeMain : Fragment(), YoutubeMainController.View {
    private lateinit var binder: FragmentYoutubeMainBinder
    private lateinit var controller: YoutubeMainController
    private lateinit var contentAdapter: YoutubeAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_youtube_main, container, false)
        binder.lifecycleOwner = this
        return binder.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller = YoutubeMainController( this, binder)
        contentAdapter = YoutubeAdapter(this)
        binder.youtubeRecyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = contentAdapter
        }
        userName.text = LocalPreferences.getInstance().getDisplayNameStored()
    }
    override fun onItemClicked(item: YoutubeSnippet) {
        if (findNavController().currentDestination?.id == R.id.youtubeMain) {
            findNavController().navigate(
                YoutubeMainDirections.actionYoutubeMainToYoutubeDetail(item))
        }
    }

    override fun onContentData(items: List<YoutubeSnippet>) {
        contentAdapter.submitList(items)
        contentAdapter.notifyDataSetChanged()
        if (items.isNullOrEmpty()) {
            binder.youtubeRecyclerview.visibility = View.GONE
        } else {
            binder.youtubeRecyclerview.visibility = View.GONE
            binder.youtubeRecyclerview.visibility = View.VISIBLE
        }
    }

    override fun onTotalContentData(items:YouTubePageInfo) {
        video_count.text =String.format(resources.getString(R.string.total_vidoes), items.totalResults.toString())
    }
}