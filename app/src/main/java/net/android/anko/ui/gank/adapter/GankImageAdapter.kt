package net.android.anko.ui.gank.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import net.android.anko.R
import net.android.anko.utils.extensions.loadImage


class GankImageAdapter internal constructor(var list: List<String>)
    : BaseQuickAdapter<String, BaseViewHolder>(R.layout.image_item, list) {
    override fun convert(helper: BaseViewHolder?, item: String?) {
        item?.let { helper?.getView<ImageView>(R.id.profileIv)?.loadImage(mContext, it) }
    }
}
