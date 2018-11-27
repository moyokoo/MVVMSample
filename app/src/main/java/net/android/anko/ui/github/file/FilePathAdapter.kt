package net.android.anko.ui.github.file

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import net.android.anko.R
import net.android.anko.model.model.FilePathModel


class FilePathAdapter internal constructor(var list: List<FilePathModel>)
    : BaseQuickAdapter<FilePathModel, BaseViewHolder>(R.layout.layout_item_file_path, list) {
    override fun convert(helper: BaseViewHolder?, item: FilePathModel?) {
        helper?.setText(R.id.path, if (item?.name == "") "." else item?.name)
    }
}
