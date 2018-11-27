package net.android.anko.base

import android.support.annotation.StringRes

interface IView {
    fun showMessage(msgRes: Any?)

    fun showMessage(@StringRes titleRes: Int, @StringRes msgRes: Int)

    fun showMessage(titleRes: String, msgRes: String)

    fun showErrorMessage(msgRes: Any?)

    fun isLoggedIn(): Boolean

    fun hideProgress()

    fun showProgress(layout: Int = 0)

    fun onRequireLogin()

    fun onScrollTop()

}