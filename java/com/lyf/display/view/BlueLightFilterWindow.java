package com.lyf.display.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

import com.lyf.display.R;
import com.lyf.display.service.BlueLightFilterService;
import com.lyf.display.service.ScreenLockService;
import com.lyf.display.service.ScreenWakeupService;
import com.lyf.display.util.Constant;

/**
 * Created by lyf on 2018/2/26.
 */

public class BlueLightFilterWindow {
    private static final String TAG = "BlueLightFilterWindow";
    private Context mContext;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowParams;

    private View mBackgroundView;
    private float mAlpha = 0.5f;

    private static boolean isShowing = false;

    public BlueLightFilterWindow(Context context) {
        mContext = context;
        mWindowManager = (WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    }

    public void showView() {
        Log.i(TAG, "show blue light filter view");
        if (isShowing)
            return;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mBackgroundView = inflater.inflate(R.layout.blue_light_filter_view, null);

        mWindowParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                (Build.VERSION.SDK_INT >= 26
                        ? (WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY)
                        : WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY),
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);

        mBackgroundView.setAlpha(mAlpha);

        mWindowManager.addView(mBackgroundView, mWindowParams);

        isShowing = true;
    }

    public void removeView() {
        Log.i(TAG, "remove blue light filter view");
        if (!isShowing)
            return;

        if (mWindowManager != null) {
            if (mBackgroundView != null && mBackgroundView.isShown()) {
                mWindowManager.removeView(mBackgroundView);
            }
        }

        isShowing = false;
    }

    public void updateBackgroundView(int alpha) {
        mAlpha = alpha / 255.0f;
        if (!isShowing) {
            return;
        }
        Log.i(TAG, "updateBackgroundView: alpha = " + alpha);
        mBackgroundView.setAlpha(mAlpha);
        mWindowManager.updateViewLayout(mBackgroundView, mWindowParams);
    }

    public void setBackgroundAlpha(int alpha) {
        mAlpha = alpha / 255.0f;
    }

    /**
     * 过滤蓝光
     * @param blueFilterPercent 蓝光过滤比例[10-80]
     * */
    public static @ColorInt int getColor(int blueFilterPercent)
    {
        int realFilter = blueFilterPercent;
        if (realFilter < 10)
        {
            realFilter = 10;
        }
        else if (realFilter > 80)
        {
            realFilter = 80;
        }
        int a = (int) (realFilter / 80f * 180);
        int r = (int) (200 - (realFilter / 80f) * 190);
        int g = (int) (180 - ( realFilter / 80f) * 170);
        int b = (int) (60 - realFilter / 80f * 60);
        return Color.argb(a, r, g, b);
    }
}
