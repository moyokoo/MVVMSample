package net.android.anko.utils.banner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class CounterTimer {

    private static final String TAG = CounterTimer.class.getSimpleName();

    private static final int MSG_RUN = 1;
    private static final int MSG_PAUSE = 2;

    private long mInterval;
    Context context;
    private OnTimerListener mTimerListener;
    private String mTag;

    public CounterTimer(Context context, long mInterval) {
        this.context = context;
        this.mInterval = mInterval;
    }

    public void setTag(String mTag) {
        this.mTag = mTag;
    }

    public void setListener(OnTimerListener mTimerListener) {
        this.mTimerListener = mTimerListener;
    }

    private void destroy() {
        Log.i(TAG, "destroy-->");
        mHandler.removeMessages(MSG_RUN);
        mHandler.removeMessages(MSG_PAUSE);
    }

    public void pause() {
        Log.i(TAG, "pause-->");
        mHandler.removeMessages(MSG_RUN);
        mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_PAUSE));
    }

    public void start() {
        Log.i(TAG, "start-->");
        mHandler.removeMessages(MSG_RUN);
        mHandler.removeMessages(MSG_PAUSE);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_RUN), mInterval);
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            synchronized (CounterTimer.this) {
                switch (message.what) {
                    case MSG_RUN:
                        Log.i(TAG, "MSG_RUN");
                        mHandler.sendEmptyMessageDelayed(MSG_RUN, mInterval);
                        mTimerListener.onTick();
                        break;

                    case MSG_PAUSE:
                        break;
                }
            }
            return false;
        }
    });


    public interface OnTimerListener {
        void onTick();
    }

}