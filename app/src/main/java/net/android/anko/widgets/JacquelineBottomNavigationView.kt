package net.android.anko.widgets

import android.content.Context
import android.util.AttributeSet

import skin.support.design.widget.SkinMaterialBottomNavigationView
import skin.support.widget.SkinCompatBackgroundHelper


class JacquelineBottomNavigationView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : SkinMaterialBottomNavigationView(context, attrs, defStyleAttr) {

    private val mBackgroundHelper: SkinCompatBackgroundHelper?

    init {
        mBackgroundHelper = SkinCompatBackgroundHelper(this)
        mBackgroundHelper.loadFromAttributes(attrs, defStyleAttr)
    }

    override fun isInEditMode(): Boolean {
        return true
    }

    override fun applySkin() {
        super.applySkin()
        mBackgroundHelper?.applySkin()
    }

}
