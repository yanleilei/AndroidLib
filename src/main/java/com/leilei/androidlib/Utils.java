package com.leilei.androidlib;

import android.app.Application;
import android.content.Context;

/**
 * Created by leilei on 2017/7/27.
 */

public class Utils {
    public static Context mContext;
    public static boolean DEBUG = true;

    public static void init(Application application, boolean debug) {
        mContext = application;
        DEBUG = debug;
    }
}
