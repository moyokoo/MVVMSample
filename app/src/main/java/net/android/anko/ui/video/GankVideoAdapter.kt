package net.android.anko.ui.video

import android.support.v7.util.DiffUtil
import android.widget.ImageView
import net.android.anko.R
import net.android.anko.helper.StringHelper
import net.android.anko.model.model.GankModel
import net.android.anko.paging.base.BaseNetAdapter
import net.android.anko.paging.base.BasePagingViewHolder
import net.android.anko.utils.extensions.loadImage
import javax.inject.Inject

class GankVideoAdapter @Inject constructor() : BaseNetAdapter<GankModel>(R.layout.fragment_gank_video_list_item, POST_COMPARATOR) {

    override fun convert(helper: BasePagingViewHolder, item: GankModel?) {
        helper.setText(R.id.titleTv, item?.desc)
        item?.imageUrl?.let {
            helper.getView<ImageView>(R.id.profileIv).loadImage(mContext, item.imageUrl)
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