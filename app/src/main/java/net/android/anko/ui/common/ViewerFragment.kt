package net.android.anko.ui.common

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.fragment_viewer.*
import net.android.anko.R
import net.android.anko.base.BaseFragment
import net.android.anko.helper.Bundler
import net.android.anko.helper.PrefGetter
import net.android.anko.helper.StringHelper
import net.android.anko.model.model.FileModel
import net.android.anko.ui.common.ViewerActivity.ViewerType.*
import net.android.anko.widgets.webview.CodeWebView
import javax.inject.Inject

class ViewerFragment @Inject constructor() : BaseFragment<ViewerViewModel>(), CodeWebView.ContentChangedListener {
    internal var wrap = false
    internal var scrollYPercent = 0f
    override fun onContentChanged(progress: Int) {
        if (loader != null) {
            loader.progress = progress
            if (progress == 100) {
                loader.visibility = View.GONE
                //delay 300 mills, in case of content height unavailable
                webView.postDelayed({
                    if (webView == null) return@postDelayed
                    val scrollY = (webView.contentHeight * scrollYPercent).toInt()
                    webView.scrollTo(0, scrollY)
                }, 300)
            }
        }
    }

    override fun onScrollChanged(reachedTop: Boolean, scroll: Int) {

    }

    override fun providePresenterClass(): Class<ViewerViewModel> {
        return ViewerViewModel::class.java
    }

    override fun provideLayout(): Int {
        return R.layout.fragment_viewer
    }

    companion object {
        fun createForHtml(title: String, htmlSource: String): ViewerFragment {
            val fragment = ViewerFragment()
            fragment.arguments = Bundler.start().put("viewerType", HtmlSource)
                    .put("title", title).put("source", htmlSource).end()
            return fragment
        }

        fun createForMd(title: String, mdSource: String): ViewerFragment {
            val fragment = ViewerFragment()
            fragment.arguments = Bundler.start().put("viewerType", MarkDown)
                    .put("title", title).put("source", mdSource).end()
            return fragment
        }

        fun createForImage(title: String, imageUrl: String): ViewerFragment {
            val fragment = ViewerFragment()
            fragment.arguments = Bundler.start().put("viewerType", Image)
                    .put("title", title).put("imageUrl", imageUrl).end()
            return fragment
        }

        fun create(fileModel: FileModel): ViewerFragment {
            val fragment = ViewerFragment()
            fragment.arguments = Bundler.start().put("viewerType", RepoFile)
                    .put("fileModel", fileModel).end()
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPause() {
        super.onPause()
        if (webView != null) {
            val scrollY = webView.scrollY
            scrollYPercent = scrollY * 1.0f / webView.contentHeight
        }
    }

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
        wrap = PrefGetter.isCodeWrap()
        loader.visibility = View.VISIBLE
        loader.isIndeterminate = true
        viewModel.getIntent(arguments)
        viewModel.viewTypeLiveData.observe(this, Observer {
            when (it) {
                RepoFile -> loadCode(viewModel.downloadSource!!, viewModel.extension)
                Image -> loadImageUrl(viewModel.imageUrl!!)
                HtmlSource -> loadMdText(viewModel.source!!, null)
            }
        })
        viewModel.load()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val menuItem = menu.findItem(R.id.action_wrap_lines)
        val menuItemDownload = menu.findItem(R.id.action_download)
        val menuItemViewFile = menu.findItem(R.id.action_view_file)
        val menuItemRefresh = menu.findItem(R.id.action_refresh)
        when (viewModel.viewerType) {
            ViewerActivity.ViewerType.RepoFile -> {
                menuItem.isVisible = viewModel.isCode() && !StringHelper.isBlank(viewModel.downloadSource)
                menuItem.isChecked = wrap
                menuItemDownload.isVisible = !StringHelper.isBlank(viewModel.fileModel?.downloadUrl)
                menuItemViewFile.isVisible = false
                menuItemRefresh.isVisible = true
            }
            ViewerActivity.ViewerType.Image -> {
                menuItem.isVisible = false
                menuItemViewFile.isVisible = false
                menuItemRefresh.isVisible = false
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_wrap_lines) {
            item.isChecked = !item.isChecked
            wrap = item.isChecked
            PrefGetter.setCodeWrap(wrap)
            if (ViewerActivity.ViewerType.RepoFile == viewModel.viewerType) {
                loadCode(viewModel.downloadSource!!, viewModel.extension)
            }
            return true
        } else if (item.itemId == R.id.action_refresh) {
            refresh()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun refresh() {
        viewModel.load()
    }

    fun loadImageUrl(url: String) {
        webView.loadImage(url)
        webView.setContentChangedListener(this)
    }

    fun loadMdText(text: String, baseUrl: String?) {
        webView.setMdSource(text, baseUrl)
        webView.setContentChangedListener(this)
        loader.visibility = View.VISIBLE
        loader.isIndeterminate = false
    }

    fun loadCode(text: String, extension: String?) {
        webView.setCodeSource(text, wrap, extension)
        webView.setContentChangedListener(this)
        activity?.invalidateOptionsMenu()
        loader.visibility = View.VISIBLE
        loader.isIndeterminate = false
    }


    fun loadHtmlSource(htmlSource: String) {
        webView.setHtmlSource(htmlSource)
        webView.setContentChangedListener(this)
        loader.visibility = View.VISIBLE
        loader.isIndeterminate = false
    }



}