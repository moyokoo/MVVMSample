package net.android.anko.ui.login

import android.arch.lifecycle.Observer
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import com.just.agentweb.AgentWeb
import kotlinx.android.synthetic.main.activity_main.*
import net.android.anko.R
import net.android.anko.base.BaseActivity
import net.android.anko.helper.GithubConfigHelper

class OauthActivity : BaseActivity<OauthViewModel>() {

    var mAgentWeb: AgentWeb? = null
    val OAUTH2_SCOPE = "user,repo,gist,notifications"

    private val mWebViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if (url?.startsWith(GithubConfigHelper.getRedirectUrl())!!) {
                view?.stopLoading()
                viewModel.handleRedirectedUrl(url)
            } else {
                view?.loadUrl(url)
            }
            return true
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            if (url != "about:blank" && url?.startsWith(GithubConfigHelper.getRedirectUrl())!!) {
                view?.stopLoading()
                viewModel.handleRedirectedUrl(url)
                return
            }
            super.onPageStarted(view, url, favicon)
        }
    }

    override fun provideLayout(): Int {
        return R.layout.activity_oauth
    }

    override fun providePresenterClass(): Class<OauthViewModel> {
        return OauthViewModel::class.java
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title  = "登录"
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(container, FrameLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setWebViewClient(mWebViewClient)
                .createAgentWeb()
                .ready()
                .go("")
        mAgentWeb?.webCreator?.webView?.loadUrl("https://github.com/login/oauth/authorize" + "?" + "client_id=" + GithubConfigHelper.getClientId()
                + "&scope=" + OAUTH2_SCOPE + "&state=" + viewModel.randomState.value)

        viewModel.fetchAccessTokenSuccess.observe(this, Observer {
            it?.let {
                if (it) {
                    setResult(1)
                    finish()
                }
            }
        })
    }
}
