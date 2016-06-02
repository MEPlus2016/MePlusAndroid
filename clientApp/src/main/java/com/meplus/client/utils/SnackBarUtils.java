package com.meplus.client.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by dandanba on 3/2/16.
 */
public class SnackBarUtils {
    public static void show(View view, String text) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
    }

}
