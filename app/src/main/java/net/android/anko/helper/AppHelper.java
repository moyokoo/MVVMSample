package net.android.anko.helper;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import net.android.anko.AnkkoApp;
import net.android.anko.BuildConfig;
import net.android.anko.R;
import net.android.anko.provider.CopyBroadcastReceiver;
import net.android.anko.provider.ShareBroadcastReceiver;
import net.android.anko.ui.github.RepositoryActivity;
import net.android.anko.widgets.webview.GitHubHelper;
import net.android.anko.widgets.webview.GitHubName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

/**
 * Created by kosh20111 on 18 Oct 2016, 9:29 PM
 */

public class AppHelper {
    private static final List<String> VIEW_IGNORE_PACKAGE = Arrays.asList(
            "com.gh4a", "com.fastaccess", "com.taobao.taobao"
    );

    public static void launchUrl(Context context, @NonNull Uri uri) {
        if (TextUtils.isEmpty(uri.toString())) return;
        String url = uri.toString();
        if (GitHubHelper.isImage(url)) {
//            ViewerActivity.showImage(context, url);
            return;
        }
        GitHubName gitHubName = GitHubName.fromUrl(url);
        String userName;
        String repoName;
        if (gitHubName == null) {
            openInCustomTabsOrBrowser(context, uri.toString());
            return;
        } else {
            userName = gitHubName.getUserName();
            repoName = gitHubName.getRepoName();
        }

//        if (GitHubHelper.isUserUrl(url)) {
//            ProfileActivity.show((Activity) context, userName);
//        } else
        if (GitHubHelper.isRepoUrl(url)) {
            RepositoryActivity.Companion.start(context, userName, repoName);
        } else {
            openInCustomTabsOrBrowser(context, url);
        }
    }

    public static void openInCustomTabsOrBrowser(@NonNull Context context, @NonNull String url) {
        if (TextUtils.isEmpty(url)) {
            Toasty.warning(context, context.getString(R.string.invalid_url), Toast.LENGTH_LONG).show();
            return;
        }
        //check http prefix
        if (!url.contains("//")) {
            url = "http://".concat(url);
        }

        String customTabsPackageName;
        if ((customTabsPackageName = CustomTabsHelper.INSTANCE.getBestPackageName(context)) != null) {
            Bitmap backIconBitmap = ViewHelper.getBitmapFromResource(context, R.drawable.ic_arrow_back_title);
            Intent shareIntent = new Intent(context.getApplicationContext(), ShareBroadcastReceiver.class);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent sharePendingIntent = PendingIntent.getBroadcast(
                    context.getApplicationContext(), 0, shareIntent, 0);
            Intent copyIntent = new Intent(context.getApplicationContext(), CopyBroadcastReceiver.class);
            copyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent copyPendingIntent = PendingIntent.getBroadcast(
                    context.getApplicationContext(), 0, copyIntent, 0);


            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                    .setToolbarColor(context.getResources().getColor(R.color.colorPrimary))
                    .setCloseButtonIcon(backIconBitmap)
                    .setShowTitle(true)
                    .addMenuItem(context.getString(R.string.share), sharePendingIntent)
                    .addMenuItem(context.getString(R.string.copy_url), copyPendingIntent)
//                    .setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left)
//                    .setExitAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .build();

            customTabsIntent.intent.setPackage(customTabsPackageName);
            customTabsIntent.launchUrl(context, Uri.parse(url));

        } else {
            openInBrowser(context, url);
        }

    }

    public static void openInBrowser(@NonNull Context context, @NonNull String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri).addCategory(Intent.CATEGORY_BROWSABLE);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent = createActivityChooserIntent(context, intent, uri, VIEW_IGNORE_PACKAGE);
        if (intent != null) {
            context.startActivity(intent);
        } else {
            //打开自带浏览器
//            Toasty.warning(context, context.getString(R.string.no_browser_clients), Toast.LENGTH_LONG).show();
        }
    }

    private static Intent createActivityChooserIntent(Context context, Intent intent,
                                                      Uri uri, List<String> ignorPackageList) {
        final PackageManager pm = context.getPackageManager();
        final List<ResolveInfo> activities = pm.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        final ArrayList<Intent> chooserIntents = new ArrayList<>();
        final String ourPackageName = context.getPackageName();

        Collections.sort(activities, new ResolveInfo.DisplayNameComparator(pm));

        for (ResolveInfo resInfo : activities) {
            ActivityInfo info = resInfo.activityInfo;
            if (!info.enabled || !info.exported) {
                continue;
            }
            if (info.packageName.equals(ourPackageName)) {
                continue;
            }
            if (ignorPackageList != null && ignorPackageList.contains(info.packageName)) {
                continue;
            }

            Intent targetIntent = new Intent(intent);
            targetIntent.setPackage(info.packageName);
            targetIntent.setDataAndType(uri, intent.getType());
            chooserIntents.add(targetIntent);
        }

        if (chooserIntents.isEmpty()) {
            return null;
        }

        final Intent lastIntent = chooserIntents.remove(chooserIntents.size() - 1);
        if (chooserIntents.isEmpty()) {
            // there was only one, no need to showImage the chooser
            return lastIntent;
        }

        Intent chooserIntent = Intent.createChooser(lastIntent, null);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                chooserIntents.toArray(new Intent[chooserIntents.size()]));
        return chooserIntent;
    }


    public static void shareText(@NonNull Context context, @NonNull String text) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.setType("text/plain");
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_to))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (ActivityNotFoundException e) {
            Toasty.warning(context, context.getString(R.string.no_share_clients)).show();
        }
    }

    public static void hideKeyboard(@NonNull View view) {
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean isNightMode(@NonNull Resources resources) {
        return true;
    }


    public static Fragment getFragmentByTag(@NonNull FragmentManager fragmentManager, @NonNull String tag) {
        return fragmentManager.findFragmentByTag(tag);
    }

    public static void cancelNotification(@NonNull Context context) {
//        cancelNotification(context, BundleConstant.REQUEST_CODE);
    }

    public static void cancelNotification(@NonNull Context context, int id) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(id);
        }
    }

    public static void cancelAllNotifications(@NonNull Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
    }

    public static void copyToClipboard(@NonNull Context context, @NonNull String uri) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(context.getString(R.string.app_name), uri);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toasty.success(AnkkoApp.getInstance(), context.getString(R.string.success_copied)).show();
        }
    }


    public static void updateAppLanguage(@NonNull Context context) {
//        String lang = PrefGetter.getAppLanguage();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            updateResources(context, lang);
//        }
//        updateResourcesLegacy(context, lang);
    }

    private static void updateResources(Context context, String language) {
        Locale locale = getLocale(language);
        Locale.setDefault(locale);
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private static void updateResourcesLegacy(Context context, String language) {
        Locale locale = getLocale(language);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    @NonNull
    public static Locale getLocale(String language) {
        Locale locale = null;
        if (language.equalsIgnoreCase("zh-rCN")) {
            locale = Locale.SIMPLIFIED_CHINESE;
        } else if (language.equalsIgnoreCase("zh-rTW")) {
            locale = Locale.TRADITIONAL_CHINESE;
        }
        if (locale != null) return locale;
        String[] split = language.split("-");
        if (split.length > 1) {
            locale = new Locale(split[0], split[1]);
        } else {
            locale = new Locale(language);
        }
        return locale;
    }


    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    private static boolean isInstalledFromPlaySore(@NonNull Context context) {
        final String ipn = context.getPackageManager().getInstallerPackageName(BuildConfig.APPLICATION_ID);
        return !InputHelper.isEmpty(ipn);
    }


    public static boolean isDeviceAnimationEnabled(@NonNull Context context) {
        float duration = Settings.Global.getFloat(context.getContentResolver(), Settings.Global.ANIMATOR_DURATION_SCALE, 1);
        float transition = Settings.Global.getFloat(context.getContentResolver(), Settings.Global.TRANSITION_ANIMATION_SCALE, 1);
        return (duration != 0 && transition != 0);
    }

    public static boolean isDataPlan() {
        final ConnectivityManager connectivityManager = (ConnectivityManager) AnkkoApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            final android.net.NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return mobile.isConnectedOrConnecting();
        }
        return false;
    }

}