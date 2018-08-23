package com.lyf.display.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by lyf on 2018/2/26.
 */

public class PermissionUtil {
    private static final String TAG = "PermissionUtil";

    public static final int CHECK_ERROR = -1;
    public static final int CHECK_FAIL = 0;
    public static final int CHECK_SUCCESS = 1;

    public static int checkPermissions(Activity activity, String[] permissions) {
        if (permissions.length < 1) {
            return CHECK_ERROR;
        }

        for (int index = 0; index < permissions.length; index++) {
            if (ActivityCompat.checkSelfPermission(activity, permissions[index]) != PackageManager.PERMISSION_GRANTED) {
                Log.w(TAG, permissions[index].toString() + " is not granted!!");
                return CHECK_FAIL;
            }
        }

        return CHECK_SUCCESS;
    }

    public static void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

}