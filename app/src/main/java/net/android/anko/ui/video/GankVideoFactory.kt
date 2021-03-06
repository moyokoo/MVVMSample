package net.android.anko.ui.video

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import net.android.anko.base.di.ActivityScoped
import net.android.anko.model.ApiGankRepository
import net.android.anko.model.model.GankModel
import net.android.anko.ui.gank.GankDataSource
import javax.inject.Inject

class GankVideoFactory constructor(var typeName: String, val r: ApiGankRepository) : DataSource.Factory<Int, GankModel>() {

    var sourceLiveData = MutableLiveData<GankVideoDataSource>()

    /**
     * 使用invalidate进行刷新操作,这里必须重新实例化,默认的DataSource的mInvalid为false,如果为原来的对象,mInvalid则为true,则无法刷新数据
     */
    override fun create(): DataSource<Int, GankModel> {
        val mainDrawDataSource = GankVideoDataSource(typeName, r)
        sourceLiveData.postValue(mainDrawDataSource)
        return mainDrawDataSource
    }
}
