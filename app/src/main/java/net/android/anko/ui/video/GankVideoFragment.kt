package net.android.anko.ui.video

import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_gank_video.*
import net.android.anko.R
import net.android.anko.base.BaseFragment
import net.android.anko.helper.AppHelper
import net.android.anko.model.model.GankModel
import net.android.anko.paging.base.BasePagingFragment
import net.android.anko.ui.main.MainActivity
import net.android.anko.utils.extensions.click
import net.android.anko.utils.extensions.gone
import net.android.anko.utils.extensions.itemClick
import net.android.anko.utils.extensions.visible
import javax.inject.Inject

class GankVideoFragment @Inject
constructor() : BasePagingFragment<GankModel, GankVideoViewModel, GankVideoAdapter>() {

    companion object {
        val TAG = GankVideoFragment::class.java.simpleName
    }

    override fun providePresenterClass(): Class<GankVideoViewModel> {
        return GankVideoViewModel::class.java
    }

    override fun provideLayout(): Int {
        return R.layout.fragment_gank_video
    }

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
        super.onFragmentCreated(view, savedInstanceState)
        adapter.itemClick {
            val entity = viewModel.list?.value?.get(it)
            if (TextUtils.isEmpty(entity?.videoUrl)) {
                val uri = Uri.parse(entity?.url)
                AppHelper.launchUrl(context, uri)
            } else {
                (activity as MainActivity).playVideo(entity?.videoUrl.toString())
            }
        }
        viewModel.fetchData()
    }
}
