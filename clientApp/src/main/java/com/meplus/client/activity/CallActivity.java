package com.meplus.client.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import com.meplus.activity.VideoActivity;
import com.meplus.avos.objects.AVOSUser;
import com.meplus.client.R;
import com.meplus.events.EventUtils;
import com.meplus.punub.Command;
import com.meplus.punub.CommandEvent;

import butterknife.ButterKnife;
import butterknife.OnTouch;

/**
 * 电话页面
 */
public class CallActivity extends VideoActivity {
    private static final String TAG = CallActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        ButterKnife.bind(this);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_call;
    }


    @OnTouch({R.id.left_button, R.id.up_button, R.id.right_button, R.id.down_button,R.id.zero, R.id.one, R.id.two, R.id.three,R.id.four, R.id.five, R.id.six, R.id.seven})
    public boolean onTouch(View view, MotionEvent event) {
        final int action = event.getAction();
        final int id = view.getId();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                return postEvent(id);
            case MotionEvent.ACTION_UP:
                return postEvent(MotionEvent.ACTION_UP);
        }
        return false;
    }

    private boolean postEvent(int id) {
        String message = "";
        switch (id) {
            case MotionEvent.ACTION_UP:
                message = Command.ACTION_STOP;
                break;
            case R.id.left_button:
                message = Command.ACTION_LEFT;
                break;
            case R.id.up_button:
                message = Command.ACTION_UP;
                break;
            case R.id.right_button:
                message = Command.ACTION_RIGHT;
                break;
            case R.id.down_button:
                message = Command.ACTION_DOWN;
                break;
            case R.id.zero:
                message = Command.ZERO;
                break;
            case R.id.one:
                message = Command.ONE;
                break;
            case R.id.two:
                message = Command.TWO;
                break;
            case R.id.three:
                message = Command.THREE;
                break;
            case R.id.four:
                message = Command.FOUR;
                break;
            case R.id.five:
                message = Command.FIVE;
                break;
            case R.id.six:
                message = Command.SIX;
                break;
            case R.id.seven:
                message = Command.SEVEN;
                break;
        }
        return postEvent(message);
    }

    private boolean postEvent(String message) {
        if (!TextUtils.isEmpty(message)) {
            final CommandEvent event = new CommandEvent();
            final AVOSUser user = AVOSUser.getCurrentUser(AVOSUser.class);
            final String sender = user.getUUId();
            event.setCommand(new Command(sender, message));
            EventUtils.postEvent(event);
            return true;
        }
        return false;
    }


}
