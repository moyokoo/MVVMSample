package net.android.anko.ui.login;

import android.arch.lifecycle.ViewModel;

import net.android.anko.base.di.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class LoginModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    abstract ViewModel bindLoginPresenter(LoginViewModel loginPresenter);
}
