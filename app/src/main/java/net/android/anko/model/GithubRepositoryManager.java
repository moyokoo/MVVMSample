
package net.android.anko.model;

import android.app.Application;
import android.content.Context;


import net.android.anko.base.di.names.Github;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;
import retrofit2.Retrofit;

/**
 * 用来管理网络请求层,以及数据缓存层,以后可能添加数据库请求层
 */
@Singleton
public class GithubRepositoryManager implements IRepositoryManager {
    Lazy<Retrofit> mRetrofit;
    Application mApplication;

    @Inject
    public GithubRepositoryManager(@Github Lazy<Retrofit> retrofit, Application application) {
        this.mRetrofit = retrofit;
        this.mApplication = application;
    }


    @Override
    public <T> T obtainRetrofitService(Class<T> service) {
        return mRetrofit.get().create(service);
    }

    @Override
    public Context getContext() {
        return mApplication;
    }
}
