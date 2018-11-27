package net.android.anko.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.runBlocking
import miaoyongjun.autil.utils.LogUtils
import net.android.anko.helper.GithubConfigHelper
import net.android.anko.model.api.GithubService
import net.android.anko.model.model.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.ArrayList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiGithubRepository @Inject
constructor() {
    @Inject
    lateinit var githubService: GithubService

    fun accessToken(code: String, state: String): Deferred<AccessTokenModel> {
        return githubService
                .login(GithubConfigHelper.getClientId(), GithubConfigHelper.getSecret(), code, state)
    }

    fun authorizations(mode: AuthRequestModel): Deferred<BasicTokenModel> {
        return githubService.authorizations(mode)
    }

    fun user(): Deferred<UserGithubModel> {
        return githubService.user()
    }

    fun getRepoInfo(ower: String, repo: String): Deferred<RepositoryModel> {
        return githubService.getRepoInfo(ower, repo)
    }

    fun getFileAsHtmlStream(url: String): Deferred<Response<ResponseBody>> {
        return githubService.getFileAsHtmlStream(url)
    }



}
