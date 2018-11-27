package net.android.anko.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.InflateException
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

import net.android.anko.R
import net.android.anko.utils.extensions.click
import net.android.anko.utils.extensions.gone

import java.util.HashMap

class AnkoEmptyLayout : FrameLayout {

    private var mInflater: LayoutInflater? = null
    var viewMap = mutableMapOf<String, View>()
    private val lock = Any()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        viewMap = HashMap()
        mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        addTypeView(R.layout.error_net_notify, NET_NOTIFY)
        addTypeView(R.layout.error_net_unwork, NET_ERROR)
        addTypeView(R.layout.error_empty_data, EMPTY_DATA)
        val size = childCount
        for (i in 0 until size) {
            getChildAt(i).gone()
        }
        visibility = View.GONE
    }

    fun addTypeView(res: Int, key: String) {
        if (viewMap.containsKey(key)) {
            throw InflateException("the key which you add is added")
        }
        val view = mInflater!!.inflate(res, null)
        synchronized(lock) {
            viewMap.put(key, view)
        }
        when (key) {
            NET_ERROR -> {
                view?.findViewById<Button>(R.id.notifyBtn)?.click {
                    showTypeView(NET_NOTIFY)
                }
            }
        }
        addView(view)
    }

    fun emptyIcon(icon: Int): AnkoEmptyLayout {
        val emptyView = getTypeView(EMPTY_DATA)
        emptyView?.findViewById<ImageView>(R.id.errorIv)?.setBackgroundResource(icon)
        return this
    }

    fun emptyMessage(msg: String): AnkoEmptyLayout {
        val emptyView = getTypeView(EMPTY_DATA)
        emptyView?.findViewById<TextView>(R.id.titleTv)?.text = msg
        return this
    }

    fun error(eb: DefaultErrorObserver): AnkoEmptyLayout {
        val netErrorView = getTypeView(NET_ERROR)
        netErrorView?.findViewById<TextView>(R.id.retryBtn)?.click {
            eb.onErrorNetRetry()
        }
        getTypeView(NET_NOTIFY)?.findViewById<Button>(R.id.retryBtn)?.click {
            eb.onNotifyRetry()
        }
        return this
    }

    interface ErrorObserver {
        fun onErrorNetRetry()
        fun onNotifyRetry()
    }

    interface DefaultErrorObserver : ErrorObserver {

        override fun onErrorNetRetry() {

        }

        override fun onNotifyRetry() {

        }
    }


    fun getTypeView(key: String): View? {
        synchronized(lock) {
            return viewMap[key]
        }
    }

    fun showEmpty() {
        showTypeView(EMPTY_DATA)
    }

    fun showNetError() {
        showTypeView(NET_ERROR)
    }

    fun showTypeView(key: String) {
        if (visibility == View.GONE) {
            visibility = View.VISIBLE
        }
        val s = childCount
        for (i in 0 until s) {
            if (getChildAt(i) === viewMap[key]) {
                getChildAt(i).visibility = View.VISIBLE
            } else {
                getChildAt(i).visibility = View.GONE
            }
        }
    }

    companion object {
        const val NET_ERROR = "netError"
        const val EMPTY_DATA = "emptyData"
        const val NET_NOTIFY = "netNotify"
    }
}
