package net.android.anko.utils.extensions

import android.app.Dialog
import android.content.Context
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.click(body: (v: View) -> Unit) {
    setOnClickListener {
        body.invoke(it)
    }
}

fun Context.inflate(res: Int, parent: ViewGroup? = null): View {
    return LayoutInflater.from(this).inflate(res, parent, false)
}

inline fun Dialog.ifIsShowing(body: Dialog.() -> Unit) {
    if (isShowing) {
        body()
    }
}

inline fun Snackbar.ifIsShowing(body: Snackbar.() -> Unit) {
    if (isShown) {
        body()
    }
}


operator fun ViewGroup.get(position: Int): View? = getChildAt(position)
