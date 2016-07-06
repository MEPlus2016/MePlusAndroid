package io.agora.sample.agora;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Dream on 2016/7/3.
 */
public class ToastUtils {
    public static void toShowToast(final Activity act,final String msg) {
        //判断是否是主线程
        if("main".equals(Thread.currentThread().getName())){
            Toast.makeText(act, msg, Toast.LENGTH_LONG).show();
        }else{
            act.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(act, msg, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
