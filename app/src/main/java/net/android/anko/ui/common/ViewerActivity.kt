package net.android.anko.ui.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Menu
import net.android.anko.R
import net.android.anko.base.BaseActivityWithoutViewModel
import net.android.anko.helper.Bundler
import net.android.anko.model.model.FileModel

class ViewerActivity : BaseActivityWithoutViewModel() {
    enum class ViewerType {
        RepoFile, MarkDown, Image, HtmlSource
    }

    companion object {
        fun showHtmlSource(context: Context, title: String,
                           htmlSource: String) {
            val intent = Intent(context, ViewerActivity::class.java)
            intent.putExtras(Bundler.start().put("viewerType", ViewerType.HtmlSource)
                    .put("title", title).put("source", htmlSource).end())
            context.startActivity(intent)
        }

        fun showMdSource(context: Context, title: String,
                         mdSource: String) {
            val intent = Intent(context, ViewerActivity::class.java)
            intent.putExtras(Bundler.start().put("viewerType", ViewerType.MarkDown)
                    .put("title", title).put("source", mdSource).end())
            context.startActivity(intent)
        }

        fun showImage(context: Context, imageUrl: String) {
            val title = imageUrl.substring(imageUrl.lastIndexOf("/") + 1)
            showImage(context, title, imageUrl)
        }

        fun showImage(context: Context, title: String,
                      imageUrl: String) {
            val intent = Intent(context, ViewerActivity::class.java)
            intent.putExtras(Bundler.start().put("viewerType", ViewerType.Image)
                    .put("title", title).put("imageUrl", imageUrl).end())
            context.startActivity(intent)
        }

        fun show(context: Context, url: String, htmlUrl: String?) {
            show(context, url, htmlUrl, null)
        }

        fun show(context: Context, url: String, htmlUrl: String?, downloadUrl: String?) {
            val fileModel = FileModel()
            fileModel.htmlUrl = htmlUrl
            fileModel.downloadUrl = downloadUrl
            fileModel.url = url
            fileModel.name = url.substring(url.lastIndexOf("/") + 1,
                    if (url.contains("?")) url.indexOf("?") else url.length)
            show(context, fileModel)
        }

        fun show(context: Context, fileModel: FileModel) {
            show(context, fileModel, null)
        }

        fun show(context: Context, fileModel: FileModel, repoName: String?) {
            val intent = Intent(context, ViewerActivity::class.java)
            intent.putExtras(Bundler.start().put("viewerType", ViewerType.RepoFile)
                    .put("fileModel", fileModel).put("repoName", repoName).end())
            context.startActivity(intent)
        }
    }

    override fun provideLayout(): Int {
        return R.layout.activity_single_fragment
    }

    internal var fileModel: FileModel? = null
    internal var repoName: String? = null
    internal var title: String? = null
    internal var source: String? = null
    internal var imageUrl: String? = null
    internal var viewerType: ViewerType? = null
    var fragment: Fragment? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (fileModel != null || imageUrl != null)
            menuInflater.inflate(R.menu.menu_viewer, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewerType = intent.getSerializableExtra("viewerType") as ViewerActivity.ViewerType?
        fileModel = intent?.getParcelableExtra("fileModel")
        title = intent?.getStringExtra("title")
        source = intent?.getStringExtra("source")
        imageUrl = intent?.getStringExtra("imageUrl")
        repoName = intent.getStringExtra("repoName")

        title = when (viewerType) {
            ViewerType.RepoFile -> fileModel?.name
            else -> this.title
        }
        supportActionBar?.title = title
        fragment = createFragment()
        supportFragmentManager.beginTransaction().add(R.id.container, fragment).commit()
    }

    fun createFragment(): ViewerFragment {
        return when (viewerType) {
            ViewerType.RepoFile -> ViewerFragment.create(fileModel!!)
            ViewerType.Image -> ViewerFragment.createForImage(title!!, imageUrl!!)
            ViewerType.HtmlSource -> ViewerFragment.createForHtml(title!!, source!!)
            else -> ViewerFragment.createForMd(title!!, source!!)
        }
    }
}