package net.android.anko.paging.base

import android.view.View
import net.android.anko.paging.NetworkState

abstract class BaseNetPagingViewHolder constructor(view: View) : BasePagingViewHolder(view) {

    abstract fun bind(networkState: NetworkState?)

}