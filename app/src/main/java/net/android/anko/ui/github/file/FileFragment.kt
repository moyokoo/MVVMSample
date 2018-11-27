package net.android.anko.ui.github.file

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.fragment_file.*
import miaoyongjun.autil.utils.LogUtils
import net.android.anko.R
import net.android.anko.R.id.*
import net.android.anko.base.BaseFragment
import net.android.anko.model.model.FileModel
import net.android.anko.model.model.FilePathModel
import net.android.anko.ui.common.ViewerActivity
import net.android.anko.ui.github.RepositotyViewModel
import net.android.anko.utils.extensions.click
import javax.inject.Inject

class FileFragment @Inject constructor() : BaseFragment<FileViewModel>() {


    lateinit var repositotyViewModel: RepositotyViewModel

    var fileList = mutableListOf<FileModel>()
    var filePathList = mutableListOf<FilePathModel>()
    lateinit var repoFilesAdapter: RepoFilesAdapter
    lateinit var filePathAdapter: FilePathAdapter


    override fun providePresenterClass(): Class<FileViewModel> {
        return FileViewModel::class.java
    }

    override fun provideLayout(): Int {
        return R.layout.fragment_file
    }

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
        repoFilesAdapter = RepoFilesAdapter(fileList)
        filePathAdapter = FilePathAdapter(filePathList)
        pathRecyclerView?.adapter = filePathAdapter
        pathRecyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        filesRecyclerView?.adapter = repoFilesAdapter
        filesRecyclerView?.layoutManager = LinearLayoutManager(context)
        repoFilesAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
            if (repoFilesAdapter.data[position].isDir) {
                refreshLayout.isRefreshing = true
                viewModel.getRepoFiles(repoFilesAdapter.data[position].path)
            } else {
                ViewerActivity.show(activity!!, repoFilesAdapter.data[position], repositotyViewModel.repo)
            }
        }

        filePathAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
            refreshLayout.isRefreshing = true
            viewModel.getRepoFiles(filePathAdapter.data[position].fullPath, true)
        }
        refreshLayout.setOnRefreshListener {
            viewModel.getRepoFiles(isReload = true)
        }

        repositotyViewModel = ViewModelProviders.of(activity!!).get(RepositotyViewModel::class.java)
        repositotyViewModel.repositoryModel.observe(this, Observer {
            viewModel.initData(it)
        })
        repositotyViewModel.chooseBranch.observe(this, Observer {
            viewModel.currentBranch = it.toString()
            viewModel.currentBranchLiveData.value = it.toString()
            viewModel.currentPath = ""
            viewModel.getRepoFiles(isReload = true)
        })
        viewModel.filePathLiveData.observe(this, Observer {
            filePathAdapter.setNewData(it)
        })
        viewModel.fileLiveData.observe(this, Observer {
            refreshLayout.isRefreshing = false
            repoFilesAdapter.setNewData(it)
        })
        viewModel.currentBranchLiveData.observe(this, Observer {
            branchNameTv.text = it
        })

        branchLayout.click {
            BranchDialogFragment.newInstance(viewModel.repositoryModel?.owner?.login, viewModel.repositoryModel?.name)
                    .show(childFragmentManager, "BranchDialogFragment")
        }
    }


    fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK && event?.action == KeyEvent.ACTION_DOWN) {
            viewModel.goBack()
        } else false
    }
}