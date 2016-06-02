package com.meplus.client.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.meplus.activity.BaseActivity;
import com.meplus.avos.objects.AVOSUser;
import com.meplus.client.R;
import com.meplus.client.utils.SnackBarUtils;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.trinea.android.common.util.ToastUtils;

/**
 * 修改密码
 */
public class PasswordActivity extends BaseActivity implements Validator.ValidationListener {
    @Bind(R.id.root)
    ViewGroup mRoot;
    @Email
    @NotEmpty
    @Bind(R.id.email_edit)
    EditText mEmailEdit;

    private Validator mValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        ButterKnife.bind(this);

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);

    }

    @OnClick({R.id.password_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.password_button:
                mValidator.validate();
                break;
        }
    }

    @Override
    public void onValidationSucceeded() {
        doResetPassword();
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

    private void doResetPassword() {
        final String email = mEmailEdit.getText().toString();
        AVOSUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
            public void done(AVException e) {
                if (e == null) {
                    // 已发送一份重置密码的指令到用户的邮箱
                    ToastUtils.show(PasswordActivity.this, "已发送一份重置密码的指令到用户的邮箱!");
                    finish();
                } else {
                    // 重置密码出错。
                    SnackBarUtils.show(mRoot, e.toString());
                }
            }
        });
    }
}
