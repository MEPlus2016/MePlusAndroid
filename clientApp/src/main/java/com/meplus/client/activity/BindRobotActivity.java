package com.meplus.client.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.meplus.activity.BaseActivity;
import com.meplus.avos.objects.AVOSRobot;
import com.meplus.avos.objects.AVOSUser;
import com.meplus.client.R;
import com.meplus.client.app.MPApplication;
import com.meplus.client.avos.Robot;
import com.meplus.client.avos.User;
import com.meplus.client.events.ScannerEvent;
import com.meplus.client.utils.SnackBarUtils;
import com.meplus.events.ErrorEvent;
import com.meplus.events.EventUtils;
import com.meplus.events.QueryEvent;
import com.meplus.events.SaveEvent;
import com.meplus.utils.IntentUtils;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.trinea.android.common.util.ListUtils;
import cn.trinea.android.common.util.ToastUtils;
import hugo.weaving.DebugLog;

/**
 * 绑定机器人
 */
public class BindRobotActivity extends BaseActivity implements ValidationListener {
    private static final String TAG = BindRobotActivity.class.getSimpleName();

    @Bind(R.id.root)
    LinearLayout mRoot;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @NotEmpty
    @Bind(R.id.bind_edit)
    EditText mBindEdit;

    private Validator mValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_robot);
        ButterKnife.bind(this);
        EventUtils.register(this);

        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setTitle("绑定机器人");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);

        final AVOSRobot robot = MPApplication.getsInstance().getRobot();
        mBindEdit.setText(robot == null ? "" : robot.getUUId());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventUtils.unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.bind_button, R.id.scan_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bind_button:
                mValidator.validate();
                break;
            case R.id.scan_button:
                startActivity(IntentUtils.generateIntent(this, ScannerActivity.class));
                break;
        }
    }

    @Override
    public void onValidationSucceeded() {
        final String uuId = mBindEdit.getText().toString();
        Robot.queryByUUID(uuId);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                SnackBarUtils.show(mRoot, message);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScannerEvent(ScannerEvent event) {
        if (event.ok()) {
            final String uuId = event.getContent();
            Robot.queryByUUID(uuId);
        }
    }


    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onQueryEvent(QueryEvent<AVOSRobot> event) {
        if (event.ok()) {
            final List<AVOSRobot> robotList = event.getList();
            if (!ListUtils.isEmpty(robotList)) {
                final AVOSUser user = AVOSUser.getCurrentUser(AVOSUser.class);
                final AVOSRobot robot = robotList.get(0);
                User.addRobot(user, robot);
            } else {
                ToastUtils.show(this, "机器人ID有误！");
            }
        }
    }

    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSaveEvent(SaveEvent<AVOSRobot> event) {
        if (event.ok()) {
            finish();
        }
    }

    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onErrorEvent(ErrorEvent event) {
        if (event.ok()) {
            ToastUtils.show(this, event.getThrowable().getMessage());
        }
    }


}
