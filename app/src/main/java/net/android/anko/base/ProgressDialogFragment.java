package net.android.anko.base;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.Window;

import net.android.anko.R;
import net.android.anko.helper.Bundler;


public class ProgressDialogFragment extends BaseDialogFragment {

    public static final String TAG = ProgressDialogFragment.class.getSimpleName();

    public ProgressDialogFragment() {
    }

    @NonNull
    public static ProgressDialogFragment newInstance(@NonNull Resources resources, @StringRes int msgId, boolean isCancelable) {
        return newInstance(resources.getString(msgId), isCancelable);
    }

    public static ProgressDialogFragment newInstance() {
        return newInstance("", true);
    }

    @NonNull
    public static ProgressDialogFragment newInstance(@NonNull String msg, boolean isCancelable) {
        ProgressDialogFragment fragment = new ProgressDialogFragment();
        fragment.setArguments(Bundler.start()
                .put("msg", msg)
                .put("isCancelable", isCancelable)
                .end());
        return fragment;
    }

    @Override
    protected int provideLayout() {
        return R.layout.progress_dialog_layout;
    }

    @Override
    protected void onFragmentCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCancelable(false);
        setCancelable(false);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setDimAmount(0);
        }
        return dialog;
    }

}
