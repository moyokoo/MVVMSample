package net.android.anko.ui.common

import android.view.View

interface OnItemTouchListener {
    fun onItemClick(view: View, position: Int)
}