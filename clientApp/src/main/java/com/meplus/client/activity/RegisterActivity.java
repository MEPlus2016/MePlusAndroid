package com.meplus.client.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SignUpCallback;
import com.meplus.activity.BaseActivity;
import com.meplus.avos.objects.AVOSUser;
import com.meplus.client.BuildConfig;
import com.meplus.client.R;
import com.meplus.client.utils.SnackBarUtils;
import com.meplus.client.utils.UUIDUtils;
import com.meplus.events.EventUtils;
import com.meplus.events.SignUpEvent;
import com.meplus.utils.IntentUtils;
import com.meplus.utils.UIDUtil;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册
 * 用户名：meplus 密码：meplus 注册邮箱：13641194007@139.com
 * 机器人：robot2 密码：robot2 注册邮箱：wanggeng@meplusplus.com
 */
public class RegisterActivity extends BaseActivity implements Validator.ValidationListener {
    @Bind(R.id.root)
    ViewGroup mRoot;

    @NotEmpty
    @Bind(R.id.phone_edit)
    EditText mPhoneEdit;

    @Email
    @Bind(R.id.email_edit)
    EditText mEmailEdit;

    @Password(min = 6)
    @Bind(R.id.password_edit)
    EditText mPasswordEdit;

    @ConfirmPassword
    @Bind(R.id.confirm_edit)
    EditText mConfirmEdit;

    private Validator mValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);

        if (BuildConfig.DEBUG) {
            mPhoneEdit.setText("meplus");
            mPasswordEdit.setText("meplus");
            mConfirmEdit.setText("meplus");
            mEmailEdit.setText("13641194007@139.com");
        }

    }

    @OnClick({R.id.register_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_button:
                mValidator.validate();
                break;
        }
    }

    @Override
    public void onValidationSucceeded() {
        doRegister();
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

    private void doRegister() {
        final String username = mPhoneEdit.getText().toString();
        final String password = mPasswordEdit.getText().toString();
        final String email = mEmailEdit.getText().toString();
        final int userId = UIDUtil.getUid();
        AVOSUser user = new AVOSUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setUserId(userId);
        user.setUUId(UUIDUtils.getUUID(this));

        user.signUpInBackground(new SignUpCallback() {
            public void done(AVException e) {
                if (e == null) {
                    EventUtils.postEvent(new SignUpEvent());
                    startActivity(IntentUtils.generateIntent(RegisterActivity.this, MainActivity.class));
                    finish();
                } else {
                    SnackBarUtils.show(mRoot, e.toString());
                }
            }
        });

    }
}
