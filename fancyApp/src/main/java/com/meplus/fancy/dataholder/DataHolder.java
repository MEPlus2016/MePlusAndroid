package com.meplus.fancy.dataholder;

public class DataHolder<D> {
    private int type;
    private D data;

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }

    public DataHolder(D data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}