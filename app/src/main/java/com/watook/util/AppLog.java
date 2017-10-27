package com.watook.util;

import android.util.Log;


public class AppLog {
    private static final boolean isDebug = true;

    public static boolean isDebug() {
        return isDebug;
    }

    public static void i(String msg) {
        i("custom", msg);
    }

    public static void i(String tag, String msg) {
        if (isDebug) Log.i(tag, msg);
    }

    public static void d(String msg) {
        d("custom", msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug) Log.d(tag, msg);
    }

    public static void e(String msg) {
        e("custom", msg);
    }

    public static void e(String tag, String msg) {
        if (isDebug) Log.e(tag, msg);
    }

}
