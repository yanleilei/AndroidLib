package com.leilei.androidlib;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by leilei on 15/11/24.
 */
public class SharedPreferenceUtils {
    private static Context mContext;

    static {
        mContext = Utils.mContext;
    }

    private static SharedPreferences.Editor getEditor(String file) {
        SharedPreferences sp = mContext.getSharedPreferences(file, Context.MODE_PRIVATE);
        return sp.edit();
    }

    public static void saveString(String file, String name, String value) {
        SharedPreferences.Editor editor = getEditor(file);
        editor.putString(name, value);
        editor.commit();
    }


    public static void saveInt(String file, String name, int value) {
        SharedPreferences.Editor editor = getEditor(file);
        editor.putInt(name, value);
        editor.commit();
    }

    public static void saveBoolean(String file, String name, boolean value) {
        SharedPreferences.Editor editor = getEditor(file);
        editor.putBoolean(name, value);
        editor.commit();
    }

    public static void saveFloat(String file, String name, float value) {
        SharedPreferences.Editor editor = getEditor(file);
        editor.putFloat(name, value);
        editor.commit();
    }

    public static void saveLong(String file, String name, long value) {
        SharedPreferences.Editor editor = getEditor(file);
        editor.putLong(name, value);
        editor.commit();
    }

    public static String getString(String file, String name) {
        SharedPreferences sp = mContext.getSharedPreferences(file, Context.MODE_PRIVATE);
        return sp.getString(name, "");
    }

    public static int getInt(String file, String name) {
        SharedPreferences sp = mContext.getSharedPreferences(file, Context.MODE_PRIVATE);
        return sp.getInt(name, 0);
    }

    public static float getFloat(String file, String name) {
        SharedPreferences sp = mContext.getSharedPreferences(file, Context.MODE_PRIVATE);
        return sp.getFloat(name, 0);
    }

    public static boolean getBoolean(String file, String name) {
        SharedPreferences sp = mContext.getSharedPreferences(file, Context.MODE_PRIVATE);
        return sp.getBoolean(name, false);
    }

    public static long getLong(String file, String name) {
        SharedPreferences sp = mContext.getSharedPreferences(file, Context.MODE_PRIVATE);
        return sp.getLong(name, 0);
    }

    public static void clear(String file) {
        SharedPreferences.Editor editor = getEditor(file);
        editor.clear();
        editor.commit();
    }

}
