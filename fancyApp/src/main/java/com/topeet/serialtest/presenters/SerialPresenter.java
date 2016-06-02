package com.topeet.serialtest.presenters;

import android.util.Log;

import com.meplus.fancy.events.ErrorEvent;
import com.meplus.fancy.events.LogEvent;
import com.meplus.fancy.events.ScannerEvent;
import com.topeet.serialtest.serial;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by dandanba on 4/23/16.
 */
public class SerialPresenter {
    public static final String METHOD_RUN = "SerialScannerRun";
    public static final boolean LOG_EVENT = false;

    private static final String TAG = SerialPresenter.class.getSimpleName();
    // 充磁 0x02 0x56 0x52 0x32 0x03 0x37
    private static final int[] MAGNETIZE_BUFFER = new int[]{0x02, 0x56, 0x52, 0x32, 0x03, 0x37};
    // 消磁 0x02 0x56 0x52 0x31 0x03 0x34
    private static final int[] DEMAGNETIZE_BUFFER = new int[]{0x02, 0x56, 0x52, 0x31, 0x03, 0x34};

    private serial mCom = new serial();
    private StringBuffer mBuffer = new StringBuffer();
    private ReadThread mReadThread;

    public void start() {
        __log("start");
        mCom.Open(1, 9600);
        __log("mCom.Open(1, 9600);");
        /* Create a receiving thread */
        mReadThread = new ReadThread();
        mReadThread.start();
    }

    public void destroy() {
        __log("destroy");
        if (mReadThread != null && !mReadThread.isInterrupted()) {
            mReadThread.interrupt();
        }
        if (mCom != null) {
            mCom.Close();
        }
        mCom = null;
    }

    public int magnetize() {
        return write(MAGNETIZE_BUFFER, MAGNETIZE_BUFFER.length);
    }

    public int demagnetize() {
        return write(DEMAGNETIZE_BUFFER, DEMAGNETIZE_BUFFER.length);
    }

    private int write(int[] buffer, int len) {
        return mCom.Write(buffer, len);
    }

    class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                if (mCom != null) {
                    final int[] RX = mCom.Read();// 扫描二维码和条形码的结果
                    handleRX(RX);
                } else { // 处理底盘异常崩溃的情况
                    __log(" if (mCom == null) {");
                    interrupt();
                    __log("interrupt()");
                    EventBus.getDefault().post(new ErrorEvent(METHOD_RUN, new IllegalArgumentException("mCom == null")));
                    __log("post");
                }
            }
        }

        private void handleRX(int[] RX) {
            if (RX != null && RX.length > 0) { // 数据有效
                if (RX != null && RX.length > 0) { // 数据有效
                    __log(RX);
                    mBuffer.append(new String(RX, 0, RX.length));
                    final String buffer = mBuffer.toString().trim(); // 去掉 ‘0D’
                    __log(buffer);

                    if (RX[RX.length - 1] == 0x0d) { // 结束符号
                        __log("RX[RX.length - 1] == 0x0d");
                        mBuffer.setLength(0);
                        __log("mBuffer.setLength(0);");
                    }

                    if (buffer.startsWith("{")) { // JSON 格式
                        __log(" if (buffer.startsWith(\"{\")) {");
                        if (buffer.endsWith("}")) {// JSON 格式结束
                            __log(" if (buffer.endsWith(\"}\")) {");
                            mBuffer.setLength(0);
                            __log("mBuffer.setLength(0);");
                            EventBus.getDefault().post(new ScannerEvent(buffer));
                            __log("post");
                        }
                    } else {// ISBN 格式
                        __log("else");
                        if (mBuffer.length() == 13) { // 13位ISBN
                            __log("if (mBuffer.length() == 13) { ");
                            mBuffer.setLength(0);
                            __log("mBuffer.setLength(0);");
                            EventBus.getDefault().post(new ScannerEvent(buffer));
                            __log("post");
                        }
                    }
                }
            }
        }
    }

    private void __log(String text) {
        Log.i(TAG, text);
        if (LOG_EVENT) {
            EventBus.getDefault().post(new LogEvent(text));
        }
    }

    private void __log(int[] array) {
        final int size = array.length;
        StringBuffer sb = new StringBuffer(size);
        for (int i = 0; i < size; i++) {
            sb.append(String.format("%1$x, ", array[i]));
        }
        __log(sb.toString());
    }

    static {
        System.loadLibrary("serialtest");
    }

}
