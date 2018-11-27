package net.android.anko.paging.base

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.view.View
import kotlinx.android.synthetic.main.fragment_paging.*
import miaoyongjun.autil.utils.LogUtils
import net.android.anko.R
import net.android.anko.R.id.progressBar
import net.android.anko.R.id.swipeRefreshLayout
import net.android.anko.base.BaseFragment
import net.android.anko.paging.NetworkState
import net.android.anko.paging.Status
import net.android.anko.utils.extensions.gone
import net.android.anko.utils.extensions.visible
import javax.inject.Inject


abstract class BasePagingFragment<T, VM : BasePagingViewModel<T>, A : BaseNetAdapter<T>> : BaseFragment<VM>() {

    private var mInAtTop = true
    private var mScrollTotal: Int = 0
    @Inject
    lateinit var adapter: A
    lateinit var recyclerView: RecyclerView

    open fun isShowFullscreenLoading() = false

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.initPagingList()
        viewModel.initLiveData()

        recyclerView = view.findViewById(R.id.recyclerView)
        adapter.retry {
            viewModel.retry()
        }

        closeDefaultAnimator()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mScrollTotal += dy
                mInAtTop = mScrollTotal <= 0
            }
        })

        viewModel.refreshState?.observe(this, Observer {
            if (!viewModel.isRefresh && isShowFullscreenLoading() && it == NetworkState.LOADING) {
                progressBar?.visible()
                swipeRefreshLayout?.gone()
            } else if (it != NetworkState.LOADING) {
                progressBar?.gone()
                swipeRefreshLayout?.visible()
                swipeRefreshLayout?.isRefreshing = it == NetworkState.LOADING
            }
            viewModel.isRefresh = false
        })
        swipeRefreshLayout?.setOnRefreshListener {
            viewModel.refresh()
        }
        viewModel.list?.observe(this, Observer {
            adapter.submitList(it)
        })
        viewModel.networkState?.observe(this, Observer {
            adapter.setNetworkState(it)
        })

    }

    override fun onScrollTop() {
        if (mInAtTop) {
            viewModel.refresh()
        } else {
            (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0)
        }
    }

    private fun closeDefaultAnimator() {
        recyclerView.itemAnimator.addDuration = 0
        recyclerView.itemAnimator.changeDuration = 0
        recyclerView.itemAnimator.moveDuration = 0
        recyclerView.itemAnimator.removeDuration = 0
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        recyclerView.itemAnimator = null
    }


}
