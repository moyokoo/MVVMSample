package net.android.anko.ui.github.file

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.fragment_branches.*
import net.android.anko.R
import net.android.anko.R.id.recyclerView
import net.android.anko.base.BaseFragment
import net.android.anko.helper.Bundler
import net.android.anko.model.model.BranchModel
import net.android.anko.ui.github.RepositotyViewModel
import net.android.anko.ui.github.file.BranchDialogFragment.Companion.LOGIN
import net.android.anko.ui.github.file.BranchDialogFragment.Companion.OWNER
import javax.inject.Inject

class BranchChooseFragment @Inject constructor() : BaseFragment<BranchChooseViewModel>() {
    lateinit var repositotyViewModel: RepositotyViewModel

    companion object {
        fun newInstance(owner: String?, login: String?): BranchChooseFragment {
            val fragment = BranchChooseFragment()
            fragment.arguments = Bundler.start()
                    .put(OWNER, owner)
                    .put(LOGIN, login)
                    .end()
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        onBranchSelect = if (parentFragment is OnBranchSelect) {
            parentFragment as OnBranchSelect
        } else context as OnBranchSelect
    }

    interface OnBranchSelect {
        fun call(name: String)
    }

    var onBranchSelect: OnBranchSelect? = null


    override fun onDestroyView() {
        onBranchSelect = null
        super.onDestroyView()
    }

    lateinit var adapter: BranchAdapter
    var list: List<BranchModel> = mutableListOf()
    override fun providePresenterClass(): Class<BranchChooseViewModel> {
        return BranchChooseViewModel::class.java
    }

    override fun provideLayout(): Int {
        return R.layout.fragment_branches
    }

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
        adapter = BranchAdapter(list)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)
        viewModel.getBranches(arguments?.getString(OWNER), arguments?.getString(LOGIN))
        viewModel.branchList.observe(this, Observer {
            adapter.setNewData(it)
        })
        adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
            onBranchSelect?.call(adapter.data[position].name)
        }

    }

}