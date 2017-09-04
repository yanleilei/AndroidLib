package com.leilei.androidlib;

import android.util.Log;

/**
 * Created by leilei on 2017/7/27.
 */

public class LogUtils {

    public static void v(String tag, String info) {
        v(tag, info, null);
    }

    public static void d(String tag, String info) {
        d(tag, info, null);
    }

    public static void i(String tag, String info) {
        i(tag, info, null);
    }

    public static void w(String tag, String info) {
        w(tag, info, null);
    }

    public static void e(String tag, String info) {
        e(tag, info, null);
    }

    public static void v(String tag, String info, Throwable tr) {
        if (Utils.DEBUG) {
            Log.v(tag, info, tr);
        }
    }

    public static void d(String tag, String info, Throwable tr) {
        if (Utils.DEBUG) {
            Log.d(tag, info, tr);
        }
    }

    public static void i(String tag, String info, Throwable tr) {
        if (Utils.DEBUG) {
            Log.i(tag, info, tr);
        }
    }

    public static void w(String tag, String info, Throwable tr) {
        if (Utils.DEBUG) {
            Log.w(tag, info, tr);
        }
    }

    public static void e(String tag, String info, Throwable tr) {
        if (Utils.DEBUG) {
            Log.e(tag, info, tr);
        }
    }
}
