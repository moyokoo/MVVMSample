package net.android.anko.ui.common;

import android.arch.lifecycle.ViewModel;

import net.android.anko.base.di.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewerModule {

    @Binds
    @IntoMap
    @ViewModelKey(ViewerViewModel.class)
    abstract ViewModel bind(ViewerViewModel model);

    @ContributesAndroidInjector
    abstract ViewerFragment viewerFragment();
}
