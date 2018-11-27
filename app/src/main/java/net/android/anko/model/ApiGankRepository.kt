package net.android.anko.model

import kotlinx.coroutines.experimental.Deferred
import net.android.anko.model.api.GankService
import net.android.anko.model.model.GankBaseModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Url
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiGankRepository @Inject
constructor() {
    @Inject
    lateinit var gankService: GankService


    fun getGank(type: String, pageSize: Int, page: Int): Deferred<GankBaseModel> {
        return gankService.gank(type, pageSize, page)
    }

    fun douyin(url: String): Call<ResponseBody> {
        return gankService.douyin(url)
    }

}
