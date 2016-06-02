package com.meplus.fancy.activity;

import android.os.Bundle;

import com.meplus.fancy.R;
import com.meplus.fancy.events.ScannerEvent;
import com.meplus.fancy.fragments.SimpleScannerFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.trinea.android.common.util.ToastUtils;

/**
 * 扫描页面
 */
public class ScannerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        EventBus.getDefault().register(this);
        replaceContainer(R.id.frame_layout, SimpleScannerFragment.newInstance());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScannerEvent(ScannerEvent event) {
        final String data = event.getContent();
        ToastUtils.show(this, data);
        finish();
    }
}
