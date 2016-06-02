package com.meplus.fancy.events;

import android.content.Context;

import cn.trinea.android.common.util.ToastUtils;

/**
 * Created by dandanba on 3/3/16.
 */
public class ErrorEvent {
    private Throwable error;
    private String method;

    public ErrorEvent(String method, Throwable error) {
        this.method = method;
        this.error = error;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public void showError(Context context) {
        ToastUtils.show(context, error != null ? error.toString() : "");
    }
}
