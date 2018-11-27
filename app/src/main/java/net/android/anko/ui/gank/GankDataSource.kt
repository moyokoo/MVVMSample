package net.android.anko.ui.gank

import kotlinx.coroutines.experimental.runBlocking
import miaoyongjun.autil.utils.LogUtils
import net.android.anko.model.ApiGankRepository
import net.android.anko.model.model.GankModel
import net.android.anko.paging.NetworkState
import net.android.anko.paging.base.BaseItemDataSource


class GankDataSource
internal constructor(var typeName: String, val repository: ApiGankRepository)
    : BaseItemDataSource<GankModel>() {

    override suspend fun onLoadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, GankModel>): List<GankModel> {
        val data = repository.getGank(typeName, params.requestedLoadSize, currentPage)
        return data.await().results
    }

    override suspend fun onLoadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, GankModel>): List<GankModel> {
        val data = repository.getGank(typeName, params.requestedLoadSize, currentPage)
        return data.await().results
    }
}
