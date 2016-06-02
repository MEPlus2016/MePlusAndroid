package com.meplus.client.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.meplus.activity.BaseActivity;
import com.meplus.avos.objects.AVOSRobot;
import com.meplus.avos.objects.AVOSUser;
import com.meplus.client.R;
import com.meplus.client.app.MPApplication;
import com.meplus.client.avos.User;
import com.meplus.events.ErrorEvent;
import com.meplus.events.EventUtils;
import com.meplus.events.QueryEvent;
import com.meplus.utils.IntentUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import cn.trinea.android.common.util.ListUtils;
import cn.trinea.android.common.util.ToastUtils;
import hugo.weaving.DebugLog;

/**
 * 启动Logo页面
 */
public class LogoActivity extends BaseActivity implements Handler.Callback {
    private static final String TAG = LogoActivity.class.getSimpleName();

    @butterknife.Bind(R.id.shimmer_view_container)
    ShimmerFrameLayout mShimmerViewContainer;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        ButterKnife.bind(this);
        EventUtils.register(this);
        mShimmerViewContainer.startShimmerAnimation();
        mHandler = new Handler(this);
        mHandler.sendEmptyMessageDelayed(1, 3000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(1);
        EventUtils.unregister(this);
    }

    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onQueryEvent(QueryEvent<AVOSRobot> event) {
        if (event.ok()) {
            final List<AVOSRobot> robotList = event.getList();
            if (!ListUtils.isEmpty(robotList)) {
                MPApplication.getsInstance().setRobot(robotList.get(0));
            }
            startActivity(IntentUtils.generateIntent(LogoActivity.this, MainActivity.class));
            finish();
        }
    }

    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onErrorEvent(ErrorEvent event) {
        if (event.ok()) {
            finish();
            ToastUtils.show(this, event.getThrowable().getMessage());
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == 1) {
            final AVOSUser user = AVOSUser.getCurrentUser(AVOSUser.class);
            if (user == null) {
                startActivity(IntentUtils.generateIntent(LogoActivity.this, LoginActivity.class));
                finish();
            } else {
                User.queryRobot(user);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

}
