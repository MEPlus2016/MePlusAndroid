package com.meplus.client.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.UpdatePasswordCallback;
import com.meplus.activity.BaseActivity;
import com.meplus.avos.objects.AVOSUser;
import com.meplus.client.R;
import com.meplus.client.utils.SnackBarUtils;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.trinea.android.common.util.ToastUtils;

/**
 * 修改密码
 */
public class EditPasswordActivity extends BaseActivity implements Validator.ValidationListener {

    @Bind(R.id.root)
    LinearLayout mRoot;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @NotEmpty
    @Password(min = 6)
    @Bind(R.id.old_password_edit)
    EditText mOldPasswordEdit;

    @NotEmpty
    @Password(min = 6)
    @Bind(R.id.new_password_edit)
    EditText mNewPasswordEdit;

    private Validator mValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        ButterKnife.bind(this);

        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setTitle("修改密码");
        setSupportActionBar(mToolbar);

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        final String oldPassword = mOldPasswordEdit.getText().toString();
        final String newPassword = mNewPasswordEdit.getText().toString();
        final AVOSUser user = AVOSUser.getCurrentUser(AVOSUser.class);
        user.updatePasswordInBackground(oldPassword, newPassword, new UpdatePasswordCallback() {
            public void done(AVException e) {
                if (e == null) {
                    // 修改密码成功
                    ToastUtils.show(EditPasswordActivity.this, "修改密码成功!");
                    finish();
                } else {
                    // 修改密码出错。
                    SnackBarUtils.show(mRoot, e.toString());
                }
            }
        });
    }
}
