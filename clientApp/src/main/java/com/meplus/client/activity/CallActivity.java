package com.meplus.client.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.LogUtil;
import com.avos.avoscloud.SaveCallback;
import com.meplus.activity.VideoActivity;
import com.meplus.avos.objects.AVOSRobot;
import com.meplus.avos.objects.AVOSUser;
import com.meplus.client.R;
import com.meplus.client.app.MPApplication;
import com.meplus.events.EventUtils;
import com.meplus.punub.Command;
import com.meplus.punub.CommandEvent;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * 电话页面
 */
public class CallActivity extends VideoActivity {
    private static final String TAG = CallActivity.class.getSimpleName();
    boolean isButton = true;
    @Bind(R.id.zero)
    ImageButton zero;

   /* @Bind(R.id.one)
    ImageButton one;

    @Bind(R.id.two)
    ImageButton two;*/

    @Bind(R.id.three)
    ImageButton three;

   /* @Bind(R.id.four)
    ImageButton four;

    @Bind(R.id.five)
    ImageButton five;*/

    @Bind(R.id.six)
    ImageButton six;

    /*@Bind(R.id.seven)
    ImageButton seven;*/

    @Bind(R.id.seven)
    ImageButton seven;

    @Bind(R.id.img)
    ImageView img;

    @Bind(R.id.battery)
    ImageButton battery;

    //add control
    @Bind(R.id.control)
    FrameLayout control;

    @Bind(R.id.left_button)
    ImageButton left;

    @Bind(R.id.right_button)
    ImageButton right;

    @Bind(R.id.up_button)
    ImageButton up;

    @Bind(R.id.down_button)
    ImageButton down;

    int bms;
    AVOSRobot robot;
    int index;
    int bms_status;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // Handler处理消息
            if (msg.what == 1) {
                final AVOSRobot robot = MPApplication.getsInstance().getRobot();
                AVObject object = AVObject.createWithoutData("Robot", robot.getObjectId());
                String flag = "flag";
                String bms = "bms";
                object.fetchIfNeededInBackground(flag, new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        index = avObject.getInt("flag");
                        bms_status = avObject.getInt("bms");
                    }
                });
                Log.i("flag", index + "");
                Log.i("bms", bms_status + "");

              if(index >0) {
                  String resName = String.format("battery%1$d", index * 10);
                  String chargeName = String.format("charge%1$d", index * 10);
//                //  mBMSState.setImageResource(getResources().getIdentifier(resName, "drawable", getPackageName()));
                  if (bms_status == 3) {
                      battery.setImageResource(getResources().getIdentifier(chargeName, "drawable", getPackageName()));
                  } else {
                      battery.setImageResource(getResources().getIdentifier(resName, "drawable", getPackageName()));
                  }
              }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        ButterKnife.bind(this);
        Log.i("fffff", "aaaaaa");
        updateSOC(0);
    }

    private void updateSOC(int index) {
        String resName = String.format("battery%1$d", index * 10);
//        battery.setImageResource(getResources().getIdentifier(resName, "drawable", getPackageName()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timer timer = new Timer();
        // 创建一个TimerTask
        // TimerTask是个抽象类,实现了Runnable接口，所以TimerTask就是一个子线程
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        };
        timer.schedule(timerTask, 0, 1000);// 3秒后开始倒计时，倒计时间隔为60秒
    }

    @Override
    public int getContentView() {
        return R.layout.activity_call;
    }

    //add onclick 侧拉菜单
   /* @OnClick({R.id.channel_drawer_button0})
    public void onclick(View view){
        switch (view.getId()){
            case R.id.channel_drawer_button0:
                if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                    control.setVisibility(View.INVISIBLE);
                    battery.setVisibility(View.INVISIBLE);
                    mDrawerLayout.closeDrawer(GravityCompat.END);
                } else {
                    control.setVisibility(View.VISIBLE);
                    battery.setVisibility(View.VISIBLE);
                    mDrawerLayout.openDrawer(GravityCompat.END);
                }
        }
    }*/

    //    @OnTouch({R.id.left_button, R.id.up_button, R.id.right_button, R.id.down_button, R.id.zero, R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.battery})
    @OnTouch({R.id.left_button, R.id.up_button, R.id.right_button, R.id.down_button, R.id.zero, R.id.three, R.id.six, R.id.seven, R.id.battery})
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
                if (isButton) {
                    up.setPressed(false);
                    down.setPressed(false);
                    left.setPressed(false);
                    right.setPressed(false);
                }
                message = Command.ACTION_STOP;
                break;
            case R.id.left_button:
                if (isButton) {
                    left.setPressed(true);
                    right.setPressed(false);
                    up.setPressed(false);
                    down.setPressed(false);
                }
                message = Command.ACTION_LEFT;
                break;
            case R.id.up_button:
                if (isButton) {
                    up.setPressed(true);
                    down.setPressed(false);
                    left.setPressed(false);
                    right.setPressed(false);
                }
                message = Command.ACTION_UP;
                break;
            case R.id.right_button:
                if (isButton) {
                    right.setPressed(true);
                    left.setPressed(false);
                    up.setPressed(false);
                    down.setPressed(false);
                }
                message = Command.ACTION_RIGHT;
                break;
            case R.id.down_button:
                if (isButton) {
                    down.setPressed(true);
                    up.setPressed(false);
                    left.setPressed(false);
                    right.setPressed(false);
                }
                message = Command.ACTION_DOWN;
                break;
            case R.id.zero:
                if (isButton) {
                    zero.setEnabled(false);//正常
//                    one.setEnabled(true);
//                    two.setEnabled(true);
                    three.setEnabled(true);
//                    four.setEnabled(true);
//                    five.setEnabled(true);
                    six.setEnabled(true);
//                    seven.setEnabled(true);
//                    eight.setEnabled(false);
                    seven.setEnabled(true);
                    img.setEnabled(false);
                }
//                message = Command.ZERO;
                message = Command.SEVEN;
                break;
            case R.id.three:
                if (isButton) {
                    zero.setEnabled(true);
//                    one.setEnabled(true);
//                    two.setEnabled(true);
                    three.setEnabled(false);
//                    four.setEnabled(true);
//                    five.setEnabled(true);
                    six.setEnabled(true);
//                    seven.setEnabled(true);
//                    eight.setEnabled(false);
                    seven.setEnabled(true);
                    img.setEnabled(false);
                }
//                message = Command.THREE;
                message = Command.FIVE;
                break;

            case R.id.six:
                if (isButton) {
                    zero.setEnabled(true);
//                    one.setEnabled(true);
//                    two.setEnabled(true);
                    three.setEnabled(true);
//                    four.setEnabled(true);
//                    five.setEnabled(true);
                    six.setEnabled(false);
//                    seven.setEnabled(true);
//                    eight.setEnabled(false);
                    seven.setEnabled(true);
                    img.setEnabled(false);
                }
//                message = Command.SIX;
                message = Command.THREE;
                break;
            /*case R.id.seven:
                if (isButton) {
                    zero.setEnabled(true);
//                    one.setEnabled(true);
//                    two.setEnabled(true);
                    three.setEnabled(true);
//                    four.setEnabled(true);
//                    five.setEnabled(true);
                    six.setEnabled(true);
//                    seven.setEnabled(false);
//                    eight.setEnabled(false);
                }
//                message = Command.SEVEN;
                message = Command.ZERO;
                break;*/

            case R.id.seven:
                if (isButton) {
                    zero.setEnabled(true);
//                    one.setEnabled(true);
//                    two.setEnabled(true);
                    three.setEnabled(true);
//                    four.setEnabled(true);
//                    five.setEnabled(true);
                    six.setEnabled(true);
//                    seven.setEnabled(true);
//                    eight.setEnabled(false);
                    seven.setEnabled(false);
                    img.setEnabled(false);
                }
//                message = Command.SIX;
                message = Command.ZERO;
                break;
            case R.id.battery:
                message = Command.ACTION_HOME;
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
