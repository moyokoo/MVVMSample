package net.android.anko.ui.login

import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import net.android.anko.R
import net.android.anko.base.BaseViewModel
import net.android.anko.helper.PrefGetter
import net.android.anko.model.ApiGithubRepository
import net.android.anko.utils.extensions.default
import java.util.*
import javax.inject.Inject


class OauthViewModel @Inject
internal constructor(private val repositoryApiGithub: ApiGithubRepository) : BaseViewModel() {

    var fetchAccessTokenSuccess = MutableLiveData<Boolean>().default(false)
    val randomState = MutableLiveData<String>().default(UUID.randomUUID().toString())

    fun handleRedirectedUrl(url: String) {
        if (!url.contains("error")) {
            val uri = Uri.parse(url)
            val code = uri.getQueryParameter("code")
            val state = uri.getQueryParameter("state")
            accessToken(code, state)
        } else {
            showErrorMessage.value = R.string.invalid_url
        }
    }

    private fun accessToken(code: String, state: String) {
        makeRestCall(repositoryApiGithub.accessToken(code, state),
                next = {
                    PrefGetter.setToken(it.access_token)
                    fetchAccessTokenSuccess.value = true
                }
        )
    }
}
