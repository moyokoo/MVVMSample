package net.android.anko.ui.main

import android.arch.lifecycle.MutableLiveData
import net.android.anko.base.BaseViewModel
import net.android.anko.helper.PrefGetter
import net.android.anko.model.ApiGithubRepository
import net.android.anko.model.model.UserGithubModel
import net.android.anko.utils.SingleLiveEvent
import javax.inject.Inject


/**
 * 如果需要在构造函数里使用变量  使用参数的方式进行注入
 * MainViewModel instance =
new MainViewModel(feedDataFactoryProvider.get(), pagedListConfigProvider.get());
BasePresenter_MembersInjector.injectRepository(instance, repositoryProvider.get());
首先实例化  其次进行变量注入
只有对应类的Factory的create被调用,才会进行注入,即这个类需要在其他地方被引用
 */
class MainViewModel
@Inject
internal constructor(val repositoryApiGithub: ApiGithubRepository) : BaseViewModel() {

    var fetchUser = SingleLiveEvent<UserGithubModel>()
    var currentFragmentPosition = 0

    fun fetchUserData() {
        makeRestCall(
                repositoryApiGithub.user(),
                next = {
                    PrefGetter.setUserGithub(it)
                    fetchUser.value = it
                }
        )
    }


}
