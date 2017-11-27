package com.leilei.androidlib;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/9/6.
 */

public class PermissionHelper {

    private PermissionCallback mCallback;

    public PermissionHelper(PermissionCallback callback) {
        mCallback = callback;
    }

    public void requestPermissions(Activity activity, String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissionList = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                int permissionI = activity.checkSelfPermission(permissions[i]);
                if (permissionI != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permissions[i]);
                }
            }

            if (permissionList.size() > 0) {
                activity.requestPermissions(permissionList.toArray(new String[permissionList.size()]), 0);
            } else {
                if (mCallback != null) {
                    mCallback.onPermissionSuccess();
                }
            }
        } else {
            if (mCallback != null) {
                mCallback.onPermissionSuccess();
            }
        }
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        List<String> permissionList = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[i]);
            }
        }

        if (permissionList.size() > 0) {
            if (mCallback != null) {
                mCallback.onPermissionRefuse(permissionList.toArray(new String[permissionList.size()]));
            }
        } else {
            if (mCallback != null) {
                mCallback.onPermissionSuccess();
            }
        }
    }

    public void destory() {
        mCallback = null;
    }

    public interface PermissionCallback {
        void onPermissionSuccess();

        void onPermissionRefuse(String[] refusePermissions);
    }
}
