package com.lyf.display.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.lyf.display.R;
import com.lyf.display.util.Constant;
import com.lyf.display.view.BlueLightFilterWindow;

/**
 * Created by lyf on 2018/2/26.
 */

public class BlueLightFilterService extends Service {
    public final static String TAG = "BlueLightFilterService";

    public static BlueLightFilterWindow mWindow;
    NotificationCompat.Builder builder;

    public static boolean isRunning = false;
    public static int mAlpha = 127;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
        mWindow = new BlueLightFilterWindow(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        Log.i(TAG, "onStartCommand: action = " + action);
        if (mWindow == null) {
            mWindow = new BlueLightFilterWindow(this);
        }
        if (!isRunning) {
            stayForeground();
            mWindow.setBackgroundAlpha(mAlpha);
            mWindow.showView();
            isRunning = true;
        }

        if (Constant.BLUE_LIGHT_FILTER_SERVICE_ACTION.equals(action)) {
            boolean enable = intent.getBooleanExtra(Constant.BLUE_LIGHT_FILTER_ENABLE, false);
            updateNotification(enable);
        }
        return START_NOT_STICKY;
    }

    private void stayForeground() {
        builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(getResources().getString(R.string.notification_blue_light_filter_title));
        builder.setContentText(getResources().getString(R.string.notification_blue_light_filter_message_enabled));
        builder.setSmallIcon(R.drawable.ic_screen_color);
        builder.setLights(Color.BLUE, 1000, 1000);
        Intent intent = new Intent(this, BlueLightFilterService.class);
        intent.setAction(Constant.BLUE_LIGHT_FILTER_SERVICE_ACTION);
        intent.putExtra(Constant.BLUE_LIGHT_FILTER_ENABLE, false);
        PendingIntent pi = PendingIntent.getService(this, Constant.BLUE_LIGHT_FILTER_PENDINGINTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);
        startForeground(Constant.BLUE_LIGHT_FILTER_NOTIFICATION_ID, builder.build());
    }

    private void updateNotification(boolean enable) {
        if (enable) {
            builder.setContentText(getResources().getString(R.string.notification_blue_light_filter_message_enabled));
            mWindow.showView();
        } else {
            builder.setContentText(getResources().getString(R.string.notification_blue_light_filter_message_disabled));
            mWindow.removeView();
        }
        Intent intent = new Intent(this, BlueLightFilterService.class);
        intent.setAction(Constant.BLUE_LIGHT_FILTER_SERVICE_ACTION);
        intent.putExtra(Constant.BLUE_LIGHT_FILTER_ENABLE, !enable);
        PendingIntent pi = PendingIntent.getService(this, Constant.BLUE_LIGHT_FILTER_PENDINGINTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);
        startForeground(Constant.BLUE_LIGHT_FILTER_NOTIFICATION_ID, builder.build());
    }

    private void quitBlueLightFilterService(boolean stop) {
        stopForeground(true);

        if (null != mWindow) {
            mWindow.removeView();
            mWindow = null;
        }

        isRunning = false;

        if (stop)
            stopSelf();
    }

    public static void updateBlueLightFilterWindow(int alpha) {
        Log.i(TAG, "updateBlueLightFilterWindow: alpha = " + alpha);
        mAlpha = alpha;
        if (mWindow != null) {
            mWindow.updateBackgroundView(alpha);
        }
    }

    public static int getAlphaFromBlueLightFilterWindow() {
        return mAlpha;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");

        if (isRunning) {
            quitBlueLightFilterService(false);
        }
    }
}
