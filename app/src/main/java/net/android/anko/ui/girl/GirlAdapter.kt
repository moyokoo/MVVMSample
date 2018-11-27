package net.android.anko.ui.girl

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import miaoyongjun.autil.utils.DensityUtil.dip2px
import net.android.anko.R
import net.android.anko.helper.StringHelper
import net.android.anko.model.model.GankModel
import net.android.anko.paging.base.BaseNetAdapter
import net.android.anko.paging.base.BasePagingViewHolder
import net.android.anko.utils.banner.HolderUtils
import net.android.anko.utils.extensions.loadImage
import javax.inject.Inject

class GirlAdapter @Inject constructor() : BaseNetAdapter<GankModel>(R.layout.fragment_girl_item, POST_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasePagingViewHolder {
        val holder = super.onCreateViewHolder(parent, viewType)
        HolderUtils.onCreateViewHolder(parent, holder.itemView)
        return holder
    }

    override fun convert(helper: BasePagingViewHolder, item: GankModel?) {
        HolderUtils.onBindViewHolder(helper.itemView, helper.adapterPosition, itemCount)
        item?.url?.let {
            helper.getView<ImageView>(R.id.profileIv).loadImage(mContext, item.url)
        }
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