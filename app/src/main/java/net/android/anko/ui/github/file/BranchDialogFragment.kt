package net.android.anko.ui.github.file

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_branches_dialog.*
import net.android.anko.R
import net.android.anko.base.BaseDialogFragment
import net.android.anko.helper.Bundler
import net.android.anko.ui.github.RepositotyViewModel

class BranchDialogFragment : BaseDialogFragment(), BranchChooseFragment.OnBranchSelect {

    override fun call(name: String) {
        repositotyViewModel.chooseBranch.value = name
        dismiss()
    }

    lateinit var repositotyViewModel: RepositotyViewModel

    companion object {
        const val OWNER = "owner"
        const val LOGIN = "login"
        fun newInstance(owner: String?, login: String?): BranchDialogFragment {
            val fragment = BranchDialogFragment()
            fragment.arguments = Bundler.start()
                    .put(OWNER, owner)
                    .put(LOGIN, login)
                    .end()
            return fragment
        }
    }


    override fun provideLayout(): Int {
        return R.layout.fragment_branches_dialog
    }

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.setNavigationOnClickListener {
            dismiss()
        }
        childFragmentManager.beginTransaction()
                .add(R.id.container, BranchChooseFragment.newInstance(arguments?.getString(OWNER), arguments?.getString(LOGIN)))
                .commit()
        repositotyViewModel = ViewModelProviders.of(activity!!).get(RepositotyViewModel::class.java)
    }

}