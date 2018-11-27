package net.android.anko.ui.common

import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import android.text.TextUtils
import net.android.anko.R
import net.android.anko.base.BaseViewModel
import net.android.anko.model.api.GithubService
import net.android.anko.model.model.FileModel
import net.android.anko.widgets.webview.GitHubHelper
import net.android.anko.widgets.webview.GitHubHelper.getExtension
import javax.inject.Inject

class ViewerViewModel @Inject constructor(val githubService: GithubService) : BaseViewModel() {
    var viewTypeLiveData = MutableLiveData<ViewerActivity.ViewerType>()
    var viewerType: ViewerActivity.ViewerType? = null

    var fileModel: FileModel? = null
    var downloadSource: String? = null
    var title: String? = null
    var source: String? = null
    var imageUrl: String? = null
    var extension: String? = null

    fun getIntent(arguments: Bundle?) {
        viewerType = arguments?.getSerializable("viewerType") as ViewerActivity.ViewerType?
        fileModel = arguments?.getParcelable("fileModel")
        title = arguments?.getString("title")
        source = arguments?.getString("source")
        imageUrl = arguments?.getString("imageUrl")
        extension = GitHubHelper.getExtension(fileModel?.url)
    }

    fun load() {
        val url = fileModel?.url
        val htmlUrl = fileModel?.htmlUrl
        when (viewerType) {
            ViewerActivity.ViewerType.RepoFile -> {
                if (TextUtils.isEmpty(url) || TextUtils.isEmpty(htmlUrl)) {
                    showErrorMessage.value = R.string.invalid_url
                    return
                }
                if (GitHubHelper.isArchive(url)) {
                    showErrorMessage.value = R.string.view_archive_file_error
                    return
                }
                if (GitHubHelper.isImage(url)) {
                    imageUrl = fileModel?.downloadUrl
                    viewTypeLiveData.value = ViewerActivity.ViewerType.Image
                    return
                }
                makeRestCall(
                        if (GitHubHelper.isMarkdown(url)) githubService.getFileAsHtmlStream(url) else githubService.getFileAsStream(url),
                        next = {
                            downloadSource = it.body()?.string()
                            if (GitHubHelper.isMarkdown(url)) {
                                source = downloadSource
                                viewTypeLiveData.value = ViewerActivity.ViewerType.HtmlSource
                            } else {
                                viewTypeLiveData.value = ViewerActivity.ViewerType.RepoFile
                            }
                        }
                )
            }
            ViewerActivity.ViewerType.Image -> {
                viewTypeLiveData.value = ViewerActivity.ViewerType.Image
                return
            }
            ViewerActivity.ViewerType.HtmlSource -> {
                viewTypeLiveData.value = ViewerActivity.ViewerType.HtmlSource
                return
            }
            else -> {
                viewTypeLiveData.value = ViewerActivity.ViewerType.HtmlSource
                return
            }
        }


    }

    fun isCode(): Boolean {
        val url = if (fileModel != null) fileModel?.url else ""
        return !GitHubHelper.isArchive(url) &&
                !GitHubHelper.isImage(url) &&
                !GitHubHelper.isMarkdown(url)
    }
}