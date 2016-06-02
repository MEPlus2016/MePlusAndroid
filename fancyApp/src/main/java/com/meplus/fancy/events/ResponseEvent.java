package com.meplus.fancy.events;

import com.meplus.fancy.model.Response;

/**
 * Created by dandanba on 3/3/16.
 */
public class ResponseEvent<T> {
    private Response<T> response;
    private String method;

    public ResponseEvent(String method, Response<T> response) {
        this.response = response;
        this.method = method;
    }

    public Response<T> getResponse() {
        return response;
    }

    public void setResponse(Response<T> response) {
        this.response = response;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
