package net.android.anko.ui.girl

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.fragment_girl.*
import net.android.anko.R
import net.android.anko.model.model.GankModel
import net.android.anko.paging.base.BasePagingFragment
import net.android.anko.utils.banner.GallerySnapHelper
import net.android.anko.utils.extensions.itemClick
import net.android.anko.utils.extensions.loadImage
import javax.inject.Inject


class GirlFragment @Inject constructor() : BasePagingFragment<GankModel, GirlViewModel, GirlAdapter>() {
    lateinit var mSnapHelper: GallerySnapHelper
    override fun providePresenterClass(): Class<GirlViewModel> = GirlViewModel::class.java

    override fun provideLayout(): Int = R.layout.fragment_girl

    override fun isShowFullscreenLoading() = true

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
        super.onFragmentCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mSnapHelper = GallerySnapHelper()
        mSnapHelper.currentItemPos = 0
        mSnapHelper.attachToRecyclerView(recyclerView)
        adapter.itemClick {

        }
        viewModel.list?.observe(this, Observer {
            profileIv.loadImage(context, viewModel.list?.value?.get(mSnapHelper.currentItemPos)?.url)
        })
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    profileIv.loadImage(context, viewModel.list?.value?.get(mSnapHelper.currentItemPos)?.url)
                }
            }
        })
        viewModel.fetchData()
    }


}