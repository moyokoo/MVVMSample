package net.android.anko.ui.github.readme

import android.arch.lifecycle.MutableLiveData
import net.android.anko.base.BaseViewModel
import net.android.anko.model.ApiGithubRepository
import okhttp3.ResponseBody
import javax.inject.Inject

class ReadmeViewModel @Inject constructor(val repositoryApiGithub: ApiGithubRepository) : BaseViewModel() {
    var responseBody = MutableLiveData<ResponseBody>()
    fun getFileAsHtmlStream(url: String) {
        makeRestCall(
                repositoryApiGithub.getFileAsHtmlStream(url),
                next = {
                    responseBody.value = it.body()
                }
        )
    }
}