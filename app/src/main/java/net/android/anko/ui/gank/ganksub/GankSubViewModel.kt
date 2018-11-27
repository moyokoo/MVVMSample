package net.android.anko.ui.gank.ganksub

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import net.android.anko.base.BaseViewModel
import net.android.anko.base.di.names.Remote
import net.android.anko.model.ApiGankRepository
import net.android.anko.model.model.GankModel
import net.android.anko.paging.GankRepository
import net.android.anko.paging.base.BasePagingViewModel
import net.android.anko.ui.gank.GankFactory
import javax.inject.Inject


class GankSubViewModel @Inject constructor(@Remote val repository: GankRepository) : BasePagingViewModel<GankModel>() {
    private val typeNameLiveData = MutableLiveData<String>()
    override fun initPagingList() {
        pagingList = Transformations.map(typeNameLiveData) {
            repository.gank(it)
        }
    }

    fun fetchTypeName(typeName: String? = ""): Boolean {
        if (typeNameLiveData.value == typeName) {
            return false
        }
        typeNameLiveData.value = typeName
        return true
    }



}
