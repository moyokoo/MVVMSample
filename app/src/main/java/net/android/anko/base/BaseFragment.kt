package net.android.anko.base


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v7.view.ContextThemeWrapper
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import dagger.android.support.DaggerFragment
import me.yokeyword.fragmentation.SupportFragment
import miaoyongjun.autil.utils.LogUtils
import net.android.anko.R
import net.android.anko.helper.AppHelper.getFragmentByTag
import net.android.anko.helper.NetHelper
import net.android.anko.provider.RestProvider
import net.android.anko.utils.extensions.getFragmentByTag
import net.android.anko.utils.extensions.gone
import net.android.anko.widgets.AnkoEmptyLayout
import javax.inject.Inject


abstract class BaseFragment<VM : BaseViewModel> : SupportFragment(), IView {

    lateinit var viewModel: VM

    lateinit var viewModelFactory: ViewModelProvider.Factory

    var callBack: IView? = null

    var emptyLayout: AnkoEmptyLayout? = null

    var toolbar: Toolbar? = null
    var appbar: AppBarLayout? = null

    protected abstract fun providePresenterClass(): Class<VM>

    protected abstract fun provideLayout(): Int

    protected abstract fun onFragmentCreated(view: View, savedInstanceState: Bundle?)

    open fun canNewInstance() = false

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is IView) {
            callBack = context
        }
        if (context is BaseActivity<*>) {
            viewModelFactory = context.viewModelFactory
        }
    }

    override fun onDetach() {
        super.onDetach()
        callBack = null
    }

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (provideLayout() != 0) {
            val contextThemeWrapper = ContextThemeWrapper(context, context?.theme)
            val themeAwareInflater = inflater.cloneInContext(contextThemeWrapper)
            val view = themeAwareInflater.inflate(provideLayout(), container, false)

            toolbar = view.findViewById(R.id.toolbar)
            appbar = view.findViewById(R.id.appbar)
            emptyLayout = view.findViewById(R.id.emptyLayout)

            viewModel = if (canNewInstance()) {
                //以Fragment为单位保存, Fragment销毁时,ViewModel对象也会销毁
                ViewModelProviders.of(this, viewModelFactory).get(providePresenterClass())
            } else {
                //以Activity为单位保存, Fragment销毁时,ViewModel对象不会销毁
                ViewModelProviders.of(activity!!, viewModelFactory).get(providePresenterClass())
            }
            viewModel.errorLiveData.observe(this, Observer {
                val code = RestProvider.getErrorCode(it)
                when (code) {
                    401 -> onRequireLogin()
                    404 -> LogUtils.e("github unWatch unStar")
                    else -> {
                        emptyLayout?.showNetError()
                        showMessage(R.string.error, NetHelper.getPrettifiedErrorMessage(it))
                    }
                }
            })
            (activity as BaseActivity<*>).setupToolbarAndStatusBar(toolbar)
            return view
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onFragmentCreated(view, savedInstanceState)
    }

    override fun onScrollTop() {
    }

    override fun showMessage(msgRes: Any?) {
        callBack?.showMessage(msgRes)
    }

    override fun showMessage(titleRes: Int, msgRes: Int) {
        callBack?.showMessage(titleRes, msgRes)
    }

    override fun showMessage(titleRes: String, msgRes: String) {
        callBack?.showMessage(titleRes, msgRes)
    }

    override fun showErrorMessage(msgRes: Any?) {
        callBack?.showMessage(msgRes)
    }

    override fun isLoggedIn(): Boolean {
        return callBack?.isLoggedIn()!!
    }

    override fun hideProgress() {
        val fragment: Fragment? = getFragmentByTag(ProgressDialogFragment.TAG)
        if (fragment != null) {
            (fragment as ProgressDialogFragment).dismissAllowingStateLoss()
        }
    }

    override fun showProgress(layout: Int) {
        emptyLayout?.gone()
        if (!isDetached) {
            var fragment: Fragment? = getFragmentByTag(ProgressDialogFragment.TAG)
            if (fragment == null) {
                fragment = ProgressDialogFragment.newInstance()
                if (layout != 0) {
                    childFragmentManager.beginTransaction()
                            .add(layout, fragment, ProgressDialogFragment.TAG).commitAllowingStateLoss()
                } else {
                    childFragmentManager.beginTransaction()
                            .add(fragment, ProgressDialogFragment.TAG).commitAllowingStateLoss()
                }
            }
        }
    }


    override fun onRequireLogin() {
        callBack?.onRequireLogin()
    }
}
