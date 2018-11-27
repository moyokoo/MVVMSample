package net.android.anko.base

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Dialog
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatDialogFragment
import android.support.v7.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.android.anko.R
import net.android.anko.helper.AnimHelper
import net.android.anko.helper.AppHelper
import net.android.anko.helper.PrefGetter

/**
 * Created by Kosh on 22 Feb 2017, 7:28 PM
 */

abstract class BaseDialogFragment : AppCompatDialogFragment() {

    protected abstract fun provideLayout(): Int

    protected abstract fun onFragmentCreated(view: View, savedInstanceState: Bundle?)

    internal var suppressAnimation = false
    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (provideLayout() != 0) {
            val contextThemeWrapper = ContextThemeWrapper(context, context!!.theme)
            val themeAwareInflater = inflater.cloneInContext(contextThemeWrapper)
            val view = themeAwareInflater.inflate(provideLayout(), container, false)
            return view
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onFragmentCreated(view, savedInstanceState)
    }


    override fun dismiss() {
        if (suppressAnimation) {
            super.dismiss()
            return
        }
        AnimHelper.dismissDialog(this, resources.getInteger(android.R.integer.config_shortAnimTime),
                object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        super@BaseDialogFragment.dismiss()
                    }
                })

    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (!PrefGetter.isAppAnimationDisabled() && this !is ProgressDialogFragment && !suppressAnimation) {
            dialog.setOnShowListener {
                AnimHelper.revealDialog(dialog,
                        resources.getInteger(android.R.integer.config_longAnimTime))
            }
        }
        return dialog
    }


}

