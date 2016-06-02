package com.meplus.fancy.events;

/**
 * Created by dandanba on 3/3/16.
 */
public class ScannerEvent {
    public final static String TYPE_CAMERA = "camera";
    public final static String TYPE_SERIAL = "serial";

    private String content;
    private String type = TYPE_SERIAL;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ScannerEvent(String content) {
        this.content = content;
    }
}
