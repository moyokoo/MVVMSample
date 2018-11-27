package net.android.anko.ui.github.user

import android.os.Bundle
import android.support.v4.app.Fragment
import com.jaeger.library.StatusBarUtil
import kotlinx.android.synthetic.main.activity_github_profile.*
import net.android.anko.R
import net.android.anko.base.BaseActivity
import net.android.anko.model.model.FragmentPagerModel
import net.android.anko.ui.github.FragmentViewPagerAdapter
import net.android.anko.utils.extensions.loadImage
import javax.inject.Inject

class UserRepositoryActivity : BaseActivity<UserRepositoryViewModel>() {
    var fragments = mutableListOf<Fragment>()
    lateinit var fragmentViewPagerAdapter: FragmentViewPagerAdapter
    var fragmentPagerModels = mutableListOf<FragmentPagerModel>()
    @Inject
    lateinit var starFragment: StarFragment

    override fun provideLayout(): Int {
        return R.layout.activity_github_profile
    }

    override fun providePresenterClass(): Class<UserRepositoryViewModel> {
        return UserRepositoryViewModel::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StatusBarUtil.setTransparent(this)
        toolbarLayout.title = "Peter\nQiu"
        val titles = resources.getStringArray(R.array.profile_names_github)
        fragments.clear()
        fragments.add(starFragment)
        fragments.add(StarFragment())
        for (x in 0 until titles.size) {
            val model = FragmentPagerModel(titles[x], fragments[x])
            fragmentPagerModels.add(model)
        }
        fragmentViewPagerAdapter = FragmentViewPagerAdapter(supportFragmentManager)
        fragmentViewPagerAdapter.pagerList = fragmentPagerModels
        tabLayout.setupWithViewPager(viewPager)
        viewPager.adapter = fragmentViewPagerAdapter
        blurringView.setBlurredView(profileIv)
        profileIv.loadImage(this,"http://bmob-cdn-982.b0.upaiyun.com/2017/02/24/98754a6a401d5c48806b2b3863e32bed.jpg")
    }
}