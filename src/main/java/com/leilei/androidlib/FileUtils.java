package com.leilei.androidlib;

import android.graphics.Bitmap;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by leilei on 2017/7/27.
 */

public class FileUtils {
    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }

        in.close();
        out.close();
    }

    public static void saveString(File file, String text, boolean append) throws IOException {
        copyStream(new ByteArrayInputStream(text.getBytes()), new FileOutputStream(file, append));
    }

    public static void saveString(String filepath, String text) throws IOException {
        saveString(new File(filepath), text, false);
    }

    public static void saveString(File file, String text) throws IOException {
        saveString(file, text, false);
    }

    public static void copyFile(String src, String des) throws IOException {
        copyStream(new FileInputStream(src), new FileOutputStream(des));
    }

    public static void copyFile(File src, File des) throws IOException {
        copyStream(new FileInputStream(src), new FileOutputStream(des));
    }

    public static void saveBitmapAsPng(File file, Bitmap bitmap) throws FileNotFoundException {
        saveBitmapFile(file, bitmap, Bitmap.CompressFormat.PNG, 100);
    }

    public static void saveBitmapAsPng(File file, Bitmap bitmap, int quality) throws FileNotFoundException {
        saveBitmapFile(file, bitmap, Bitmap.CompressFormat.PNG, quality);
    }

    public static void saveBitmapAsJpeg(File file, Bitmap bitmap) throws FileNotFoundException {
        saveBitmapAsJpeg(file, bitmap, 100);
    }

    public static void saveBitmapAsJpeg(File file, Bitmap bitmap, int quality) throws FileNotFoundException {
        saveBitmapFile(file, bitmap, Bitmap.CompressFormat.JPEG, quality);
    }

    public static void saveBitmapFile(String filePath, Bitmap bitmap, Bitmap.CompressFormat format, int quality) throws FileNotFoundException {
        saveBitmapFile(new File(filePath), bitmap, format, quality);
    }

    public static void saveBitmapFile(File file, Bitmap bitmap, Bitmap.CompressFormat format, int quality) throws FileNotFoundException {
        bitmap.compress(format, quality, new FileOutputStream(file));
    }


}
