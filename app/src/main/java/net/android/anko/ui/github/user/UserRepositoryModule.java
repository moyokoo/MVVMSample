package net.android.anko.ui.github.user;

import android.arch.lifecycle.ViewModel;

import net.android.anko.base.di.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public abstract class UserRepositoryModule {

    @Binds
    @IntoMap
    @ViewModelKey(UserRepositoryViewModel.class)
    abstract ViewModel bind(UserRepositoryViewModel model);


    @Binds
    @IntoMap
    @ViewModelKey(StarViewModel.class)
    abstract ViewModel bind2(StarViewModel model);

    @ContributesAndroidInjector
    abstract StarFragment starFragment();
}
