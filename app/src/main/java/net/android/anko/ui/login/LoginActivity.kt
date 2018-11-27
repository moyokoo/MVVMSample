package net.android.anko.ui.login

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import kotlinx.android.synthetic.main.activity_login_github.*
import net.android.anko.R
import net.android.anko.base.BaseActivity
import net.android.anko.base.ProStatus
import net.android.anko.helper.AnimHelper
import net.android.anko.helper.AppHelper
import net.android.anko.helper.PrefGetter
import org.jetbrains.anko.startActivityForResult

class LoginActivity : BaseActivity<LoginViewModel>() {

    val OAUTH_CODE = 1
    override fun provideLayout(): Int {
        return R.layout.activity_login_github
    }

    override fun providePresenterClass(): Class<LoginViewModel> {
        return LoginViewModel::class.java
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PrefGetter.cleanToken()
        PrefGetter.cleanUserGithub()
        login_bn.setOnClickListener {
            showLoginProgress()
            viewModel.login(user_name_et.text.toString(), password_et.text.toString())
        }

        oauth_login_bn.setOnClickListener {
            startActivityForResult(Intent(this@LoginActivity, OauthActivity::class.java), OAUTH_CODE)
        }

        viewModel.fetchAccessTokenSuccess.observe(this, Observer {
            it?.let {
                if (it) {
                    finish()
                }
            }
        })
        viewModel.showLoginProgress.observe(this, Observer {
            it?.let {
                when (it) {
                    ProStatus.SHOW -> {
                        showLoginProgress()
                    }
                    ProStatus.HIDE -> {
                        hideLoginProgress()
                    }
                    ProStatus.NOTHING -> Unit
                }
            }
        })
    }

    fun showLoginProgress() {
        login_bn.hide()
        AppHelper.hideKeyboard(login_bn)
        AnimHelper.animateVisibility(progress, true)
    }

    fun hideLoginProgress() {
        progress.visibility = View.GONE
        login_bn.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OAUTH_CODE && (!TextUtils.isEmpty(PrefGetter.getToken()))) {
            showLoginProgress()
            viewModel.fetchUser()
        }
    }
}
