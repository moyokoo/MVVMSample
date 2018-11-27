package net.android.anko.ui.gank

import android.arch.lifecycle.ViewModelProviders
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import miaoyongjun.autil.utils.LogUtils
import net.android.anko.R
import net.android.anko.base.di.ActivityScoped
import net.android.anko.helper.AppHelper
import net.android.anko.helper.Bundler
import net.android.anko.model.model.GankModel
import net.android.anko.paging.HeaderTransform
import net.android.anko.paging.base.BasePagingFragment
import net.android.anko.ui.gank.adapter.GankAdapter
import net.android.anko.ui.gank.adapter.GankHeaderHelper
import net.android.anko.ui.gank.adapter.GankTypeAdapter
import net.android.anko.ui.gank.ganksub.GankSubFragment
import net.android.anko.ui.main.MainViewModel
import net.android.anko.utils.extensions.changeGankToolbar
import net.android.anko.utils.extensions.itemClick
import javax.inject.Inject
import javax.inject.Provider

class GankFragment @Inject constructor() : BasePagingFragment<GankModel, GankViewModel, GankAdapter>() {

    companion object {
        val TAG = GankFragment::class.java.simpleName
        const val TYPE_NAME = "typeName"
    }

    @Inject
    lateinit var gankSubFragmentProvider: Provider<GankSubFragment>

    fun addBundle(typeName: String) {
        arguments = Bundler.start().put(TYPE_NAME, typeName).end()
    }

    lateinit var mainViewModel: MainViewModel

    override fun providePresenterClass(): Class<GankViewModel> {
        return GankViewModel::class.java
    }

    override fun provideLayout(): Int {
        return R.layout.fragment_paging
    }

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
        super.onFragmentCreated(view, savedInstanceState)
        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        adapter.addHeader(object : HeaderTransform {
            override fun createView(): View {
                return headerView()
            }
        })
        adapter.itemClick {
            val uri = Uri.parse(viewModel.list?.value?.get(it)?.url)
            AppHelper.launchUrl(context, uri)
        }
        viewModel.fetchTypeName(arguments?.getString(TYPE_NAME))
    }

    fun headerView(): View {
        val itemView = layoutInflater.inflate(R.layout.fragment_gank_type, recyclerView.parent as ViewGroup, false)
        val typeRecyclerView = itemView.findViewById<RecyclerView>(R.id.recyclerView)
        val list = GankHeaderHelper.provideDrawerList(context!!)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        val adapter = GankTypeAdapter(list)
        adapter.setOnItemClickListener { _, _, position ->
            changeGankToolbar(true, list[position].name.toString())
            val gankSubFragment = gankSubFragmentProvider.get()
            gankSubFragment.addBundle(list[position].name.toString())
            start(gankSubFragment)
        }
        typeRecyclerView.adapter = adapter
        typeRecyclerView.layoutManager = linearLayoutManager
        return itemView
    }
}
