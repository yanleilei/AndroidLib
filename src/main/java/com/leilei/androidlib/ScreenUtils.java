package com.leilei.androidlib;

import android.util.DisplayMetrics;

/**
 * Created by leilei on 2017/7/27.
 */

public class ScreenUtils {
    private static int SCREEN_WIDTH = 0;
    private static int SCREEN_HEIGHT = 0;
    private static float DESITY = 0f;


    private static void init() {
        DisplayMetrics metrics = Utils.mContext.getResources().getDisplayMetrics();
        SCREEN_WIDTH = metrics.widthPixels;
        SCREEN_HEIGHT = metrics.heightPixels;
        DESITY = metrics.density;
    }

    public static int getScreenWidth() {
        if (SCREEN_WIDTH == 0) {
            init();
        }
        return SCREEN_WIDTH;
    }

    public static int getScreenHeight() {
        if (SCREEN_HEIGHT == 0) {
            init();
        }
        return SCREEN_HEIGHT;
    }

    public static float getScrenDesity() {
        if (DESITY == 0) {
            init();
        }
        return DESITY;
    }

    public static int dpTopx(int dp) {
        if (DESITY == 0) {
            init();
        }
        return (int) (dp * DESITY);
    }

    public static int pxTodp(int px) {
        if (DESITY == 0) {
            init();
        }
        return (int) (px / DESITY);
    }
}
