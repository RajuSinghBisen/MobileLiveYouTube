package com.example.mobileliveyoutube.modules.youtubemainscreen

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.example.mobileliveyoutube.R
import com.example.mobileliveyoutube.utils.DataBindingAdapter
import com.example.mobileliveyoutube.utils.DataBindingViewHolder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_youtube_main.view.*


class YoutubeAdapter(val listener: YoutubeMainController.View) :DataBindingAdapter<YoutubeSnippet>(
    Callback()
) {
    class Callback : DiffUtil.ItemCallback<YoutubeSnippet>() {
        override fun areItemsTheSame(oldItem: YoutubeSnippet, newItem: YoutubeSnippet): Boolean
                = oldItem == newItem

        override fun areContentsTheSame(oldItem: YoutubeSnippet, newItem: YoutubeSnippet): Boolean
                =   oldItem == newItem
    }

    override fun getLayout(position: Int): Int  = R.layout.adapter_youtube_main

    override fun onBindViewHolder(holder: DataBindingViewHolder<YoutubeSnippet>, position: Int) {
        super.onBindViewHolder(holder,position)
        val item = getItem(position)
        holder.itemView.adapter_content_title.text = "${item.snippet?.title}"
        //Image Should be never empty but just for crash safe check.
        val imageUrl = item.snippet?.thumbnails?.medium?.url
        if(!imageUrl.isNullOrEmpty()) {
            holder.itemView.adapter_content_icon.visibility = View.VISIBLE
            Picasso.get().load(imageUrl).into(holder.itemView.adapter_content_icon)
        }else{
            holder.itemView.adapter_content_icon.visibility = View.GONE
        }
        holder.itemView.constraint_dashboard.setOnClickListener {
            listener.onItemClicked(item)
        }
    }

}