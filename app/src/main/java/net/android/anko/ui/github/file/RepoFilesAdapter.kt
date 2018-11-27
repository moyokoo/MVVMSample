package net.android.anko.ui.github.file

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import net.android.anko.R
import net.android.anko.helper.StringHelper
import net.android.anko.model.model.FileModel

class RepoFilesAdapter internal constructor(var list: List<FileModel>)
    : BaseQuickAdapter<FileModel, BaseViewHolder>(R.layout.layout_item_file, list) {
    override fun convert(helper: BaseViewHolder?, item: FileModel?) {
        helper?.setText(R.id.fileName, item?.name)
        item?.let {
            if (item.isFile) {
                helper?.setBackgroundRes(R.id.fileType, R.drawable.ic_file)
                helper?.setText(R.id.fileSize, StringHelper.getSizeString(item.size.toLong()))
            } else {
                helper?.setBackgroundRes(R.id.fileType, R.drawable.ic_folder)
                helper?.setText(R.id.fileSize, "")
            }
        }

    }
}