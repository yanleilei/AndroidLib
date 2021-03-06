package com.leilei.androidlib;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by leilei on 16/8/31.
 */
public class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static String EXCEPTION_PATH = "";

    private String TAG = "UncaughtExceptionHandler";

    private static Context mContext;
    private Map<String, String> infos = new HashMap();

    //用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

    public static UncaughtExceptionHandler init(Context context, String path) {
        mContext = context;
        if (TextUtils.isEmpty(path)) {
            EXCEPTION_PATH = context.getPackageName() + "/ErrorLog/";
        } else {
            EXCEPTION_PATH = path;
        }
        UncaughtExceptionHandler exceptionHandler = new UncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
        return exceptionHandler;
    }


    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {

        //打印出错误
        ex.printStackTrace();
        LogUtils.i(TAG, "---thread.Name=  " + thread.getName());
        LogUtils.i(TAG, "---thread= Id " + thread.getId());
        LogUtils.i(TAG, "---日志打印结束 ");
        //保存异常错误到本地文件
        collectDeviceInfo(mContext);
        LogUtils.i(TAG, "---手机错误结束 ");
        //保存日志文件
        saveCatchInfo2File(ex);
        LogUtils.i(TAG, "---保存到本地结束 ");
        System.exit(0);
    }


    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCatchInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        String message = sb.toString();
        LogUtils.e(TAG, message);
        try {
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + ".txt";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                //这里修改了保存路径
                String path = Environment.getExternalStorageDirectory().getPath() + "/" + EXCEPTION_PATH;
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdir();
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(new File(dir, fileName));
                fos.write(message.getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            LogUtils.e(TAG, "an error occured while writing file..." + e);
        }
        return null;
    }


    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e(TAG, "an error occured when collect package info  \n" + e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                LogUtils.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                LogUtils.e(TAG, "an error occured when collect crash info  \n" + e);
            }
        }
    }


}
