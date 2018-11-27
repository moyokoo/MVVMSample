package net.android.anko.utils.extensions

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import com.jaeger.library.StatusBarUtil
import com.yanzhenjie.sofia.Sofia
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_bottom_navigation.*
import net.android.anko.R
import net.android.anko.R.id.*
import net.android.anko.helper.ThemeHelper
import net.android.anko.ui.gank.GankFragment
import net.android.anko.ui.gank.GankRootFragment
import net.android.anko.ui.girl.GirlFragment
import net.android.anko.ui.main.MainActivity
import net.android.anko.ui.video.GankVideoFragment
import skin.support.content.res.SkinCompatUserThemeManager
import android.content.res.ColorStateList


fun MainActivity.changeGankToolbar(isBack: Boolean = false, title: String = resources.getString(R.string.app_name)) {
    if (supportActionBar != null) {
        supportActionBar!!.setHomeAsUpIndicator(if (isBack) R.drawable.ic_back else R.mipmap.ic_launcher)
        supportActionBar!!.title = title
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(showToolbarTitle())
    }
}

fun getBottomStates(): Array<IntArray> {
    return arrayOf(intArrayOf(android.R.attr.state_pressed),
            intArrayOf(-android.R.attr.state_selected),
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf()
    )
}

fun MainActivity.clickGirlPostion() {
    bottomNavigationView.setBackgroundColor(Color.TRANSPARENT)
    val states = getBottomStates()
    val whiteColor = ContextCompat.getColor(this, R.color.md_white_1000)
    val colors = intArrayOf(whiteColor, whiteColor, whiteColor, whiteColor)
    bottomNavigationView.itemTextColor = ColorStateList(states, colors)
    bottomNavigationView.itemIconTintList = ColorStateList(states, colors)
}

fun MainActivity.unClickGirlPostion() {
    bottomNavigationView.setBackgroundColor(ContextCompat.getColor(this, R.color.navigation_color))
    val states = getBottomStates()
    val colors = intArrayOf(
            ContextCompat.getColor(this, R.color.colorAccent),
            ContextCompat.getColor(this, R.color.colorAccent),
            ContextCompat.getColor(this, R.color.colorAccent),
            ContextCompat.getColor(this, R.color.textColorTertiary))
    bottomNavigationView.itemTextColor = ColorStateList(states, colors)
    bottomNavigationView.itemIconTintList = ColorStateList(states, colors)
}

fun MainActivity.onNavigation(item: MenuItem): Boolean {
    var clickPosition = 0
    when (item.itemId) {
        R.id.gank -> clickPosition = 0
        R.id.video -> clickPosition = 1
        R.id.girl -> clickPosition = 2
    }

    if (clickPosition == viewModel.currentFragmentPosition) {
        val reSelectFragment = mFragments[clickPosition]
        val count = reSelectFragment?.childFragmentManager?.backStackEntryCount
        if (count!! > 1) {
            when (reSelectFragment) {
                is GankRootFragment -> {
                    reSelectFragment.popToChild(GankFragment::class.java, false)
                    changeGankToolbar(false)
                }
            }
        }
        if (count == 1) {
            if (reSelectFragment.topChildFragment is GankFragment) {
                (reSelectFragment.topChildFragment as GankFragment).onScrollTop()
            }
        }

    } else {
        if (clickPosition == 2) {
            clickGirlPostion()
        } else {
            unClickGirlPostion()
        }
        showHideFragment(mFragments[clickPosition], mFragments[viewModel.currentFragmentPosition])
        viewModel.currentFragmentPosition = clickPosition
    }
    return true
}

fun MainActivity.initFragments() {
    val firstFragment = findFragment(GankRootFragment::class.java)
    if (firstFragment == null) {
        mFragments[0] = gankFragment
        mFragments[1] = gankVideoFragment
        mFragments[2] = girlFragment

        loadMultipleRootFragment(R.id.container, 0,
                mFragments[0],
                mFragments[1],
                mFragments[2])
    } else {
        mFragments[0] = firstFragment
        mFragments[1] = findFragment(GankVideoFragment::class.java)
        mFragments[2] = findFragment(GirlFragment::class.java)
    }
}