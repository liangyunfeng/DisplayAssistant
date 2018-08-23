package com.lyf.display.util;

import android.content.Context;
import android.os.PowerManager;

/**
 * Created by lyf on 2018/2/25.
 */

public class WakeLock {

    private static PowerManager.WakeLock sWakeLock;

    public static synchronized void acquire(Context context) {
        if (sWakeLock != null) {
            sWakeLock.release();
        }

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        sWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "==KeepScreenOn==");
        sWakeLock.acquire();
    }

    public static synchronized void release() {
        if (sWakeLock != null) {
            sWakeLock.release();
            sWakeLock = null;
        }
    }
}