package net.android.anko.ui.github.file

import android.arch.lifecycle.MutableLiveData
import net.android.anko.base.BaseViewModel
import net.android.anko.model.api.GithubService
import net.android.anko.model.model.BranchModel
import javax.inject.Inject

class BranchChooseViewModel @Inject constructor(var githubService: GithubService) : BaseViewModel() {


    var branchList = MutableLiveData<List<BranchModel>>()


    fun getBranches(owner: String?, repo: String?) {
        makeRestCall(
                githubService.getBranches(owner, repo),
                next = {
                    branchList.value = it.items
                }
        )
    }
}