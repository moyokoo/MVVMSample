package net.android.anko.ui.gank.adapter

import android.support.v7.util.DiffUtil
import net.android.anko.R
import net.android.anko.helper.StringHelper
import net.android.anko.model.model.GankModel
import net.android.anko.paging.base.BaseNetAdapter
import net.android.anko.paging.base.BasePagingViewHolder
import javax.inject.Inject

class GankAdapter @Inject constructor() : BaseNetAdapter<GankModel>(R.layout.fragment_gank_list_item, POST_COMPARATOR) {

    override fun convert(helper: BasePagingViewHolder, item: GankModel?) {
        helper.setText(R.id.titleTv, item?.desc)
                .setText(R.id.timeTv, StringHelper.getNewsTimeStr(mContext, item?.publishedAt))
                .setText(R.id.contentTv, item?.url)
    }

    companion object {
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<GankModel>() {
            override fun areContentsTheSame(oldItem: GankModel, newItem: GankModel): Boolean =
                    oldItem == newItem

            override fun areItemsTheSame(oldItem: GankModel, newItem: GankModel): Boolean =
                    oldItem._id == newItem._id
        }
    }

}