package net.android.anko.paging

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import net.android.anko.model.ApiGankRepository
import net.android.anko.model.model.GankModel
import net.android.anko.ui.gank.GankFactory
import net.android.anko.ui.video.GankVideoFactory
import javax.inject.Inject

class GankNetRepository @Inject constructor(val r: ApiGankRepository) : GankRepository {
    override fun gankVideo(typeName: String): Listing<GankModel> {
        val sourceFactory = GankVideoFactory(typeName, r)
        val pagedListConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .build()
        //开启线程进行数据源的处理 处理结束后通知LiveData(pagedList
        val pagedList: LiveData<PagedList<GankModel>> = LivePagedListBuilder(sourceFactory, pagedListConfig).build()
        val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
            it.initialLoad
        }
        return Listing(
                pagedList = pagedList,
                networkState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                    it.networkState
                },
                retry = {
                    sourceFactory.sourceLiveData.value?.retryFailed()
                },
                refresh = {
                    sourceFactory.sourceLiveData.value?.invalidate()
                },
                refreshState = refreshState,
                clear = {
                    sourceFactory.sourceLiveData.value?.clear()
                }
        )
    }

    override fun gank(typeName: String): Listing<GankModel> {
        val sourceFactory = GankFactory(typeName, r)
        val pagedListConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(30)
                .setPageSize(30)
                .build()
        //开启线程进行数据源的处理 处理结束后通知LiveData(pagedList
        val pagedList: LiveData<PagedList<GankModel>> = LivePagedListBuilder(sourceFactory, pagedListConfig).build()
        val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
            it.initialLoad
        }
        return Listing(
                pagedList = pagedList,
                networkState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                    it.networkState
                },
                retry = {
                    sourceFactory.sourceLiveData.value?.retryFailed()
                },
                refresh = {
                    sourceFactory.sourceLiveData.value?.invalidate()
                },
                refreshState = refreshState,
                clear = {
                    sourceFactory.sourceLiveData.value?.clear()
                }
        )
    }

}