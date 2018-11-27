package net.android.anko.ui.github.file

import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import miaoyongjun.autil.utils.LogUtils
import miaoyongjun.autil.utils.StringUtils
import net.android.anko.base.BaseViewModel
import net.android.anko.helper.StringHelper
import net.android.anko.model.ApiGithubRepository
import net.android.anko.model.api.GithubService
import net.android.anko.model.model.BranchModel
import net.android.anko.model.model.FileModel
import net.android.anko.model.model.FilePathModel
import net.android.anko.model.model.RepositoryModel
import java.util.*
import javax.inject.Inject

class FileViewModel @Inject constructor(var githubService: GithubService) : BaseViewModel() {

    var currentBranchLiveData = MutableLiveData<String>()
    var currentBranch = ""
    var currentPath = ""
    var repositoryModel: RepositoryModel? = null
    var filePathLiveData = MutableLiveData<List<FilePathModel>>()
    var fileLiveData = MutableLiveData<List<FileModel>>()
    var filePath = mutableListOf<FilePathModel>()
    var homePath: FilePathModel = FilePathModel("", "")
    val cacheMap = mutableMapOf<String, List<FileModel>>()

    init {
        filePath.add(homePath)
    }

    fun initData(it: RepositoryModel?) {
        it?.let {
            if (currentBranch == "") currentBranch = it.defaultBranch
            repositoryModel = it
        }
        currentBranchLiveData.value = currentBranch
        getRepoFiles()
    }

    private fun updateFilePath() {
        filePath.clear()
        filePath.add(homePath)
        if (!StringHelper.isBlank(currentPath)) {
            val pathArray = currentPath.split("/")
            for (i in pathArray.indices) {
                val name = pathArray[i]
                var fullPath = ""
                for (j in 0..i) {
                    fullPath = fullPath + pathArray[j] + "/"
                }
                fullPath = if (fullPath.endsWith("/"))
                    fullPath.substring(0, fullPath.length - 1)
                else
                    fullPath
                val path = FilePathModel(name, fullPath)
                filePath.add(path)
            }
        }
        filePathLiveData.value = filePath
    }


    private fun getCacheKey(): String {
        return "$currentBranch-$currentPath"
    }

    fun getRepoFiles(path: String = "", isReload: Boolean = false) {
        currentPath = if (path == "") if (currentPath == "") path else currentPath else path
        updateFilePath()
        val filesCache = cacheMap[getCacheKey()];
        if (!isReload && filesCache != null) {
            fileLiveData.value = filesCache
            return
        }
        makeRestCall(
                githubService.getRepoFiles(repositoryModel?.owner?.login, repositoryModel?.name, currentPath, currentBranch),
                next = {
                    it.items.sortWith(Comparator { o1, o2 ->
                        if (o1.type != o2.type) {
                            if (o1.isDir) -1 else 1
                        } else 0
                    })
                    cacheMap[getCacheKey()] = it.items
                    fileLiveData.value = it.items
                }
        )
    }

    fun goBack(): Boolean {
        if (!StringHelper.isBlank(currentPath)) {
            currentPath = if (currentPath.contains("/"))
                currentPath.substring(0, currentPath.lastIndexOf("/"))
            else
                ""
            getRepoFiles()
            return true
        }
        return false
    }
}