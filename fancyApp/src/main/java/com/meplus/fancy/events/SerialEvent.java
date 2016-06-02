package com.meplus.fancy.events;

/**
 * Created by dandanba on 3/3/16.
 */
public class SerialEvent {
    public final static String TYPE_DEMAGNETIZE = "demagnetize";
    public final static String TYPE_MAGNETIZE = "magnetize";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type = TYPE_MAGNETIZE;

    public SerialEvent(String type) {
        this.type = type;
    }
}
