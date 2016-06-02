package com.meplus.fancy.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.meplus.fancy.utils.IntentUtils;

public class SplashActivity extends BaseActivity implements Handler.Callback {
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler(this);
        mHandler.sendEmptyMessageDelayed(1, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(1);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == 1) {
            startActivity(IntentUtils.generateIntent(this, MainActivity.class));
            finish();
            return true;
        }
        return false;
    }
}
