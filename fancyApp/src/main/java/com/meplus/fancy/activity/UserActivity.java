package com.meplus.fancy.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.meplus.fancy.R;
import com.meplus.fancy.events.ErrorEvent;
import com.meplus.fancy.events.ResponseEvent;
import com.meplus.fancy.fragments.BooksFragment;
import com.meplus.fancy.model.Response;
import com.meplus.fancy.model.entity.User;
import com.meplus.fancy.presenters.ApiPresenter;
import com.meplus.fancy.utils.ImageUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 用户信息
 */
public class UserActivity extends BaseActivity {
    private final static String TAG = UserActivity.class.getSimpleName();
    @Bind(R.id.icon)
    ImageView mIcon;
    @Bind(R.id.user_id)
    TextView mUserId;
    @Bind(R.id.name_text)
    TextView mNameText;

    private ApiPresenter mApiPresenter = new ApiPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        replaceContainer(R.id.frame_layout, BooksFragment.newInstance());

        final String Data = getIntent().getStringExtra("Data");
        final String LibraryId = getIntent().getStringExtra("LibraryId");
        mApiPresenter.getborrowlistbyrobot(ApiPresenter.METHOD_GETBORROWLISTBYROBOT, Data, LibraryId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseEventUser(ResponseEvent<User> event) {
        final String method = event.getMethod();
        if (ApiPresenter.METHOD_GETBORROWLISTBYROBOT.equals(method)) {
            final Response<User> response = event.getResponse();
            if (response.isOk()) {
                final User user = response.getResult();
                ImageUtils.withActivityInto(this, user.getIconUrl(), mIcon);
                mUserId.setText(user.getUserId());
                mNameText.setText(user.getNickName());

                BooksFragment fragment = (BooksFragment) findFragmentById(R.id.frame_layout);
                fragment.updateBooks(user.getBorrowBookList());
            } else {
                response.showMessage(this);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onErrorEvent(ErrorEvent event) {
        final String method = event.getMethod();
        if (ApiPresenter.METHOD_GETBORROWLISTBYROBOT.equals(method)) {
            event.showError(this);
        }
    }

}
