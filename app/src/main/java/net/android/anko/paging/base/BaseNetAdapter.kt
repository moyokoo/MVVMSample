package net.android.anko.paging.base

import android.support.annotation.LayoutRes
import android.support.v7.util.DiffUtil
import android.view.ViewGroup
import net.android.anko.paging.NetworkStateItemPagingViewHolder

abstract class BaseNetAdapter<T> constructor(@LayoutRes mLayoutResId: Int, diff: DiffUtil.ItemCallback<T>)
    : BasePagingAdapter<T, BasePagingViewHolder>(mLayoutResId, diff) {

    override fun provideNetStatusViewHolder(parent: ViewGroup,retryCallback: (() -> Unit)?): BasePagingViewHolder {
        return NetworkStateItemPagingViewHolder.create(parent, retryCallback)
    }
}