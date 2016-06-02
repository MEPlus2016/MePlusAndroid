package com.meplus.client.app;

import com.meplus.avos.objects.AVOSRobot;
import com.meplus.avos.objects.AVOSUser;

import hugo.weaving.DebugLog;

/**
 * Created by dandanba on 3/1/16.
 */
public class ModelApplication extends BaseApplication {
    private AVOSRobot mRobot;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @DebugLog
    public AVOSRobot getRobot() {
        return mRobot;
    }

    @DebugLog
    public void setRobot(AVOSRobot robot) {
        mRobot = robot;
    }

    public void logOut() {
        AVOSUser.logOut();
        setRobot(null);
    }
}
