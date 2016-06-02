package com.meplus.client.app;

/**
 * Created by dandanba on 3/1/16.
 * MP 代表 MePlus
 */
public class MPApplication extends ModelApplication {
    private static MPApplication sInstance;

    public static MPApplication getsInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

}
