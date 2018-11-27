package net.android.anko.ui.login

import android.arch.lifecycle.MutableLiveData
import net.android.anko.base.BaseViewModel
import net.android.anko.base.ProStatus
import net.android.anko.helper.PrefGetter
import net.android.anko.model.ApiGithubRepository
import net.android.anko.model.model.AuthRequestModel
import net.android.anko.utils.extensions.default
import okhttp3.Credentials
import javax.inject.Inject


class LoginViewModel @Inject
internal constructor(private val repositoryApiGithub: ApiGithubRepository) : BaseViewModel() {

    var fetchAccessTokenSuccess = MutableLiveData<Boolean>().default(false)
    var showLoginProgress = MutableLiveData<ProStatus>().default(ProStatus.NOTHING)


    fun login(name: String, password: String) {
        val token = Credentials.basic(name, password)
        PrefGetter.setToken(token)
        val authModel = AuthRequestModel.generate()
        makeRestCall(
                repositoryApiGithub.authorizations(authModel),
                next = {
                    PrefGetter.setToken(it.token)
                    fetchUser()
                },
                error = { showLoginProgress.value = ProStatus.HIDE }
        )
    }

    fun fetchUser() {
        makeRestCall(
                repositoryApiGithub.user(),
                next = {
                    PrefGetter.setUserGithub(it)
                    fetchAccessTokenSuccess.value = true
                },
                error = { showLoginProgress.value = ProStatus.HIDE },
                complete = { showLoginProgress.value = ProStatus.HIDE }
        )
    }
}
