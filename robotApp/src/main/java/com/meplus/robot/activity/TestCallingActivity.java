package com.meplus.robot.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.meplus.activity.VideoActivity;
import com.meplus.robot.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dream on 2016/7/14.
 */
public class TestCallingActivity extends VideoActivity {

    @Bind(R.id.action_hung_up)
    Button hungUp;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        ButterKnife.bind(this);

        hungUp.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_testcalling;
    }
}
