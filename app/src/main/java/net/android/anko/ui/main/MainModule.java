package net.android.anko.ui.main;

import android.arch.lifecycle.ViewModel;

import net.android.anko.base.di.FragmentScoped;
import net.android.anko.base.di.ViewModelKey;
import net.android.anko.base.di.names.Local;
import net.android.anko.base.di.names.Remote;
import net.android.anko.paging.GankNetRepository;
import net.android.anko.paging.GankRepository;
import net.android.anko.ui.gank.GankFragment;
import net.android.anko.ui.gank.GankRootFragment;
import net.android.anko.ui.gank.GankViewModel;
import net.android.anko.ui.gank.ganksub.GankSubFragment;
import net.android.anko.ui.gank.ganksub.GankSubViewModel;
import net.android.anko.ui.girl.GirlFragment;
import net.android.anko.ui.girl.GirlViewModel;
import net.android.anko.ui.video.GankVideoFragment;
import net.android.anko.ui.video.GankVideoViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainModule {

    @Binds
    @IntoMap // 指定该@Provides方法向Map提供元素
    @ViewModelKey(MainViewModel.class)// 指定该元素在Map中所对应的的Key
    abstract ViewModel bindPresenterVm(MainViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(GankViewModel.class)
    abstract ViewModel bindGankViewModel(GankViewModel model);

    @ContributesAndroidInjector
    abstract GankRootFragment gankHomeFragment();

    @ContributesAndroidInjector
    abstract GankFragment gankFragment();

    @ContributesAndroidInjector
    abstract GankSubFragment gankSubFragment();

    @Binds
    @IntoMap
    @ViewModelKey(GankSubViewModel.class)
    abstract ViewModel bindGankSubViewModel(GankSubViewModel model);


    @Binds
    @IntoMap
    @ViewModelKey(GankVideoViewModel.class)
    abstract ViewModel bindGankVideoViewModel(GankVideoViewModel model);

    @FragmentScoped
    @ContributesAndroidInjector
    abstract GankVideoFragment messageFragment();

    @Binds
    @IntoMap
    @ViewModelKey(GirlViewModel.class)
    abstract ViewModel bindGirlViewModel(GirlViewModel model);

    @ContributesAndroidInjector
    abstract GirlFragment girlFragment();


    @Binds
    @Remote
    abstract GankRepository repositoryNet(GankNetRepository gankNetRepository);

    @Binds
    @Local
    abstract GankRepository repositoryLocal(GankNetRepository gankNetRepository);

}
