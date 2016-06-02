package com.meplus.client.activity;

import android.os.Bundle;

import com.meplus.activity.BaseActivity;
import com.meplus.client.BuildConfig;
import com.meplus.client.R;
import com.meplus.client.events.ScannerEvent;
import com.meplus.client.fragments.SimpleScannerFragment;
import com.meplus.events.EventUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.trinea.android.common.util.ToastUtils;

/**
 * 测试扫描页面
 */
public class ScannerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        EventUtils.register(this);
        replaceContainer(R.id.frame_layout, SimpleScannerFragment.newInstance());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventUtils.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScannerEvent(ScannerEvent event) {
        if (event.ok()) {
            if (BuildConfig.DEBUG) {
                final String content = event.getContent();
                ToastUtils.show(this, content);
            }
            finish();
//            SimpleScannerFragment simpleScannerFragment = (SimpleScannerFragment) findFragmentById(R.id.frame_layout);
//            simpleScannerFragment.resumeScanner();
        }
    }
}
