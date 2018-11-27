package net.android.anko.widgets;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import net.android.anko.R;
import net.android.anko.helper.Bundler;
import net.android.anko.helper.InputHelper;

/**
 * Created by Kosh on 16 Sep 2016, 2:15 PM
 */

public class MessageDialogView extends BaseBottomSheetDialog {

    public static final String TAG = MessageDialogView.class.getSimpleName();

    public interface MessageDialogViewActionCallback {

        void onMessageDialogActionClicked(boolean isOk, @Nullable Bundle bundle);

        void onDialogDismissed();

    }

    TextView title;
    TextView message;
    TextView cancel;
    TextView ok;

    @Nullable
    private MessageDialogViewActionCallback callback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() != null && getParentFragment() instanceof MessageDialogViewActionCallback) {
            callback = (MessageDialogViewActionCallback) getParentFragment();
        } else if (context instanceof MessageDialogViewActionCallback) {
            callback = (MessageDialogViewActionCallback) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    public void onClick(View view) {
        if (callback != null) {
            isAlreadyHidden = true;
            callback.onMessageDialogActionClicked(view.getId() == R.id.ok, getArguments().getBundle("bundle"));
        }
        dismiss();
    }

    @Override
    protected int layoutRes() {
        return R.layout.message_bottom_dialog;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.title);
        message = view.findViewById(R.id.message);
        cancel = view.findViewById(R.id.cancel);
        ok = view.findViewById(R.id.ok);
        Bundle bundle = getArguments();
        title.setText(bundle.getString("bundleTitle"));
        message.setText(bundle.getString("bundleMsg"));
        cancel.setOnClickListener(this::onClick);
        ok.setOnClickListener(this::onClick);
    }


    @Override
    protected void onDismissedByScrolling() {
        super.onDismissedByScrolling();
        if (callback != null) callback.onDialogDismissed();
    }

    @Override
    protected void onHidden() {
        if (callback != null) callback.onDialogDismissed();
        super.onHidden();
    }

    @NonNull
    public static MessageDialogView newInstance(@NonNull String bundleTitle, @NonNull String bundleMsg) {
        MessageDialogView messageDialogView = new MessageDialogView();
        messageDialogView.setArguments(getBundle(bundleTitle, bundleMsg));
        return messageDialogView;
    }

    private static Bundle getBundle(String bundleTitle, String bundleMsg) {
        return Bundler.start()
                .put("bundleTitle", bundleTitle)
                .put("bundleMsg", bundleMsg)
                .end();
    }

}
