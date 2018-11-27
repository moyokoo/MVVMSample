package net.android.anko.ui.gank.ganksub

import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import miaoyongjun.autil.utils.LogUtils
import net.android.anko.R
import net.android.anko.helper.AppHelper
import net.android.anko.helper.Bundler
import net.android.anko.model.model.GankModel
import net.android.anko.paging.base.BasePagingFragment
import net.android.anko.ui.gank.GankFragment
import net.android.anko.ui.gank.adapter.GankAdapter
import net.android.anko.utils.extensions.itemClick
import javax.inject.Inject

class GankSubFragment @Inject constructor(): BasePagingFragment<GankModel,GankSubViewModel, GankAdapter>() {
    companion object {
        val TAG = GankSubFragment::class.java.simpleName
    }

    fun addBundle(typeName: String) {
        arguments = Bundler.start().put(GankFragment.TYPE_NAME, typeName).end()
    }

    override fun providePresenterClass(): Class<GankSubViewModel> {
        return GankSubViewModel::class.java
    }

    override fun provideLayout(): Int {
        return R.layout.fragment_paging
    }

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
        super.onFragmentCreated(view, savedInstanceState)
        adapter.itemClick {
            val uri = Uri.parse(viewModel.list?.value?.get(it)?.url)
            AppHelper.launchUrl(context, uri)
        }
        viewModel.fetchTypeName(arguments?.getString(GankFragment.TYPE_NAME))
    }

}
