package net.android.anko.utils.extensions

import net.android.anko.R
import net.android.anko.base.BaseActivity
import net.android.anko.ui.gank.GankFragment
import net.android.anko.ui.main.MainActivity

fun GankFragment.changeGankToolbar(isBack: Boolean = false, title: String = resources.getString(R.string.app_name)) {
    if ((activity as BaseActivity<*>).supportActionBar != null) {
        (activity as BaseActivity<*>).supportActionBar!!.setHomeAsUpIndicator(if (isBack) R.drawable.ic_back else R.mipmap.ic_launcher)
        (activity as BaseActivity<*>).supportActionBar!!.title = title
        (activity as BaseActivity<*>).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as BaseActivity<*>).supportActionBar!!.setDisplayShowTitleEnabled((activity as BaseActivity<*>).showToolbarTitle())
    }
}