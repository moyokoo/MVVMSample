package net.android.anko.ui.github.user

import android.os.Bundle
import android.view.View
import net.android.anko.R
import net.android.anko.base.BaseFragment
import javax.inject.Inject

class StarFragment @Inject constructor(): BaseFragment<StarViewModel>() {
    override fun providePresenterClass(): Class<StarViewModel> {
        return StarViewModel::class.java
    }

    override fun provideLayout(): Int {
        return R.layout.fragment_star
    }

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {

    }

}