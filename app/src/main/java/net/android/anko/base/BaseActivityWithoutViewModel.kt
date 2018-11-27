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
import dagger.android.support.DaggerAppCompatActivity
import es.dmoral.toasty.Toasty
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

abstract class BaseActivityWithoutViewModel : BaseActivity<BaseViewModel>() {
    override fun providePresenterClass(): Class<BaseViewModel> {
        return BaseViewModel::class.java
    }


}
