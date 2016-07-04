package com.meplus.avos.objects;

import android.util.Log;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.meplus.events.EventUtils;
import com.meplus.events.bindEvent;

import java.util.List;

/**
 * Created by dandanba on 4/3/16.
 */
@AVClassName("User")
public class AVOSUser extends AVUser {
    public static final Creator CREATOR = AVObjectCreator.instance;

    private final static String KEY_USER_UUID = "userUUID"; // String
//    private final static String KEY_ROBOT_UUID = "robotUUID"; // String
    public final static String KEY_ROBOT_UUID = "robotUUID"; // String

    private final static String KEY_USER_ID = "userIntId"; // Int
    private final static String RELATION_ROBOTS = "robots";

    public int getUserId() {
        return getInt(KEY_USER_ID);
    }

    public void setUserId(int userId) {
        put(KEY_USER_ID, userId);
    }

    public String getUUId() {
        return getString(KEY_USER_UUID);
    }

    public void setUUId(String uuId) {
        put(KEY_USER_UUID, uuId);
    }

    public void setRobotUUId(String uuId) {
        put(KEY_ROBOT_UUID, uuId);
    }

    public String getRobotUUId() {
        return getString(KEY_ROBOT_UUID);
    }

    public static void registerSubclass() {
        AVObject.registerSubclass(AVOSRobot.class);
    }

    public void addRobot(final AVOSRobot avosRobot) throws AVException {
        /*AVRelation<AVOSRobot> relation = getRelation(AVOSUser.RELATION_ROBOTS);
        relation.add(avosRobot);
        setRobotUUId(avosRobot.getUUId());
        save();*/

        //add
        final AVRelation<AVOSRobot> relation = getRelation(AVOSUser.RELATION_ROBOTS);
        String uuid = avosRobot.getUUId();
        AVQuery<AVOSUser> query = new AVQuery<AVOSUser>("_User");
        query.whereEqualTo(KEY_ROBOT_UUID,uuid);
        query.findInBackground(new FindCallback<AVOSUser>() {
            @Override
            public void done(List<AVOSUser> list, AVException e) {
                if(e == null){
                    if(list.size()==0){
                        relation.add(avosRobot);
                        setRobotUUId(avosRobot.getUUId());
//                        save();
                        saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                Log.i("save", "save success");
                            }
                        });
                    }else{
                        EventUtils.postEvent(new bindEvent());
                    }
                }
            }
        });

    }

    public List<AVOSRobot> queryRobotByUUID(String uuid) throws AVException {
        final AVRelation<AVOSRobot> relation = getRelation(AVOSUser.RELATION_ROBOTS);
        final AVQuery<AVOSRobot> query = relation.getQuery();
        query.whereEqualTo(KEY_ROBOT_UUID, uuid);
        final List<AVOSRobot> list = query.find();
        return list;
    }

    public List<AVOSRobot> queryRobot(int limit) throws AVException {
        final AVRelation<AVOSRobot> relation = getRelation(AVOSUser.RELATION_ROBOTS);
        final AVQuery<AVOSRobot> query = relation.getQuery();
        query.setLimit(limit);
        final List<AVOSRobot> list = query.find();
        return list;
    }

}
