package com.meplus.client.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.meplus.activity.BaseActivity;
import com.meplus.avos.objects.AVOSRobot;
import com.meplus.avos.objects.AVOSUser;
import com.meplus.client.R;
import com.meplus.client.app.MPApplication;
import com.meplus.client.avos.User;
import com.meplus.client.utils.SnackBarUtils;
import com.meplus.events.ErrorEvent;
import com.meplus.events.EventUtils;
import com.meplus.events.QueryEvent;
import com.meplus.events.SignUpEvent;
import com.meplus.utils.IntentUtils;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

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
 * 登录 username: meplus password:meplus
 */
public class LoginActivity extends BaseActivity {
    @Bind(R.id.root)
    ViewGroup mRoot;
    @NotEmpty
    @Bind(R.id.phone_edit)
    EditText mPhoneEdit;
    @Password(min = 6)
    @Bind(R.id.password_edit)
    EditText mPasswordEdit;
    private Validator mValidator;

    private Validator.ValidationListener mListener = new Validator.ValidationListener() {
        @Override
        public void onValidationSucceeded() {
            doLogin();
        }

        @Override
        public void onValidationFailed(List<ValidationError> errors) {
            for (ValidationError error : errors) {
                View view = error.getView();
                String message = error.getCollatedErrorMessage(LoginActivity.this);
                if (view instanceof EditText) {
                    ((EditText) view).setError(message);
                } else {
                    SnackBarUtils.show(mRoot, message);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        EventUtils.register(this);

        mValidator = new Validator(this);
        mValidator.setValidationListener(mListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mValidator = null;
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
            startActivity(IntentUtils.generateIntent(this, MainActivity.class));
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


    @OnClick({R.id.password_button, R.id.login_button, R.id.register_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.password_button:
                startActivity(IntentUtils.generateIntent(LoginActivity.this, PasswordActivity.class));
                break;
            case R.id.login_button:
                mValidator.validate();
                break;
            case R.id.register_button:
                startActivity(IntentUtils.generateIntent(LoginActivity.this, RegisterActivity.class));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSignUpEvent(SignUpEvent event) {
        if (event.ok()) {
            finish();
        }
    }

    private void doLogin() {
        final String username = mPhoneEdit.getText().toString();
        final String password = mPasswordEdit.getText().toString();

        AVOSUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null) {
                    final AVOSUser user = AVOSUser.getCurrentUser(AVOSUser.class);
                    User.queryRobot(user);
                } else {
                    SnackBarUtils.show(mRoot, e.toString());
                }
            }
        });
    }

}
