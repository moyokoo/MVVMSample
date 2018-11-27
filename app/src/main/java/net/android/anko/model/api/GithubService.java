package net.android.anko.model.api;

import android.database.Observable;
import android.support.annotation.NonNull;

import net.android.anko.model.model.AccessTokenModel;
import net.android.anko.model.model.AuthRequestModel;
import net.android.anko.model.model.BasicTokenModel;
import net.android.anko.model.model.GithubListModel;
import net.android.anko.model.model.BranchModel;
import net.android.anko.model.model.FileModel;
import net.android.anko.model.model.RepositoryModel;
import net.android.anko.model.model.UserGithubModel;

import kotlinx.coroutines.experimental.Deferred;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface GithubService {

    @POST("authorizations")
    @Headers("Accept: application/json")
    Deferred<BasicTokenModel> authorizations(
            @NonNull @Body AuthRequestModel authModel
    );

    @GET("user")
    Deferred<UserGithubModel> user();

    @POST("https://github.com/login/oauth/access_token")
    @Headers("Accept: application/json")
    Deferred<AccessTokenModel> login(
            @Query("client_id") String clientId,
            @Query("client_secret") String clientSecret,
            @Query("code") String code,
            @Query("state") String state
    );

    @NonNull
    @GET("repos/{owner}/{repo}")
    Deferred<RepositoryModel> getRepoInfo(
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    @NonNull
    @PUT("user/subscriptions/{owner}/{repo}")
    Deferred<Response<ResponseBody>> watchRepo(
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    @NonNull
    @DELETE("user/subscriptions/{owner}/{repo}")
    Deferred<Response<ResponseBody>> unwatchRepo(
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    /**
     * Star a repository
     */
    @NonNull
    @PUT("user/starred/{owner}/{repo}")
    Deferred<Response<ResponseBody>> starRepo(
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    /**
     * Unstar a repository
     */
    @NonNull
    @DELETE("user/starred/{owner}/{repo}")
    Deferred<Response<ResponseBody>> unstarRepo(
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    @NonNull
    @POST("repos/{owner}/{repo}/forks")
    Deferred<RepositoryModel> createFork(
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    @NonNull
    @GET
    @Headers("Accept: application/vnd.github.html")
    Deferred<Response<ResponseBody>> getFileAsHtmlStream(
            @Url String url
    );

    @NonNull
    @GET
    @Headers("Accept: application/vnd.github.VERSION.raw")
    Deferred<Response<ResponseBody>> getFileAsStream(
            @Url String url
    );

    /**
     * Check if you are starring a repository
     */
    @NonNull
    @GET("user/starred/{owner}/{repo}")
    Deferred<Response<ResponseBody>> checkRepoStarred(
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    @NonNull
    @GET("user/subscriptions/{owner}/{repo}")
    Deferred<Response<ResponseBody>> checkRepoWatched(
            @Path("owner") String owner,
            @Path("repo") String repo
    );


    @NonNull
    @GET("repos/{owner}/{repo}/contents/{path}")
    Deferred<GithubListModel<FileModel>> getRepoFiles(
            @Path("owner") String owner,
            @Path("repo") String repo,
            @Path(value = "path", encoded = true) String path,
            @Query("ref") String branch
    );

    @NonNull
    @GET("repos/{owner}/{repo}/branches")
    Deferred<GithubListModel<BranchModel>> getBranches(
            @Path("owner") String owner,
            @Path("repo") String repo
    );
}
