package net.android.anko.utils.extensions

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.app.*
import android.support.v4.content.ContextCompat
import android.support.v4.util.Pair
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import es.dmoral.toasty.Toasty
import net.android.anko.AnkkoApp
import net.android.anko.R
import net.android.anko.helper.ViewHelper
import java.util.*

fun FragmentActivity.screenWidth(): Int {
    val metrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metrics)
    return metrics.widthPixels
}

fun FragmentActivity.screenHeight(): Int {
    val metrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metrics)
    return metrics.heightPixels
}

fun FragmentActivity.color(resId: Int): Int {
    return ContextCompat.getColor(this, resId)
}

fun FragmentActivity.getActivity(): Activity? {
    return this
}

fun FragmentActivity.getFragmentByTag(tag: String): Fragment? {
    return supportFragmentManager.findFragmentByTag(tag)
}

fun Fragment.getFragmentByTag(tag: String): Fragment? {
    return childFragmentManager.findFragmentByTag(tag)
}

fun FragmentActivity.getVisibleFragment(): Fragment? {
    val fragments = supportFragmentManager.fragments
    if (fragments != null && !fragments.isEmpty()) {
        for (fragment in fragments) {
            if (fragment != null && fragment.isVisible) {
                return fragment
            }
        }
    }
    return null
}

fun FragmentActivity.addFragmentToActivity(
        fragment: Fragment, frameId: Int) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.add(frameId, fragment)
    transaction.commit()
}

fun FragmentActivity.addFragmentToActivity(
        fragment: Fragment, frameId: Int, tag: String) {
    val transaction = supportFragmentManager.beginTransaction()
    val tempFragment = supportFragmentManager.findFragmentByTag(tag)
    if (tempFragment == null) {
        transaction.add(frameId, fragment, tag)
        transaction.commit()
    }
}

/**
 * The `fragment` is added to the container view with id `frameId`. The operation is
 * performed by the `fragmentManager`.
 */
fun FragmentActivity.replaceFragmentInActivity(fragment: Fragment, frameId: Int) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(frameId, fragment)
    transaction.commit()
}

/**
 * The `fragment` is added to the container view with id `frameId`. The operation is
 * performed by the `fragmentManager`.
 */
fun FragmentActivity.replaceFragmentInActivity(fragmentManager: FragmentManager,
                                               fragment: Fragment, tag: String) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.add(fragment, tag)
    transaction.commit()
}


fun FragmentActivity.start(cl: Class<*>) {
    val intent = Intent(this, cl)
    this.startActivity(intent)
}

@SafeVarargs
fun FragmentActivity.start(cl: Class<*>, vararg sharedElements: Pair<View, String>) {
    val intent = Intent(this, cl)
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, *sharedElements)
    this.startActivity(intent, options.toBundle())
}

fun FragmentActivity.start(intent: Intent, sharedElement: View) {
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
            sharedElement, ViewHelper.getTransitionName(sharedElement)!!)
    this.startActivity(intent, options.toBundle())
}

fun startReveal(activity: Activity, intent: Intent, sharedElement: View, requestCode: Int) {

    val options = ActivityOptionsCompat.makeClipRevealAnimation(sharedElement, sharedElement.width / 2,
            sharedElement.height / 2,
            sharedElement.width, sharedElement.height)
    activity.startActivityForResult(intent, requestCode, options.toBundle())

}

fun startReveal(fragment: Fragment, intent: Intent, sharedElement: View, requestCode: Int) {

    val options = ActivityOptionsCompat.makeClipRevealAnimation(sharedElement, sharedElement.width / 2,
            sharedElement.height / 2,
            sharedElement.width, sharedElement.height)
    fragment.startActivityForResult(intent, requestCode, options.toBundle())

}

fun startReveal(activity: Activity, intent: Intent, sharedElement: View) {

    val options = ActivityOptionsCompat.makeClipRevealAnimation(sharedElement, sharedElement.width / 2,
            sharedElement.height / 2,
            sharedElement.width, sharedElement.height)
    activity.startActivity(intent, options.toBundle())

}

@SafeVarargs
fun FragmentActivity.start(intent: Intent,
                           vararg sharedElements: Pair<View, String>) {
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, *sharedElements)
    this.startActivity(intent, options.toBundle())

}

fun FragmentActivity.shareUrl(url: String) {
    val activity = this
    try {
        ShareCompat.IntentBuilder.from(activity)
                .setChooserTitle(this.getString(R.string.share))
                .setType("text/plain")
                .setText(url)
                .startChooser()
    } catch (e: ActivityNotFoundException) {
        Toasty.error(AnkkoApp.getInstance(), e.message!!, Toast.LENGTH_LONG).show()
    }

}


fun isPermissionGranted(context: Context, permission: String): Boolean {
    return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}

private fun isExplanationNeeded(context: Activity, permissionName: String): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(context, permissionName)
}

private fun isReadWritePermissionIsGranted(context: Context): Boolean {
    return isPermissionGranted(context, Manifest.permission.READ_EXTERNAL_STORAGE) && isPermissionGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
}

private fun requestReadWritePermission(context: Activity) {
    ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
}

fun checkAndRequestReadWritePermission(activity: Activity): Boolean {
    //        if (!isReadWritePermissionIsGranted(activity)) {
    //            requestReadWritePermission(activity);
    //            return false;
    //        } else if (isExplanationNeeded(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
    //                || isExplanationNeeded(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
    //            Toasty.error(App.getInstance(), activity.getString(R.string.read_write_permission_explanation), Toast.LENGTH_LONG).show();
    //            return false;
    //        }
    return true
}

private fun chooserIntent(context: Context, intent: Intent, uri: Uri): Intent? {
    val pm = context.packageManager
    val activities = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
    val chooserIntents = ArrayList<Intent>()
    val ourPackageName = context.packageName
    for (resInfo in activities) {
        val info = resInfo.activityInfo
        if (!info.enabled || !info.exported) {
            continue
        }
        if (info.packageName == ourPackageName) {
            continue
        }
        val targetIntent = Intent(intent)
        targetIntent.setPackage(info.packageName)
        targetIntent.setDataAndType(uri, intent.type)
        chooserIntents.add(targetIntent)
    }
    if (chooserIntents.isEmpty()) {
        return null
    }
    val lastIntent = chooserIntents.removeAt(chooserIntents.size - 1)
    if (chooserIntents.isEmpty()) {
        return lastIntent
    }
    val chooserIntent = Intent.createChooser(lastIntent, null)
    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, chooserIntents.toTypedArray())
    return chooserIntent
}