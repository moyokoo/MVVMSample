package net.android.anko.ui.main

import android.arch.lifecycle.Observer
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import com.jaeger.library.StatusBarUtil
import com.yanzhenjie.sofia.Sofia
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_bottom_navigation.*
import kotlinx.android.synthetic.main.youtu_dispatch.*
import me.yokeyword.fragmentation.SupportFragment
import net.android.anko.R
import net.android.anko.base.BaseActivity
import net.android.anko.helper.ThemeHelper
import net.android.anko.ui.gank.GankRootFragment
import net.android.anko.ui.girl.GirlFragment
import net.android.anko.ui.video.GankVideoFragment
import net.android.anko.utils.extensions.*
import net.android.anko.widgets.youtube.*
import org.salient.artplayer.MediaPlayerManager
import org.salient.artplayer.ScaleType
import org.salient.artplayer.ijk.IjkPlayer
import skin.support.content.res.SkinCompatUserThemeManager
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewModel>(), DouyinDraggingView.Callback {


    @Inject
    lateinit var gankFragment: GankRootFragment
    @Inject
    lateinit var gankVideoFragment: GankVideoFragment
    @Inject
    lateinit var girlFragment: GirlFragment

    val mFragments = arrayOfNulls<SupportFragment>(4)
    var controlPanel: DouyinControlPanel? = null

    override fun canBack(): Boolean = false

    override fun provideLayout(): Int = R.layout.activity_main

    override fun providePresenterClass(): Class<MainViewModel> = MainViewModel::class.java


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Sofia.with(this).invasionStatusBar().statusBarBackgroundAlpha(0)
        bottomNavigationView?.setOnNavigationItemSelectedListener { onNavigation(it) }

        initFragments()
        viewModel.fetchUserData()
        viewModel.fetchUser.observe(this, Observer {

        })

        youTuDraggingView.setCallback(this)
        controlPanel = DouyinControlPanel(this)
        controlPanel?.setYouTuDraggingView(youTuDraggingView)
        videoView.controlPanel = controlPanel
        controlPanel?.downIv?.click { youTuDraggingView.fullScreenGoMin() }
        MediaPlayerManager.instance().releasePlayerAndView(this)
        MediaPlayerManager.instance().mediaPlayer = IjkPlayer()
    }


    fun playVideo(url: String) {
        youTuDraggingView.show()
        videoView.setUp(url)
        videoView.start()
        MediaPlayerManager.instance().setScreenScale(ScaleType.SCALE_CENTER_CROP)
    }

    override fun onVideoViewHide() {

    }

    override fun videoSize(width: Int, height: Int) {
    }

    override fun onIconClick(iconType: IconType?) {
        if (iconType === IconType.CLOSE) {
            if (videoView.isCurrentPlaying) {
                videoView.pause()
            }
        } else if (iconType === IconType.PAUSE) {
            if (videoView.isCurrentPlaying)
                videoView.pause()
        } else if (iconType === IconType.PLAY) {
            if (videoView.isCurrentPlaying && MediaPlayerManager.instance().isPlaying)
                return
            videoView.start()
        }
    }

    override fun status(status: Int) {
        controlPanel?.showOrHide(status)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return youTuDraggingView.handleKeyDown(keyCode) || super.onKeyDown(keyCode, event)
    }

    override fun onBackPressedSupport() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            pop()
        } else {
            ActivityCompat.finishAfterTransition(this)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.red) {
            SkinCompatUserThemeManager.get().clearColors()
            SkinCompatUserThemeManager.get().clearDrawables()
            return true
        } else if (id == R.id.blue) {
            ThemeHelper.setDarkTheme(this)
            return true
        } else if (id == R.id.green) {
//            ThemeHelper.setDarkTheme(this)
//            start(LoginActivity::class.java)
            playVideo("https://www.iesdouyin.com/share/video/6568417257347616007/?region=CN&mid=6568419006418848520&titleType=title&utm_source=copy_link&utm_campaign=client_share&utm_medium=android&app=aweme&iid=36442509245&timestamp=1530068063")
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}
