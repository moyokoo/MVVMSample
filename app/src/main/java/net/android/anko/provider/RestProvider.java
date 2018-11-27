package net.android.anko.provider;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import net.android.anko.R;
import net.android.anko.helper.InputHelper;

import java.io.File;

import retrofit2.HttpException;

/**
 * Created by Kosh on 08 Feb 2017, 8:37 PM
 */

public class RestProvider {

    public static final int PAGE_SIZE = 30;

    public static void downloadFile(@NonNull Context context, @NonNull String url) {
        if (InputHelper.isEmpty(url)) return;
        Uri uri = Uri.parse(url);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        String authToken = "";
        if (!TextUtils.isEmpty(authToken)) {
            request.addRequestHeader("Authorization", authToken.startsWith("Basic") ? authToken : "token " + authToken);
        }
        File direct = new File(Environment.getExternalStorageDirectory() + File.separator + context.getString(R.string.app_name));
        if (!direct.isDirectory() || !direct.exists()) {
            boolean isCreated = direct.mkdirs();
            if (!isCreated) {
//                Toast.makeText(AnkkoApp.getInstance(), "Unable to create directory to download file", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        String fileName = new File(url).getName();
        request.setDestinationInExternalPublicDir(context.getString(R.string.app_name), fileName);
        request.setTitle(fileName);
        request.setDescription(context.getString(R.string.downloading_file));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
        }
    }

    public static int getErrorCode(Throwable throwable) {
        if (throwable instanceof HttpException) {
            return ((HttpException) throwable).code();

        }
        return -1;
    }

//    @NonNull
//    public static UserRestService getUserService( ) {
//        return provideRetrofit().create(UserRestService.class);
//    }



}
