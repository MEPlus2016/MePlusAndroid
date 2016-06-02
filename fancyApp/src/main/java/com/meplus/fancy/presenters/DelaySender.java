package com.meplus.fancy.presenters;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by dandanba on 5/5/16.
 */
public class DelaySender {
    private static final String TAG = DelaySender.class.getSimpleName();
    public static final int MSG_SEND = 1;
    public static final int MSG_PROGRESS = 2;

    private static final long sDelayMillis = 10000;
    private final int sMaxCount = (int) (sDelayMillis / 1000);
    private Handler mDelaySender;
    private int mCount;

    public void create(Handler.Callback callback) {
        mDelaySender = new Handler(callback);
    }

    public void removeCallback() {
        mDelaySender.removeMessages(MSG_SEND);
        mDelaySender.removeMessages(MSG_PROGRESS);
    }

    public void delaySend(String data) {
        final Message msg = mDelaySender.obtainMessage();
        msg.what = MSG_SEND;
        msg.obj = data;
        mDelaySender.sendMessageDelayed(msg, sDelayMillis);
    }

    public void startProgress() {
        mCount = 0;
        mDelaySender.sendEmptyMessage(MSG_PROGRESS);
    }

    public int progress() {
        final int progress = mCount * 100 / sMaxCount;
        mDelaySender.removeMessages(MSG_PROGRESS);
        if (mCount < sMaxCount) {
            Log.i(TAG, "count: " + mCount);
            mDelaySender.sendEmptyMessageDelayed(MSG_PROGRESS, 1000);
        }
        mCount++;
        return progress;
    }
}
