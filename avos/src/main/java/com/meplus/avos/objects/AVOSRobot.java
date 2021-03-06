package com.meplus.avos.objects;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;

import java.util.List;

@AVClassName("Robot")
public class AVOSRobot extends AVObject {
    public static final Creator CREATOR = AVObjectCreator.instance;

//    private final static String KEY_ROBOT_UUID = "robotUUID"; // String
    public final static String KEY_ROBOT_UUID = "robotUUID"; // String

    private final static String KEY_ROBOT_ID = "robotId"; // Int
    private final static String KEY_ROBOT_NAME = "robotName"; // String
    private final static String KEY_ROBOT_DESCRIPTION = "robotDescription"; // String
    private final static String KEY_ROBOT_BATTARY = "robotbattary";//Int
    private final static String KEY_ROBOT_FLAG = "flag";
    private final static String KEY_ROBOT_BMS = "bms";
    private final static String KEY_ROBOT_CALL = "call";//视频是否有人连接
    private final static String KEY_ROBOT_ONLINE = "online";
    private final static String KEY_ROBOT_TOOTH = "tooth";

    public boolean getRobotTooth(){return getBoolean(KEY_ROBOT_TOOTH);}
    public void setRobotTooth(boolean isTooth){put(KEY_ROBOT_TOOTH,isTooth);}

    public boolean getRobotOnline(){return getBoolean(KEY_ROBOT_ONLINE);}
    public void setRobotOnline(boolean isOnline){put(KEY_ROBOT_ONLINE,isOnline);}

    public boolean getRobotCall(){return getBoolean(KEY_ROBOT_CALL);}
    public void setRobotCall(boolean call){put(KEY_ROBOT_CALL,call);}

    public int getRobotBms(){return getInt(KEY_ROBOT_BMS);}
    public void setKeyRobotBms(int bms) {
        put(KEY_ROBOT_BMS, bms);
    }

    public  int getKeyRobotFlag() {
        return getInt(KEY_ROBOT_FLAG);
    }
    public void setKeyRobotFlag(int flag) {
        put(KEY_ROBOT_FLAG, flag);
    }

    public int getBattary() {
        return getInt(KEY_ROBOT_BATTARY);
    }

    public void setBattary(int battary) {
        put(KEY_ROBOT_BATTARY, battary);
    }

    public String getUUId() {
        return getString(KEY_ROBOT_UUID);
    }

    public void setUUId(String uuId) {
        put(KEY_ROBOT_UUID, uuId);
    }

    public int getRobotId() {
        return getInt(KEY_ROBOT_ID);
    }

    public void setRobotId(int robotId) {
        put(KEY_ROBOT_ID, robotId);
    }

    public String getRobotName() {
        return getString(KEY_ROBOT_NAME);
    }

    public void setRobotName(String robotName) {
        put(KEY_ROBOT_NAME, robotName);
    }

    public String getRobotDescription() {
        return getString(KEY_ROBOT_DESCRIPTION);
    }

    public void setRobotDescription(String robotDescription) {
        put(KEY_ROBOT_DESCRIPTION, robotDescription);
    }


    public static void registerSubclass() {
        AVObject.registerSubclass(AVOSRobot.class);
    }

    public static List<AVOSRobot> queryRobotByUUID(String uuid) throws AVException {
        final AVQuery<AVOSRobot> query = AVOSRobot.getQuery(AVOSRobot.class);
        query.whereEqualTo(KEY_ROBOT_UUID, uuid);
        final List<AVOSRobot> list = query.find();
        return list;
    }
}