package net.android.anko.ui.gank

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import miaoyongjun.autil.utils.LogUtils
import net.android.anko.base.BaseViewModel
import net.android.anko.base.di.names.Remote
import net.android.anko.model.ApiGankRepository
import net.android.anko.model.model.GankModel
import net.android.anko.paging.GankRepository
import net.android.anko.paging.base.BasePagingViewModel
import javax.inject.Inject


class GankViewModel @Inject
internal constructor(@Remote val repository: GankRepository) : BasePagingViewModel<GankModel>() {
    override fun initPagingList() {
        pagingList = Transformations.map(typeNameLiveData) {
            repository.gank(it)
        }
    }

    val typeNameLiveData = MutableLiveData<String>()


    fun fetchTypeName(typeName: String? = ""): Boolean {
        if (typeNameLiveData.value == typeName) {
            return false
        }
        typeNameLiveData.value = typeName
        return true
    }
}
