package com.meplus.fancy.events;

/**
 * Created by dandanba on 3/3/16.
 */
public class LogEvent {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LogEvent(String text) {
        this.text = text;
    }

}
