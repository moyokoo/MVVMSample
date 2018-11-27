package net.android.anko.utils.extensions

import android.view.View
import net.android.anko.paging.base.BasePagingAdapter

fun BasePagingAdapter<*, *>.itemClick(body: (position: Int) -> Unit) {
    setOnItemClickListener(object : BasePagingAdapter.OnItemClickListener {
        override fun onItemClick(adapter: BasePagingAdapter<*, *>, view: View, position: Int) {
            body.invoke(position)
        }
    })
}

fun BasePagingAdapter<*, *>.itemClickAllParams(body: (adapter: BasePagingAdapter<*, *>, view: View, position: Int) -> Unit) {
    setOnItemClickListener(object : BasePagingAdapter.OnItemClickListener {
        override fun onItemClick(adapter: BasePagingAdapter<*, *>, view: View, position: Int) {
            body.invoke(adapter, view, position)
        }
    })
}