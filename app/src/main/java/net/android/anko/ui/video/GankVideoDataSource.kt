package net.android.anko.ui.video

import net.android.anko.model.ApiGankRepository
import net.android.anko.model.model.GankModel
import net.android.anko.paging.base.BaseItemDataSource
import net.android.anko.utils.douyin.DYDecode
import retrofit2.Response
import java.io.IOException
import java.util.regex.Pattern


class GankVideoDataSource
internal constructor(var typeName: String, val repository: ApiGankRepository)
    : BaseItemDataSource<GankModel>() {

    override suspend fun onLoadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, GankModel>): List<GankModel> {
        val data = repository.getGank(typeName, params.requestedLoadSize, currentPage)
        val result = data.await().results
        mapData(result)
        return result
    }

    override suspend fun onLoadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, GankModel>): List<GankModel> {
        val data = repository.getGank(typeName, params.requestedLoadSize, currentPage)
        val result = data.await().results
        mapData(result)
        return result
    }

    fun mapData(data: List<GankModel>) {
        for (x in data) {
            if (x.url.contains("douyin")) {
                val url1 = urlAnalysisMethod(x.url)
                val url2 = DYDecode.NewUrlDecode(url1)
                val videoUrl = getURI(url2).trim()
                val imageUrl = DYDecode.getCover(url1).trim()
                x.videoUrl = videoUrl
                x.imageUrl = imageUrl
            }
        }
    }

    fun getURI(url: String): String {
        var responseBodyCall = repository.douyin(url)
        var response: Response<*>? = null
        try {
            response = responseBodyCall.execute()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return response!!.raw().request().url().uri().toString()
    }

    fun urlAnalysisMethod(url: String): String? {
        var url = url
        try {
            if (isContainChinese(url)) {
                url = cuthttpschinese(url)
            }
            if (url.length < 40) {
                url = getURI(url)
            }
            return url
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    /**
     * @param str
     * @return
     */
    // 检查是否有中文
    fun isContainChinese(str: String): Boolean {
        val p = Pattern.compile("[\u4e00-\u9fa5]")
        val m = p.matcher(str)
        return m.find()
    }

    /**
     * @param str
     * @return
     */
    // 截取到http开始的字段
    fun cuthttpschinese(str: String): String {
        val start = str.indexOf("http")
        return str.substring(start)
    }
}
