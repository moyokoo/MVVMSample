package net.android.anko.base.di;

import net.android.anko.ui.common.ViewerActivity;
import net.android.anko.ui.common.ViewerModule;
import net.android.anko.ui.github.RepositoryActivity;
import net.android.anko.ui.github.RepositoryModule;
import net.android.anko.ui.github.user.UserRepositoryActivity;
import net.android.anko.ui.github.user.UserRepositoryModule;
import net.android.anko.ui.login.OauthActivity;
import net.android.anko.ui.login.OauthModule;
import net.android.anko.ui.main.MainActivity;
import net.android.anko.ui.main.MainModule;
import net.android.anko.ui.login.LoginActivity;
import net.android.anko.ui.login.LoginModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @ContributesAndroidInjector 生成返回类型的Module以及SubComponent
 * @Module(subcomponents = ActivityBindingModule_TasksActivity.TasksActivitySubcomponent.class)
 * public abstract class ActivityBindingModule_TasksActivity {
 * private ActivityBindingModule_TasksActivity() {}
 * @Binds
 * @IntoMap
 * @ActivityKey(TasksActivity.class) abstract AndroidInjector.Factory<? extends Activity> bindAndroidInjectorFactory(
 * TasksActivitySubcomponent.Builder builder);
 * @Subcomponent(modules = TasksModule.class)
 * @ActivityScoped public interface TasksActivitySubcomponent extends AndroidInjector<TasksActivity> {
 * @Subcomponent.Builder abstract class Builder extends AndroidInjector.Builder<TasksActivity> {}
 * }
 * }
 */
@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = {UserRepositoryModule.class})
    abstract UserRepositoryActivity userRepositoryActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {ViewerModule.class})
    abstract ViewerActivity viewerActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {MainModule.class})
    abstract MainActivity mainActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = LoginModule.class)
    abstract LoginActivity loginActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = OauthModule.class)
    abstract OauthActivity oauthActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = RepositoryModule.class)
    abstract RepositoryActivity repositoryActivity();

}
