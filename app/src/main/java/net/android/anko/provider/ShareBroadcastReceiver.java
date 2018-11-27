package net.android.anko.provider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import net.android.anko.helper.AppHelper;



public class ShareBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Uri uri = intent.getData();
        if(uri != null){
            String content = uri.toString();
            AppHelper.shareText(context, content);
        }
    }

}
