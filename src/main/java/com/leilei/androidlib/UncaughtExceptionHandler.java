package com.leilei.androidlib;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private Map<String, String> infos = new HashMap<String, String>();

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
        try {
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + ".html";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                ///这里修改了保存路径,如果不能保存到sdcard,则保存到data/data应用目录下
                String path = Environment.getExternalStorageDirectory().getPath() + "/" + EXCEPTION_PATH;
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                //发送给开发人员
                sendCrashLog2PM(path + fileName);
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            LogUtils.e(TAG, "an error occured while writing file..." + e);
        }
        return null;
    }

    /**
     * 目前只将log日志保存在sdcard 和输出到LogCat中，并未发送给后台。
     * 如果需要发送到云端，可以考虑用第三发的工具类似bugly等等
     */
    private void sendCrashLog2PM(String fileName) {

        FileInputStream fis = null;
        BufferedReader reader = null;
        String s = null;
        try {
            fis = new FileInputStream(fileName);
            reader = new BufferedReader(new InputStreamReader(fis, "GBK"));
            while (true) {
                s = reader.readLine();
                if (s == null) break;
                //由于目前尚未确定以何种方式发送，所以先打出log日志。
                LogUtils.e("info", s.toString());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {   // 关闭流
            try {
                if (reader != null)
                    reader.close();
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
