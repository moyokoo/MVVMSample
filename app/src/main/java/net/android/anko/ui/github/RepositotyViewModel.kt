package net.android.anko.ui.github

import android.arch.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import miaoyongjun.autil.utils.LogUtils
import net.android.anko.base.BaseViewModel
import net.android.anko.model.ApiGithubRepository
import net.android.anko.model.api.GithubService
import net.android.anko.model.model.RepositoryModel
import net.android.anko.utils.extensions.default
import net.android.anko.utils.extensions.handleException
import org.jetbrains.anko.coroutines.experimental.Ref
import org.jetbrains.anko.coroutines.experimental.asReference
import javax.inject.Inject

class RepositotyViewModel @Inject constructor(var githubService: GithubService) : BaseViewModel() {
    var chooseBranch = MutableLiveData<String>()
    var watchCount = 0
    var watchCountLiveData = MutableLiveData<Int>()
    var starCount = 0
    var starCountLiveData = MutableLiveData<Int>()
    var isStared = false
    var isWatched = false
    var owner = ""
    var repo = ""
    val repositoryModel = MutableLiveData<RepositoryModel>()


    fun getRepo(owner: String, repo: String) {
        val ref: Ref<RepositotyViewModel> = this.asReference()
        launch {
            val r0 = githubService.getRepoInfo(owner, repo)
            val r1 = githubService.checkRepoStarred(owner, repo)
            val r2 = githubService.checkRepoWatched(owner, repo)
            var it: RepositoryModel? = null
            try {
                it = r0.await()
                ref().owner = it.owner.login
                ref().repo = it.name
                watchCount = it.subscribersCount
                starCount = it.stargazersCount
            } catch (e: Exception) {
                onError(e)
            }
            isStared = try {
                r1.await().isSuccessful
            } catch (e: Exception) {
                //404为没有star
                false
            }
            isWatched = try {
                r2.await().isSuccessful
            } catch (e: Exception) {
                false
            }
            launch(UI) {
                if (it != null) {
                    repositoryModel.value = it
                }
            }
        }
    }

    fun watch() {
        makeRestCall(if (isWatched) githubService.unwatchRepo(owner, repo) else githubService.watchRepo(owner, repo), next = {
            if (isWatched) {
                isWatched = false
                watchCount--
            } else {
                isWatched = true
                watchCount++
            }
            watchCountLiveData.value = watchCount
        })
    }

    fun star() {
        makeRestCall(if (isStared) githubService.unstarRepo(owner, repo) else githubService.starRepo(owner, repo), next = {
            if (isStared) {
                isStared = false
                starCount--
            } else {
                isStared = true
                starCount++
            }
            starCountLiveData.value = starCount
        })
    }

    fun fork() {

    }
}