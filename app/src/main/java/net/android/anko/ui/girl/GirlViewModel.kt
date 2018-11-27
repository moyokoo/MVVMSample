package net.android.anko.ui.girl

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import net.android.anko.base.BaseViewModel
import net.android.anko.base.di.names.Remote
import net.android.anko.model.model.GankModel
import net.android.anko.paging.GankRepository
import net.android.anko.paging.base.BasePagingViewModel
import javax.inject.Inject

class GirlViewModel @Inject constructor(@Remote val repository: GankRepository) : BasePagingViewModel<GankModel>() {
    val fetchLiveData = MutableLiveData<Boolean>()
    override fun initPagingList() {
        pagingList = Transformations.map(fetchLiveData) {
            repository.gank("福利")
        }
    }

    fun fetchData(typeName: Boolean = true): Boolean {
        if (this.fetchLiveData.value == typeName) {
            return false
        }
        fetchLiveData.value = !typeName
        return true
    }

}