package net.android.anko.helper

import net.android.anko.R
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

class NetHelper {
    companion object {
        fun getPrettifiedErrorMessage(throwable: Throwable?): Int {
            var resId = R.string.network_error
            if (throwable is HttpException) {
                resId = R.string.network_error
            } else if (throwable is IOException) {
                resId = R.string.request_error
            } else if (throwable is TimeoutException) {
                resId = R.string.unexpected_error
            }
            return resId
        }
    }
}