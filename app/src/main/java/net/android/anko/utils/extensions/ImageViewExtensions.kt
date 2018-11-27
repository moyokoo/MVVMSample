package net.android.anko.utils.extensions

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import net.android.anko.GlideApp
import net.android.anko.R
import net.android.anko.utils.GlideRoundTransform


fun ImageView.loadImageCrop(context: Context, url: String) {
    GlideApp.with(context)
            .load(url)
            .transforms(CenterCrop(), GlideRoundTransform())
            .into(this)
}

fun ImageView.loadProfileCircle(context: Context, url: String?) {
    GlideApp.with(context)
            .load(url)
            .centerCrop().circleCrop()
            .placeholder(R.drawable.ic_profile)
            .into(this)
}

fun ImageView.loadImageCircle(context: Context, url: String?) {
    GlideApp.with(context)
            .load(url)
            .centerCrop().circleCrop()
            .into(this)
}

fun ImageView.loadImageCrop(context: Context, url: String, roundingRadius: Int) {
    if (url == "") {
        return
    }
    GlideApp.with(context)
            .load(url)
            .transforms(CenterCrop(), GlideRoundTransform(roundingRadius))
            .into(this)
}

fun ImageView.loadImage(context: Context?, url: String?) {
    if (context == null) {
        return
    }
    GlideApp.with(context)
            .load(url)
            .thumbnail(0.1f)
            .dontAnimate()
            .into(this)
}

fun ImageView.loadImageRes(context: Context, url: Int) {
    GlideApp.with(getContext())
            .load(url)
            .into(this)
}

fun ImageView.loadVector(url: Int) {
    setImageDrawable(resources.getDrawable(url))
}

fun ImageView.loadImageSimple(context: Context, url: String) {
    GlideApp.with(context)
            .load(url)
            .error(R.drawable.ic_profile)
            .dontAnimate()
            .into(this)
}

fun ImageView.loadImage(context: Context, url: String, errorMipmap: Int) {
    GlideApp.with(context)
            .load(url)
            .error(errorMipmap)
            .dontAnimate()
            .into(this)
}


