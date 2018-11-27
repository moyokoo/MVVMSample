package net.android.anko.base

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasFragmentInjector
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.HasSupportFragmentInjector
import es.dmoral.toasty.Toasty
import me.yokeyword.fragmentation.SupportActivity
import miaoyongjun.autil.utils.LogUtils
import net.android.anko.AnkkoApp
import net.android.anko.R
import net.android.anko.helper.AppHelper
import net.android.anko.helper.NetHelper
import net.android.anko.provider.RestProvider
import net.android.anko.ui.login.LoginActivity
import net.android.anko.ui.main.MainActivity
import net.android.anko.utils.extensions.getFragmentByTag
import net.android.anko.utils.extensions.gone
import net.android.anko.utils.extensions.start
import net.android.anko.widgets.AnkoEmptyLayout
import java.util.*
import javax.inject.Inject


/**
 * Created by miaoyongjun
 */

abstract class BaseActivity<VM : BaseViewModel> : SupportActivity(), IView, HasFragmentInjector, HasSupportFragmentInjector {
    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var frameworkFragmentInjector: DispatchingAndroidInjector<android.app.Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector

    override fun fragmentInjector(): AndroidInjector<android.app.Fragment> = frameworkFragmentInjector

    val toolbar: Toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    val appbar: AppBarLayout  by lazy { findViewById<AppBarLayout>(R.id.appbar) }

    var emptyLayout: AnkoEmptyLayout? = null

    //通过ViewModelModule进行管理注入
    lateinit var viewModel: VM

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected abstract fun provideLayout(): Int

    protected abstract fun providePresenterClass(): Class<VM>

    private var toast: Toast? = null


    protected open fun canBack(): Boolean {
        return true
    }

    open fun showToolbarTitle(): Boolean {
        return true
    }

    protected open fun retryNet() {

    }

    protected open fun retryNotify() {

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        AppHelper.updateAppLanguage(this)
//        if (PrefGetter.isWhiteTheme()) {
//            Sofia.with(this).statusBarDarkFont().fitsStatusBarView(StatusView(this))
//        }
        super.onCreate(savedInstanceState)

        if (provideLayout() != 0) {
            setContentView(provideLayout())
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(providePresenterClass())
        emptyLayout = findViewById(R.id.emptyLayout)
        emptyLayout?.error(object : AnkoEmptyLayout.DefaultErrorObserver {
            override fun onErrorNetRetry() {
                emptyLayout?.gone()
                retryNet()
            }

            override fun onNotifyRetry() {
                retryNotify()
            }
        })
        showChangelog()
        setupToolbarAndStatusBar(toolbar)
        initObserver()
    }


    private fun initObserver() {
        viewModel.errorLiveData.observe(this, android.arch.lifecycle.Observer {
            hideProgress()
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

        viewModel.showErrorMessage.observe(this, android.arch.lifecycle.Observer {
            showErrorMessage(it)
        })

        viewModel.showMessage.observe(this, android.arch.lifecycle.Observer {
            showMessage(it)
        })

        viewModel.progressStatus.observe(this, android.arch.lifecycle.Observer {
            when (it) {
                ProStatus.SHOW -> showProgress()
                ProStatus.HIDE -> hideProgress()
                ProStatus.NOTHING -> Unit
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (canBack()) {
            if (item.itemId == android.R.id.home) {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onScrollTop() {

    }

    override fun showMessage(msgRes: Any?) {
        if (msgRes is Int) {
            showMessage("", getString(msgRes))
        } else if (msgRes is String) {
            showMessage("", msgRes)
        }
    }

    override fun showMessage(@StringRes titleRes: Int, @StringRes msgRes: Int) {
        showMessage(getString(titleRes), getString(msgRes))
    }

    override fun showMessage(titleRes: String, msgRes: String) {
        hideProgress()
        if (toast != null) toast?.cancel()
        val context = AnkkoApp.getInstance() // WindowManager$BadTokenException
        toast = if (titleRes == context.getString(R.string.error))
            Toasty.error(context, msgRes, Toast.LENGTH_LONG)
        else
            Toasty.info(context, msgRes, Toast.LENGTH_LONG)
        toast?.show()
    }

    override fun showErrorMessage(msgRes: Any?) {
        if (msgRes is Int) {
            showMessage(getString(R.string.error), getString(msgRes))
        } else if (msgRes is String) {
            showMessage(getString(R.string.error), msgRes)
        }
    }

    override fun isLoggedIn(): Boolean {
        return false
    }

    override fun hideProgress() {
        val fragment: Fragment? = getFragmentByTag(ProgressDialogFragment.TAG)
        if (fragment != null) {
            (fragment as ProgressDialogFragment).dismissAllowingStateLoss()
        }

    }

    override fun showProgress(layout: Int) {

        if (!isFinishing) {
            var fragment: Fragment? = getFragmentByTag(ProgressDialogFragment.TAG)
            if (fragment == null) {
                fragment = ProgressDialogFragment.newInstance()
                if (layout != 0) {
                    supportFragmentManager.beginTransaction()
                            .add(layout, fragment, ProgressDialogFragment.TAG).commitAllowingStateLoss()
                } else {
                    supportFragmentManager.beginTransaction()
                            .add(fragment, ProgressDialogFragment.TAG).commitAllowingStateLoss()
                }
            }
        }
    }

    override fun onRequireLogin() {
//        Toasty.warning(AnkkoApp.getInstance(), getString(R.string.unauthorized_user), Toast.LENGTH_LONG).show()
//        start(LoginActivity::class.java)
    }


    fun setupToolbarAndStatusBar(toolbar: Toolbar?) {
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            if (canBack()) {
                if (supportActionBar != null) {
                    supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)
                    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                    supportActionBar!!.setDisplayShowTitleEnabled(showToolbarTitle())
                    if (canBack()) {
                        val navIcon = getToolbarNavigationIcon(toolbar)
                        navIcon?.setOnLongClickListener { v ->
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                            true
                        }
                    }
                }
            }
        }
    }

    protected fun setToolbarIcon(@DrawableRes res: Int) {
        if (supportActionBar != null) {
            supportActionBar!!.setHomeAsUpIndicator(res)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
    }

    protected fun hideShowShadow(show: Boolean) {
        appbar.elevation = if (show) resources.getDimension(R.dimen.spacing_micro) else 0.0f
    }

    protected fun getToolbarNavigationIcon(toolbar: Toolbar): View? {
        val hadContentDescription = TextUtils.isEmpty(toolbar.navigationContentDescription)
        val contentDescription = if (!hadContentDescription) toolbar.navigationContentDescription.toString() else "navigationIcon"
        toolbar.navigationContentDescription = contentDescription
        val potentialViews = ArrayList<View>()
        toolbar.findViewsWithText(potentialViews, contentDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION)
        var navIcon: View? = null
        if (potentialViews.size > 0) {
            navIcon = potentialViews[0]
        }
        if (hadContentDescription) toolbar.navigationContentDescription = null
        return navIcon
    }

    fun onRestartApp() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finishAndRemoveTask()
    }


    private fun showChangelog() {
        //        if (PrefGetter.showWhatsNew() && !(this instanceof PlayStoreWarningActivity)) {
        //            new ChangelogBottomSheetDialog().show(getSupportFragmentManager(), "ChangelogBottomSheetDialog");
        //        }
    }

    private fun showInAppNotifications(): Boolean {
        //        return FastHubNotification.hasNotifications();
        return false
    }

}
