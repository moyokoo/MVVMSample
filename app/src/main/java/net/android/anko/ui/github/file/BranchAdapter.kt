package net.android.anko.ui.github.file

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import net.android.anko.R
import net.android.anko.model.model.BranchModel
import net.android.anko.model.model.FilePathModel


class BranchAdapter internal constructor(var list: List<BranchModel>)
    : BaseQuickAdapter<BranchModel, BaseViewHolder>(R.layout.layout_item_branch, list) {
    override fun convert(helper: BaseViewHolder?, item: BranchModel?) {
        helper?.setText(R.id.nameTv, item?.name)
    }
}
