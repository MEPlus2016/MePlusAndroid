package com.meplus.events;

/**
 * Created by dandanba on 3/11/16.
 * 登出
 */
public class BindEvent extends BaseEvent{

    public BindEvent() {}

    public String getMessage(){
        return "该机器人已经被其他用户绑定！";
    }
}
