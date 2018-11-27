package net.android.anko.paging

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import miaoyongjun.autil.utils.LogUtils
import net.android.anko.R
import net.android.anko.R.id.error_msg
import net.android.anko.helper.NetHelper
import net.android.anko.paging.base.BaseNetPagingViewHolder
import net.android.anko.utils.extensions.gone
import net.android.anko.utils.extensions.visible

/**
 * A View Holder that can display a loading or have click action.
 * It is used to show the network state of paging.
 */
class NetworkStateItemPagingViewHolder(view: View, private val retryCallback: (() -> Unit)?) : BaseNetPagingViewHolder(view) {
    private val mContext: Context = view.context
    val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
    private val retry = view.findViewById<Button>(R.id.retry_button)
    private val errorMsg = view.findViewById<TextView>(R.id.error_msg)
    val errorLayout = view.findViewById<LinearLayout>(R.id.errorLayout)

    init {
        retry.setOnClickListener {
            retryCallback?.let {
                retryCallback.invoke()
            }
        }
    }

    override fun bind(networkState: NetworkState?) {
        progressBar.visibility = if (networkState?.status == Status.RUNNING) View.VISIBLE else View.GONE
        errorLayout.visibility = if (networkState?.status == Status.FAILED) View.VISIBLE else View.GONE
        networkState?.throwable?.let {
            errorMsg.text = mContext.getString(NetHelper.getPrettifiedErrorMessage(networkState.throwable))
        }
    }

    companion object {
        fun create(parent: ViewGroup, retryCallback: (() -> Unit)?): NetworkStateItemPagingViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemPagingViewHolder(view, retryCallback)
        }
    }
}