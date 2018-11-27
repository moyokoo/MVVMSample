package net.android.anko.base.di;

import android.app.Application;
import android.support.annotation.Nullable;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory;

import net.android.anko.base.constant.AnkoConstant;
import net.android.anko.base.di.interceptors.AuthenticationInterceptor;
import net.android.anko.base.di.interceptors.ContentTypeInterceptor;
import net.android.anko.base.di.interceptors.PaginationInterceptor;
import net.android.anko.base.di.names.Gank;
import net.android.anko.base.di.names.Github;
import net.android.anko.model.GankRepositoryManager;
import net.android.anko.model.GithubRepositoryManager;
import net.android.anko.model.IRepositoryManager;
import net.android.anko.model.api.GankService;
import net.android.anko.model.api.GithubService;
import net.android.anko.utils.DataHelper;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 第三方库module
 *
 * @Provides 标注的将会生成一个Factory来提供一个对象, 并判断参数, 如果有参数, 则先把参数实例化
 * <p>
 * 最后需要在使用的地方Component才会将Factory关联起来
 */
@Module
public abstract class LibraryModule {
    private static final int TIME_OUT = 10;

    /**
     * 告诉dagger,IRepositoryManager是由RepositoryManager实现的,需要在RepositoryManager的构造函数上加上 @Inject
     * 并且你的构造函数中所需要的类,需要手动 @Provides 提供
     *
     * @param githubRepositoryManager
     * @return
     */
    @Singleton
    @Binds
    @Github
    abstract IRepositoryManager githubManager(GithubRepositoryManager githubRepositoryManager);

    @Singleton
    @Binds
    @Gank
    abstract IRepositoryManager gankManager(GankRepositoryManager githubRepositoryManager);

    @Singleton
    @Binds
    abstract Interceptor bindInterceptor(HttpLoggingInterceptor httpLoggingInterceptor);

    /**
     * 如果提供的参数中存在接口,你需要同时提供该接口的实现类的 @Provides
     * 如果有两个实现类,使用 @Name来区分
     *
     * @param builder
     * @param client
     * @param gson
     * @return {@link Retrofit}
     */
    @Singleton
    @Provides
    @Gank
    static Retrofit provideGankRetrofit(Retrofit.Builder builder, @Gank OkHttpClient client
            , Gson gson) {
        builder.baseUrl(AnkoConstant.GANK_BASE_URL)//域名
                .client(client)
                .addConverterFactory( new GithubResponseConverter(gson))
                .addCallAdapterFactory(CoroutineCallAdapterFactory.create());
        return builder.build();
    }

    @Singleton
    @Provides
    @Github
    static Retrofit provideGithubRetrofit(Retrofit.Builder builder, @Github OkHttpClient client
            , Gson gson) {
        builder.baseUrl(AnkoConstant.GITHUB_BASE_URL)//域名
                .client(client)
                .addConverterFactory(new GithubResponseConverter(gson))
                .addCallAdapterFactory(CoroutineCallAdapterFactory.create());
        return builder.build();
    }

    @Singleton
    @Provides
    static GithubService provideGithubService(@Github IRepositoryManager repositoryManager) {
        return repositoryManager.obtainRetrofitService(GithubService.class);
    }

    @Singleton
    @Provides
    static GankService provideGankService(@Gank IRepositoryManager repositoryManager) {
        return repositoryManager.obtainRetrofitService(GankService.class);
    }

    @Singleton
    @Provides
    @Gank
    static OkHttpClient provideGankClient(OkHttpClient.Builder builder) {
        return builder.build();
    }

    @Singleton
    @Provides
    @Github
    static OkHttpClient provideGithubClient(OkHttpClient.Builder builder) {
        builder.addInterceptor(new AuthenticationInterceptor());
        builder.addInterceptor(new PaginationInterceptor());
        builder.addInterceptor(new ContentTypeInterceptor());
        return builder.build();
    }

    @Singleton
    @Provides
    static Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder();
    }

    @Singleton
    @Provides
    static OkHttpClient.Builder provideClientBuilder(Interceptor intercept) {
        return new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(intercept);
    }

    @Singleton
    @Provides
    @Nullable
    static List<Interceptor> provideInterceptors() {
        return new ArrayList<>();
    }

    /**
     * 提供缓存文件
     */
    @Singleton
    @Provides
    static File provideCacheFile(Application application) {
        return DataHelper.getCacheFile(application);
    }


    @Singleton
    @Provides
    static Gson provideGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .setPrettyPrinting()
                .create();
    }

    @Provides
    @Singleton
    static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }


}
