package net.android.anko.ui.gank

import android.os.Bundle
import android.view.View
import net.android.anko.R
import net.android.anko.base.BaseFragment
import net.android.anko.base.BaseViewModel
import net.android.anko.helper.PrefGetter
import net.android.anko.ui.main.MainActivity
import net.android.anko.utils.extensions.changeGankToolbar
import javax.inject.Inject

class GankRootFragment @Inject constructor() : BaseFragment<BaseViewModel>() {
    override fun providePresenterClass(): Class<BaseViewModel> {
        return BaseViewModel::class.java
    }

    @Inject
    lateinit var gankFragment: GankFragment

    override fun provideLayout() = R.layout.fragment_empty

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
        if (findChildFragment(GankFragment::class.java) == null) {
            gankFragment.addBundle(PrefGetter.gankTypeName())
            loadRootFragment(R.id.container, gankFragment)
        }
    }

    override fun onBackPressedSupport(): Boolean {
        if (childFragmentManager.backStackEntryCount > 1) {
            popChild()
            if (childFragmentManager.backStackEntryCount == 2) {
                (activity as MainActivity).changeGankToolbar(false)
            }
        } else {
            _mActivity.finish()
        }
        return true
    }
}