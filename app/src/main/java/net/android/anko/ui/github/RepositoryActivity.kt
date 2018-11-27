package net.android.anko.ui.github

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.KeyEvent
import kotlinx.android.synthetic.main.activity_repository.*
import kotlinx.android.synthetic.main.repo_header_icons_layout.*
import net.android.anko.R
import net.android.anko.base.BaseActivity
import net.android.anko.helper.Bundler
import net.android.anko.helper.StringHelper
import net.android.anko.model.model.FragmentPagerModel
import net.android.anko.ui.github.file.FileFragment
import net.android.anko.ui.github.readme.ReadmeFragment
import net.android.anko.ui.github.user.UserRepositoryActivity
import net.android.anko.utils.extensions.click
import net.android.anko.utils.extensions.loadProfileCircle
import net.android.anko.utils.extensions.start
import net.android.anko.widgets.MessageDialogView
import java.util.*
import javax.inject.Inject


class RepositoryActivity : BaseActivity<RepositotyViewModel>() {
    override fun showToolbarTitle() = false


    companion object {
        fun start(activity: Context, owner: String,
                  repoName: String) {
            val intent = createIntent(activity, owner, repoName)
            activity.startActivity(intent)
        }

        fun createIntent(activity: Context, owner: String,
                         repoName: String): Intent {
            return Intent(activity, RepositoryActivity::class.java)
                    .putExtras(Bundler.start()
                            .put("owner", owner)
                            .put("repoName", repoName).end())
        }
    }

    lateinit var fragmentViewPagerAdapter: FragmentViewPagerAdapter
    var fragments = mutableListOf<Fragment>()
    var fragmentPagerModels = mutableListOf<FragmentPagerModel>()
    @Inject
    lateinit var readmeFragment: ReadmeFragment
    @Inject
    lateinit var fileFragment: FileFragment

    override fun provideLayout(): Int {
        return R.layout.activity_repository
    }

    override fun providePresenterClass(): Class<RepositotyViewModel> {
        return RepositotyViewModel::class.java
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val titles = resources.getStringArray(R.array.repository_names)
        fragments.clear()
        fragments.add(readmeFragment)
        fragments.add(fileFragment)
        for (x in 0 until titles.size) {
            val model = FragmentPagerModel(titles[x], fragments[x])
            fragmentPagerModels.add(model)
        }
        fragmentViewPagerAdapter = FragmentViewPagerAdapter(supportFragmentManager)
        fragmentViewPagerAdapter.pagerList = fragmentPagerModels
        tabLayout.setupWithViewPager(viewPager)
        viewPager.adapter = fragmentViewPagerAdapter
        showProgress()
        viewModel.getRepo(intent.getStringExtra("owner"), intent.getStringExtra("repoName"))
        viewModel.repositoryModel.observe(this, Observer {
            hideProgress()
            repoNameTv.text = it?.fullName
            repoDescTv.text = it?.description
            repoDetailTv.text = StringHelper.getNewsTimeStr(this, it?.updatedAt
                    ?: Date()) + " " + StringHelper.getSizeString(it?.size?.toLong() ?: 0)
            repoLangTv.text = it?.language ?: ""
            watchRepoTv.text = it?.subscribersCount.toString()
            starRepoTv.text = it?.stargazersCount.toString()
            forkRepoTv.text = it?.forksCount.toString()
            profileIv.loadProfileCircle(this, it?.owner?.avatarUrl)
            watchRepoImage.tintDrawableColor(resources.getColor(if (viewModel.isWatched) R.color.colorAccent else R.color.textColorPrimary))
            starRepoImage.tintDrawableColor(resources.getColor(if (viewModel.isStared) R.color.colorAccent else R.color.textColorPrimary))
        })
        viewModel.watchCountLiveData.observe(this, Observer {
            watchRepoImage.tintDrawableColor(resources.getColor(if (viewModel.isWatched) R.color.colorAccent else R.color.textColorPrimary))
            watchRepoTv.text = it.toString()
        })
        viewModel.starCountLiveData.observe(this, Observer {
            starRepoImage.tintDrawableColor(resources.getColor(if (viewModel.isStared) R.color.colorAccent else R.color.textColorPrimary))
            starRepoTv.text = it.toString()
        })

        watchRepoLayout.click {
            viewModel.watch()
        }
        starRepoLayout.click {
            viewModel.star()
        }
        forkRepoLayout.click {
            MessageDialogView.newInstance("FORK", "Fork " +
                    viewModel.repositoryModel.value?.fullName + " ?").show(supportFragmentManager, MessageDialogView.TAG)
            viewModel.fork()
        }
        profileIv.click {
            start(UserRepositoryActivity::class.java)
        }
    }

    override fun retryNet() {
        viewModel.getRepo(intent.getStringExtra("owner"), intent.getStringExtra("repoName"))
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val fragment = fragmentViewPagerAdapter.curFragment
        return if (fragment != null
                && fragment is FileFragment && fragment.onKeyDown(keyCode, event)) {
            return true
        } else super.onKeyDown(keyCode, event)
    }

}