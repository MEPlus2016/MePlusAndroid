package com.meplus.robot.utils;

import android.content.Context;
import android.content.Intent;

import com.meplus.activity.VideoActivity;
import com.meplus.robot.activity.TestCallingActivity;

/**
 * Created by Dream on 2016/7/14.
 */
public class IntentUtils extends com.meplus.utils.IntentUtils {

        public static Intent generateCallIntent(Context context, String channel, int userId) {
                Intent intent = new Intent(context, TestCallingActivity.class);
                intent.putExtra(TestCallingActivity.EXTRA_TYPE, TestCallingActivity.CALLING_TYPE_VIDEO);
                intent.putExtra(TestCallingActivity.EXTRA_USER_ID, userId);
                intent.putExtra(TestCallingActivity.EXTRA_CHANNEL, channel);
                return intent;
        }

}
