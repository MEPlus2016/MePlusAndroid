package com.meplus.utils;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by wyx on 2016/7/25.
 */
public class UUIDUtils {
    public static String getUUID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

}
