package com.lyf.display.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.lyf.display.R;
import com.lyf.display.util.Constant;
import com.lyf.display.util.WakeLock;
import com.lyf.display.view.FloatingView;

/**
 * Created by lyf on 2018/2/26.
 */

public class ScreenLockService extends Service {
    public final static String TAG = "ScreenLockService";
    private FloatingView mFloatingView;
    private BroadcastReceiver mReceiver;
    public static boolean isRunning = false;

    private void registerEventReceiver() {
        if (isRunning) {
            return;
        }
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Log.i(TAG, "onReceive, action = " + action);

                if (action.equals(Intent.ACTION_SCREEN_OFF)) {   // only stop when screen turn on and in keyguard mode
                    Log.i(TAG, "screen off");
                    quitScreenLockService(true);
                } else if (action.equals(Intent.ACTION_SHUTDOWN)) {
                    Log.w(TAG, "system shut down, stop recording");
                    quitScreenLockService(true);
                } else if (action.equals(Intent.ACTION_LOCALE_CHANGED)) {
                    Log.i(TAG, "change system language");
                    stayForeground();   // re-send the notification in new language
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SHUTDOWN);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_LOCALE_CHANGED);
        registerReceiver(mReceiver, intentFilter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
        mFloatingView = new FloatingView(this, Constant.FLOATING_TYPE_LOCK);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        Log.i(TAG, "onStartCommand: action = " + action);
        if (Constant.SCREEN_LOCK_SERVICE_ACTION_STOP.equals(action)) {
            Log.i(TAG, "onStartCommand: ACTION_STOP");
            if (isRunning) {
                quitScreenLockService(true);
            } else {
                stopSelf();
            }
        } else if (Constant.SCREEN_LOCK_SERVICE_ACTION_START.equals(action)) {
            Log.i(TAG, "onStartCommand: ACTION_START");
            if (!isRunning) {
                registerEventReceiver();
                stayForeground();
                mFloatingView.showView();
                isRunning = true;
            }
        }
        return START_NOT_STICKY;
    }

    private void stayForeground() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(getResources().getString(R.string.notification_lock_title));
        builder.setContentText(getResources().getString(R.string.notification_lock_message));
        builder.setSmallIcon(R.drawable.ic_lock_btn);
        builder.setLights(Color.BLUE, 1000, 1000);
        Intent intent = new Intent(this, ScreenLockService.class);
        intent.setAction(Constant.SCREEN_LOCK_SERVICE_ACTION_STOP);
        PendingIntent pi = PendingIntent.getService(this, Constant.SCREEN_LOCK_PENDINGINTENT_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pi);
        startForeground(Constant.SCREEN_LOCK_NOTIFICATION_ID, builder.build());
    }

    private void quitScreenLockService(boolean stop) {
        stopForeground(true);
        if (null != mFloatingView) {
            mFloatingView.removeView();
        }
        isRunning = false;
        if (stop)
            stopSelf();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");

        if (null != mReceiver)
            unregisterReceiver(mReceiver);
    }
}
