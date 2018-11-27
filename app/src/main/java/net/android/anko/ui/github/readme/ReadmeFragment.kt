package net.android.anko.ui.github.readme

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_readme.*
import miaoyongjun.autil.utils.LogUtils
import net.android.anko.R
import net.android.anko.base.BaseFragment
import net.android.anko.base.constant.AnkoConstant
import net.android.anko.ui.github.RepositotyViewModel
import net.android.anko.utils.extensions.gone
import net.android.anko.utils.extensions.visible
import net.android.anko.widgets.webview.CodeWebView
import javax.inject.Inject

class ReadmeFragment @Inject constructor() : BaseFragment<ReadmeViewModel>(), CodeWebView.ContentChangedListener {


    var baseUrl = ""
    override fun onContentChanged(progress: Int) {
        if (readmeLoader != null) {
            readmeLoader.progress = progress
            if (progress == 100) {
                readmeLoader.gone()
            }
        }
    }

    lateinit var repositotyViewModel: RepositotyViewModel

    override fun onScrollChanged(reachedTop: Boolean, scroll: Int) {
    }

    override fun providePresenterClass(): Class<ReadmeViewModel> {
        return ReadmeViewModel::class.java
    }

    override fun provideLayout(): Int {
        return R.layout.fragment_readme
    }

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {

        repositotyViewModel = ViewModelProviders.of(activity!!).get(RepositotyViewModel::class.java)
        repositotyViewModel.repositoryModel.observe(this, Observer {
            readmeLoader.visible()
            readmeLoader.isIndeterminate = true
            codeWebView.gone()
            val readmeFileUrl = (AnkoConstant.GITHUB_BASE_URL + "repos/" + it?.fullName
                    + "/" + "readme" + "?ref=" + it?.defaultBranch)
            baseUrl = (AnkoConstant.GITHUB_BASE_URL_NO_API + it?.fullName
                    + "/blob/" + it?.defaultBranch + "/" + "README.md")
            viewModel.getFileAsHtmlStream(readmeFileUrl)
        })

        viewModel.responseBody.observe(this, Observer {
            it?.let {
                codeWebView.setMdSource(it.string(), baseUrl, true)
                readmeLoader.gone()
                readmeLoader.isIndeterminate = false
                codeWebView.visible()
            }
        })

    }

}