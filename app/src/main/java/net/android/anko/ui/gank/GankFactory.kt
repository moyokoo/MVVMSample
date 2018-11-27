package net.android.anko.ui.gank

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import net.android.anko.base.di.ActivityScoped
import net.android.anko.model.ApiGankRepository
import net.android.anko.model.model.GankModel
import javax.inject.Inject

class GankFactory constructor(var typeName: String, val r: ApiGankRepository) : DataSource.Factory<Int, GankModel>() {

    var sourceLiveData = MutableLiveData<GankDataSource>()

    /**
     * 使用invalidate进行刷新操作,这里必须重新实例化,默认的DataSource的mInvalid为false,如果为原来的对象,mInvalid则为true,则无法刷新数据
     */
    override fun create(): DataSource<Int, GankModel> {
        val mainDrawDataSource = GankDataSource(typeName, r)
        sourceLiveData.postValue(mainDrawDataSource)
        return mainDrawDataSource
    }
}
