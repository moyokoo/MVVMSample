package net.android.anko.ui.github;

import android.arch.lifecycle.ViewModel;

import net.android.anko.base.di.ViewModelKey;
import net.android.anko.ui.github.file.BranchChooseFragment;
import net.android.anko.ui.github.file.BranchChooseViewModel;
import net.android.anko.ui.github.file.FileFragment;
import net.android.anko.ui.github.file.FileViewModel;
import net.android.anko.ui.github.readme.ReadmeFragment;
import net.android.anko.ui.github.readme.ReadmeViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public abstract class RepositoryModule {
    @Binds
    @IntoMap
    @ViewModelKey(RepositotyViewModel.class)
    abstract ViewModel bindReModel(RepositotyViewModel model);


    @Binds
    @IntoMap
    @ViewModelKey(ReadmeViewModel.class)
    abstract ViewModel bindReadmeModel(ReadmeViewModel model);


    @ContributesAndroidInjector
    abstract ReadmeFragment readmeFragment();


    @Binds
    @IntoMap
    @ViewModelKey(BranchChooseViewModel.class)
    abstract ViewModel bindBranchModel(BranchChooseViewModel model);

    @ContributesAndroidInjector
    abstract BranchChooseFragment branchChooseFragment();


    @Binds
    @IntoMap
    @ViewModelKey(FileViewModel.class)
    abstract ViewModel bindFileModel(FileViewModel model);

    @ContributesAndroidInjector
    abstract FileFragment fileFragment();
}
