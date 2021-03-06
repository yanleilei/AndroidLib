package com.leilei.androidlib;

import android.content.res.AssetManager;
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
        copyStream(in, out, null);
    }


    public static void saveString(File file, String text, boolean append) throws IOException {
        file = checkFile(file);
        copyStream(new ByteArrayInputStream(text.getBytes()), new FileOutputStream(file, append));
    }

    private static File checkFile(File file) {
        if (!file.exists()) {
            String path = file.getAbsolutePath();
            if (path.contains("/")) {
                int index = path.lastIndexOf("/");
                String dir = path.substring(0, index);
                String name = path.substring(index + 1, path.length());
                File fileDir = new File(dir);
                fileDir.mkdir();
                fileDir.mkdirs();
                file = new File(fileDir, name);
            }
        }
        return file;
    }

    public static void saveString(String filepath, String text) throws IOException {
        saveString(new File(filepath), text, false);
    }

    public static void saveString(File file, String text) throws IOException {
        saveString(file, text, false);
    }

    public static void copyFile(String src, String des) throws IOException {
        File fileSrc = new File(src);
        File fileDes = new File(des);
        if (fileSrc.isDirectory()) {
            copyFile(fileSrc, fileDes);
        } else {
            copyStream(new FileInputStream(src), new FileOutputStream(des));
        }

    }

    public static void copyFile(File src, File des) throws IOException {
        if (src.isDirectory()) {
            des.mkdir();
            des.mkdirs();
            File[] files = src.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        File desSubDir = new File(des, file.getName());
                        desSubDir.mkdir();
                        copyFile(file, desSubDir);
                    } else {
                        File desSubFile = new File(des, file.getName());
                        copyFile(file, desSubFile);
                    }
                }
            }
        } else {
            copyStream(new FileInputStream(src), new FileOutputStream(des));
        }
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
        file = checkFile(file);
        bitmap.compress(format, quality, new FileOutputStream(file));
    }

    public static void copyFileFromAsset(String assetFile, String desFile) throws IOException {
        copyFileFromAsset(assetFile, new File(desFile));
    }

    public static void copyFileFromAsset(String assetFile, File desfile) throws IOException {
        AssetManager assetManager = Utils.mContext.getAssets();
        String filenames[] = assetManager.list(assetFile);
        if (filenames.length > 0) {
            desfile.mkdir();
            desfile.mkdirs();
            for (String fileName : filenames) {
                copyFileFromAsset(assetFile + "/" + fileName, new File(desfile, fileName));
            }
        } else {
            copyStream(assetManager.open(assetFile), new FileOutputStream(desfile));
        }

    }

    public static void copyStream(InputStream in, OutputStream out, ProgressListener listener) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        long total = 0;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
            total += len;
            if (listener != null) {
                listener.onProgress(total);
            }
        }
        in.close();
        out.close();
    }

    public interface ProgressListener {

        void onProgress(float finishLength);
    }

}
