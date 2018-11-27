package net.android.anko.ui.login;

import android.arch.lifecycle.ViewModel;

import net.android.anko.base.di.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class OauthModule {

    @Binds
    @IntoMap
    @ViewModelKey(OauthViewModel.class)
    abstract ViewModel bindModel(OauthViewModel model);
}
