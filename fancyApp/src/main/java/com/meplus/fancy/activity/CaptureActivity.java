package com.meplus.fancy.activity;

import android.os.Bundle;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.meplus.fancy.app.FancyApplication;
import com.meplus.fancy.events.ScannerEvent;
import com.meplus.fancy.utils.ISBNUtils;

import org.greenrobot.eventbus.EventBus;

import cn.trinea.android.common.util.ToastUtils;

/**
 * 扫描页面
 */
public class CaptureActivity extends com.meplus.zbar.CaptureActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.onCreate(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FancyApplication.getInstance().getRefWatcher().watch(this);
        SwipeBackHelper.onDestroy(this);
    }

    @Override
    public void onResult(String result) {
        super.onResult(result);
        result = result.trim();
        result = ISBNUtils.getISBN13(result);

        final ScannerEvent scannerEvent = new ScannerEvent(result);
        scannerEvent.setType(ScannerEvent.TYPE_CAMERA);
        EventBus.getDefault().post(scannerEvent);

        ToastUtils.show(this, result);
        finish();
    }

}
