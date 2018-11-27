package net.android.anko.paging

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(
        val status: Status,
        val throwable: Throwable? = null) {
    companion object {
        //加载完成
        val LOADED = NetworkState(Status.SUCCESS)
        //正在加载中
        val LOADING = NetworkState(Status.RUNNING)

        fun error(throwable: Throwable?) = NetworkState(Status.FAILED, throwable)
    }
}