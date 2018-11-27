package net.android.anko.base.di;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;


public class AnkoViewModelFactory implements ViewModelProvider.Factory {

    /**
     * Map中存的是对应的Factory而不是具体需要实例化的类,比如MainPresenter
     * 存的是MainPresenter_Factory而不是MainPresenter
     * 每次需要对象的时候会调用Factory的get方法进行实例化对象
     */
    private final Map<Class<? extends ViewModel>, Provider<ViewModel>> creators;

    @Inject
    public AnkoViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> creators) {
        this.creators = creators;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Provider<? extends ViewModel> creator = creators.get(modelClass);
        if (creator == null) {
            for (Map.Entry<Class<? extends ViewModel>, Provider<ViewModel>> entry : creators.entrySet()) {
                if (modelClass.isAssignableFrom(entry.getKey())) {
                    //viewModel = mFactory.create(modelClass);
                    creator = entry.getValue();
                }
            }
        }

        if (creator == null) {
            try {
                return modelClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Unknown model class " + modelClass);
            }
        }

        try {
            return (T) creator.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
