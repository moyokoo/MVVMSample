package net.android.anko.paging

import net.android.anko.model.model.GankModel
import net.android.anko.paging.base.BasePagingRepository

interface GankRepository : BasePagingRepository {
    fun gank(typeName: String): Listing<GankModel>
    fun gankVideo(typeName: String): Listing<GankModel>
}