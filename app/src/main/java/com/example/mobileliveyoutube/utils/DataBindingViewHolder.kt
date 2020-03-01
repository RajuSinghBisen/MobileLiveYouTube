package com.example.mobileliveyoutube.utils

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileliveyoutube.BR
/** This class defines a generic view holder to be used within Recycler Views.
 *
 */
class DataBindingViewHolder<T>(
    val binder : ViewDataBinding
) : RecyclerView.ViewHolder(binder.root) {
    fun bind(item : T) {
        binder.setVariable(BR.bindItem, item)
        binder.executePendingBindings()
    }
}
