package net.android.anko.base.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * private Provider<Map<Class<? extends ViewModel>, Provider<ViewModel>>>
 * mapOfClassOfAndProviderOfViewModelProvider;
 *      this.mapOfClassOfAndProviderOfViewModelProvider =
 *          MapProviderFactory.<Class<? extends ViewModel>, ViewModel>builder(2)
 *          .put(MainViewModel.class, (Provider) mainPresenterProvider)
 *          .put(WeiboViewModel.class, (Provider) weiboPresenterProvider)
 *          .build();
 * <p>
 *   由于创建JacquelineViewModelFactory需要提供Map中的key(Presenter),如果把所有的Presenter写在这里
 *   会导致在Presenter所在的module中,如果要在Presenter注入对象就必须要使用Inject,并且如果是第三方的库,则必须写在这里
 *   因为Presenter是在这里提供的,如果不使用Inject会导致错误
 *   Map<java.lang.Class<? extends ViewModel>,Provider<ViewModel>> cannot be provided without an @Provides-annotated method.
 *   因此,建立一个空的Presenter不让dagger报错,对应的Presenter则可以写到对应的module中
 */
@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(EmptyViewModel.class)
    abstract ViewModel bindEmptyViewmodel(EmptyViewModel emptyViewModel);

    /**
     * 每个子module都必须要提供JacquelineViewModelFactory中的Map<Class<? extends ViewModel>, Provider<ViewModel>> creators参数
     * 即提供 abstract ViewModel bindPresenterVm(BaseViewModel userViewModel);
     */
    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(AnkoViewModelFactory factory);
}