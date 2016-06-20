package com.meplus.client.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.meplus.activity.VideoActivity;
import com.meplus.avos.objects.AVOSUser;
import com.meplus.client.R;
import com.meplus.events.EventUtils;
import com.meplus.punub.Command;
import com.meplus.punub.CommandEvent;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnTouch;

/**
 * 电话页面
 */
public class CallActivity extends VideoActivity {
    private static final String TAG = CallActivity.class.getSimpleName();

    boolean isButton = true;

    @Bind(R.id.zero)
    ImageButton zero;

    @Bind(R.id.one)
    ImageButton one;

    @Bind(R.id.two)
    ImageButton two;

    @Bind(R.id.three)
    ImageButton three;

    @Bind(R.id.four)
    ImageButton four;

    @Bind(R.id.five)
    ImageButton five;

    @Bind(R.id.six)
    ImageButton six;

    @Bind(R.id.seven)
    ImageButton seven;

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
                if(isButton) {
                    zero.setEnabled(false);
                    one.setEnabled(true);
                    two.setEnabled(true);
                    three.setEnabled(true);
                    four.setEnabled(true);
                    five.setEnabled(true);
                    six.setEnabled(true);
                    seven.setEnabled(true);
                }
                message = Command.ZERO;
                break;
            case R.id.one:
                if(isButton) {
                    zero.setEnabled(true);
                    one.setEnabled(false);
                    two.setEnabled(true);
                    three.setEnabled(true);
                    four.setEnabled(true);
                    five.setEnabled(true);
                    six.setEnabled(true);
                    seven.setEnabled(true);
                }
                message = Command.ONE;
                break;
            case R.id.two:
                if(isButton) {
                    zero.setEnabled(true);
                    one.setEnabled(true);
                    two.setEnabled(false);
                    three.setEnabled(true);
                    four.setEnabled(true);
                    five.setEnabled(true);
                    six.setEnabled(true);
                    seven.setEnabled(true);
                }                message = Command.TWO;
                break;
            case R.id.three:
                if(isButton) {
                    zero.setEnabled(true);
                    one.setEnabled(true);
                    two.setEnabled(true);
                    three.setEnabled(false);
                    four.setEnabled(true);
                    five.setEnabled(true);
                    six.setEnabled(true);
                    seven.setEnabled(true);
                }
                message = Command.THREE;
                break;
            case R.id.four:
                if(isButton) {
                    zero.setEnabled(true);
                    one.setEnabled(true);
                    two.setEnabled(true);
                    three.setEnabled(true);
                    four.setEnabled(false);
                    five.setEnabled(true);
                    six.setEnabled(true);
                    seven.setEnabled(true);
                }
                message = Command.FOUR;
                break;
            case R.id.five:
                if(isButton) {
                    zero.setEnabled(true);
                    one.setEnabled(true);
                    two.setEnabled(true);
                    three.setEnabled(true);
                    four.setEnabled(true);
                    five.setEnabled(false);
                    six.setEnabled(true);
                    seven.setEnabled(true);
                }
                message = Command.FIVE;
                break;
            case R.id.six:
                if(isButton) {
                    zero.setEnabled(true);
                    one.setEnabled(true);
                    two.setEnabled(true);
                    three.setEnabled(true);
                    four.setEnabled(true);
                    five.setEnabled(true);
                    six.setEnabled(false);
                    seven.setEnabled(true);
                }
                message = Command.SIX;
                break;
            case R.id.seven:
                if(isButton) {
                    zero.setEnabled(true);
                    one.setEnabled(true);
                    two.setEnabled(true);
                    three.setEnabled(true);
                    four.setEnabled(true);
                    five.setEnabled(true);
                    six.setEnabled(true);
                    seven.setEnabled(false);
                }
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
