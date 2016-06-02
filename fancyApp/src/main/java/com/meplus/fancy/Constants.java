package com.meplus.fancy;

import android.os.Environment;

import java.io.File;

/**
 * Created by dandanba on 3/9/16.
 */
public class Constants {
    public static final String BASE_URL = "http://112.74.132.249:8082/";
    public static final String APPNO = "1006";//应用系统编号
    public static final String APIVERSION = "v1.0";// 接口版本号
    public static final String KEY = "c/khhXuhRxjDW10asd0+w+=";

    public static final String API_TOKEN = "21c6827d8237ace81b4f3b2ce7f56027"; // me+

    public static final String MEPLUS_ROBOT_PACKAGENAME = "com.meplus.robot";

    public static final File sLogPath = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "meplusplus.log");

}
