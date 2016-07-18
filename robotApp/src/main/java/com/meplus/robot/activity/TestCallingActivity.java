package com.meplus.robot.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.meplus.activity.VideoActivity;
import com.meplus.events.EventUtils;
import com.meplus.punub.Command;
import com.meplus.robot.R;
import com.meplus.robot.events.BluetoothEvent;
import com.meplus.robot.presenters.BluetoothPresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.trinea.android.common.util.ToastUtils;
import io.agora.rtc.IRtcEngineEventHandler;

/**
 * Created by Dream on 2016/7/14.
 */
public class TestCallingActivity extends VideoActivity {

    @Bind(R.id.action_hung_up)
    Button hungUp;

    @Bind(R.id.battery)
    ImageButton battery0;

//    ---------------------------------
    @Bind(R.id.stat_bytes)
    TextView mStat_bytes;
//    -----------------------------------

    private Context context;
    private boolean mBluetoothSupport = true;

    private BluetoothPresenter mBTPresenter;
    private int index;
    private byte bms;
    private int V;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        context = this;

        EventUtils.register(this);

        mBTPresenter = new BluetoothPresenter(context);
        if (!mBTPresenter.isBluetoothAvailable()) { // 蓝牙模块硬件支持
            mBluetoothSupport = false;
            ToastUtils.show(context, getString(R.string.bt_unsupport));
            finish();
            return;
        }

        ButterKnife.bind(this);
        hungUp.setVisibility(View.INVISIBLE);

        mBTPresenter.create(context);

        updateSOC(12500);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mBTPresenter.isBluetoothEnabled()) {// 蓝牙软件部分不支持
            mBTPresenter.enableBluetooth(this);
        } else {
            if (!mBTPresenter.isServiceAvailable()) { // 蓝牙服务没启动
                mBTPresenter.startBluetoothService();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventUtils.unregister(this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mBTPresenter.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_testcalling;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBluetoothEvent(BluetoothEvent event) {
        if (event.ok()) {
            if (event.isConnected()) {
                bms = event.getBMS();

                V = event.getVoltage();
                Log.i("Vaaa", V + "00000000000000");
                if (V > 0) {// 发送电量的数据
                    updateSOC(V);
                    //  handler.post(task);
                }
            }

        }
    }

    //判断电量及充电状态，并根据电量显示
    private void updateSOC(int V) {
        index = (V -12500) / 250 + 1;
        Log.i("index111", index + "aaaa");
        index = index > 10 ? 10 : index;
        String resName = String.format("battery%1$d", index * 10);
        String chargeName = String.format("charge%1$d", index * 10);
        //  mBMSState.setImageResource(getResources().getIdentifier(resName, "drawable", getPackageName()));
        if (bms == 3||bms == 7) {
            battery0.setImageResource(getResources().getIdentifier(chargeName, "drawable", getPackageName()));
        } else {
            battery0.setImageResource(getResources().getIdentifier(resName, "drawable", getPackageName()));
        }
    }

/////////////////////////////////////////////////////////////////////
    private final List<String> mUids = new ArrayList<>();
    @Override
    public synchronized void onUserJoined(int uid, int elapsed) {
        super.onUserJoined(uid, elapsed);
        final String strUid = String.valueOf(uid);
        if (!mUids.contains(strUid)) {
            mUids.add(strUid);
        }
    }

    @Override
    public void timeEscaped(int time) {
        super.timeEscaped(time);

        if(mUids.isEmpty()){
            if(bms!=3){
                mBTPresenter.sendDirection(Command.ACTION_STOP);
                //com.meplus.utils.ToastUtils.toShowToast(this,"stop");
            }
            if(time>3){
                doBackPressed();
            }
        }
    }
}
