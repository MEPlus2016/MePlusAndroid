package com.meplus.client.events;

import com.meplus.events.BaseEvent;

/**
 * Created by dandanba on 3/3/16.
 */
public class ScannerEvent extends BaseEvent {
    public ScannerEvent(String content) {
        super();
        this.content = content;
    }

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
