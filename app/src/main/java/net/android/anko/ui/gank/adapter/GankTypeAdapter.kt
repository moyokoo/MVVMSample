package net.android.anko.ui.gank.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import net.android.anko.R
import net.android.anko.model.model.MainDrawModel
import net.android.anko.utils.extensions.loadVector


class GankTypeAdapter  constructor(var list: List<MainDrawModel>)
    : BaseQuickAdapter<MainDrawModel, BaseViewHolder>(R.layout.fragment_gank_type_item, list) {
    override fun convert(helper: BaseViewHolder?, item: MainDrawModel?) {
        helper?.setText(R.id.titleTv, item?.name)
        helper?.getView<ImageView>(R.id.profileIv)?.loadVector(item?.icon!!)
    }
}
