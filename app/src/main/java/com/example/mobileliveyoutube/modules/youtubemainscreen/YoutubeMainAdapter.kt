package com.example.mobileliveyoutube.modules.youtubemainscreen

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.example.mobileliveyoutube.R
import com.example.mobileliveyoutube.utils.DataBindingAdapter
import com.example.mobileliveyoutube.utils.DataBindingViewHolder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_youtube_main.view.*


class YoutubeMainAdapter(val listener: YoutubeMainController.View) : DataBindingAdapter<YoutubeModel>(
    Callback()
) {
    class Callback : DiffUtil.ItemCallback<YoutubeModel>() {
        override fun areItemsTheSame(oldItem: YoutubeModel, newItem: YoutubeModel): Boolean
                = oldItem == newItem

        override fun areContentsTheSame(oldItem: YoutubeModel, newItem: YoutubeModel): Boolean
                =   oldItem.data?.title == newItem.data?.title
    }

    override fun getLayout(position: Int): Int  = R.layout.adapter_youtube_main

    override fun onBindViewHolder(holder: DataBindingViewHolder<YoutubeModel>, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)
        //Image Should be never empty but just for crash safe check.
        if(!item.data?.thumbnail.isNullOrEmpty()) {
            holder.itemView.adapter_content_icon.visibility = View.VISIBLE
            Picasso.get().load(item.data?.thumbnail).into(holder.itemView.adapter_content_icon)
        }else{
            holder.itemView.adapter_content_icon.visibility = View.GONE
        }

        holder.itemView.constraint_dashboard.setOnClickListener {
            listener.onItemClicked(item)
        }
        holder.itemView.adapter_content_title.text = "${item.data?.title}"
    }

}