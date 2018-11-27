package net.android.anko.model.api

import kotlinx.coroutines.experimental.Deferred
import net.android.anko.model.model.GankBaseModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface GankService {

    //固定每页30个item
    @GET("data/{gankType}/{pageSize}/{page}")
    fun gank(@Path("gankType") gankType: String, @Path("pageSize") pageSize: Int, @Path("page") page: Int): Deferred<GankBaseModel>


    @GET
    fun douyin(@Url url: String): Call<ResponseBody>

}
