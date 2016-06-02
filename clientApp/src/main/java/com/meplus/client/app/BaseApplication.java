package com.meplus.client.app;

import com.meplus.avos.objects.AVOSRobot;
import com.meplus.avos.objects.AVOSUser;
import com.meplus.utils.AVOSUtils;
import com.meplus.utils.FIRUtils;
import com.squareup.leakcanary.LeakCanary;

import io.agora.sample.agora.AgoraApplication;

/**
 * Created by dandanba on 3/1/16.
 */
public class BaseApplication extends AgoraApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        FIRUtils.init(this);

        // LeakCanary
        LeakCanary.install(this);

        AVOSUser.registerSubclass();
        AVOSRobot.registerSubclass();

        AVOSUtils.initialize(this);
    }

}
