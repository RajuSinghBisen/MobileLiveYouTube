package com.example.mobileliveyoutube.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class DataBindingAdapter<T>(
    callback : DiffUtil.ItemCallback<T>
) : ListAdapter<T, DataBindingViewHolder<T>>(callback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<T> {
        return DataBindingViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), getLayout(viewType), parent, false
            )
        )
    }
    @LayoutRes abstract fun getLayout(viewType: Int): Int

    override fun onBindViewHolder(holder: DataBindingViewHolder<T>, position: Int) {
        holder.bind(getItem(position))
    }

}