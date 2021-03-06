package com.meplus.robot.events;

import com.meplus.events.BaseEvent;

/**
 * Created by dandanba on 3/3/16.
 */
public class BluetoothEvent extends BaseEvent {
    public BluetoothEvent() {
        super();
    }

    private boolean mConnected;
    private int dis1;
    private int dis2;
    private int dis3;
    private int dis4;
    private int dis5;

    public int getVoltage() {
        return Voltage;
    }

    public void setVoltage(int voltage) {
        Voltage = voltage;
    }

    private int Voltage;
    private byte mSOC;
    private byte BMS;

    public byte getBMS() {
        return BMS;
    }

    public void setBMS(byte BMS) {
        this.BMS = BMS;
    }

    public int getDis1() {
        return dis1;
    }

    public void setDis1(int dis1) {
        this.dis1 = dis1;
    }

    public int getDis2() {
        return dis2;
    }

    public void setDis2(int dis2) {
        this.dis2 = dis2;
    }

    public int getDis3() {
        return dis3;
    }

    public void setDis3(int dis3) {
        this.dis3 = dis3;
    }

    public int getDis4() {
        return dis4;
    }

    public void setDis4(int dis4) {
        this.dis4 = dis4;
    }

    public int getDis5() {
        return dis5;
    }

    public void setDis5(int dis5) {
        this.dis5 = dis5;
    }

    public byte getSOC() {
        return mSOC;
    }

    public void setSOC(byte soc) {
        mSOC = soc;
    }

    public boolean isConnected() {
        return mConnected;
    }

    public void setConnected(boolean state) {
        this.mConnected = state;
    }

}