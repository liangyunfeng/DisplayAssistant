package com.lyf.display.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
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
import com.lyf.display.service.ScreenLockService;
import com.lyf.display.service.ScreenWakeupService;
import com.lyf.display.util.Constant;

/**
 * Created by lyf on 2018/2/26.
 */

public class FloatingView implements View.OnClickListener, View.OnTouchListener {
    private static final String TAG = "FloatingView";
    private Context mContext;
    private WindowManager.LayoutParams mButtonParams, mBorderParams, mLockParams;
    private View mButtonView, mBorderView, mLockView, mButtonLock, mButtonQuit;
    private LinearLayout mLayoutLock;
    private View right, down, left, up;
    private WindowManager mWindowManager;
    private int mScreenWidth, mScreenHeight, mStatusBarHeight, mScreenDpi;
    private static boolean isShowing = false;
    private Intent stopService;
    private int mType = Constant.FLOATING_TYPE_WAKEUP;

    public FloatingView(Context context, int type) {
        mContext = context;
        mType = type;
        if (mType == Constant.FLOATING_TYPE_WAKEUP) {
            stopService = new Intent(mContext, ScreenWakeupService.class);
            stopService.setAction(Constant.SCREEN_WAKE_UP_SERVICE_ACTION_STOP);
        } else if (mType == Constant.FLOATING_TYPE_LOCK) {
            stopService = new Intent(mContext, ScreenLockService.class);
            stopService.setAction(Constant.SCREEN_LOCK_SERVICE_ACTION_STOP);
        }

        getScreenInfo();
    }

    protected void getScreenInfo() {
        mWindowManager = (WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics dm = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        mScreenDpi = dm.densityDpi;

        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            mStatusBarHeight = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        Log.i(TAG, "getScreenInfo: " + mScreenWidth + "w * " + mScreenHeight + "h, @" + mScreenDpi + "dpi, status bar height = " + mStatusBarHeight);
    }

    public void showView() {
        Log.i(TAG, "show floating button view");
        if (isShowing)
            return;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mBorderView = inflater.inflate(R.layout.floating_border, null);

        if (mType == Constant.FLOATING_TYPE_WAKEUP) {
            mBorderParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    (Build.VERSION.SDK_INT >= 26
                            ? (WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY)
                            : WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY),
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                    PixelFormat.TRANSLUCENT);
        } else {
            mBorderParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    (Build.VERSION.SDK_INT >= 26
                            ? (WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY | WindowManager.LayoutParams.TYPE_PHONE)
                            : WindowManager.LayoutParams.TYPE_PHONE),
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_IGNORE_CHEEK_PRESSES | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,
                    PixelFormat.TRANSLUCENT);
        }

        right = mBorderView.findViewById(R.id.right);
        down = mBorderView.findViewById(R.id.down);
        left = mBorderView.findViewById(R.id.left);
        up = mBorderView.findViewById(R.id.up);

        mWindowManager.addView(mBorderView, mBorderParams);

        if (mType == Constant.FLOATING_TYPE_WAKEUP) {
            mButtonView = inflater.inflate(R.layout.floating_button, null);
            mButtonParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    (Build.VERSION.SDK_INT >= 26
                            ? (WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY | WindowManager.LayoutParams.TYPE_PHONE)
                            : WindowManager.LayoutParams.TYPE_PHONE),
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    PixelFormat.TRANSLUCENT);
            mButtonParams.gravity = Gravity.TOP | Gravity.START;
            mButtonView.measure(mScreenWidth, mScreenHeight);
            mButtonParams.x = mScreenWidth - mButtonView.getMeasuredWidth() - mContext.getResources().getDimensionPixelSize(R.dimen.floating_main_margin_right);
            mButtonParams.y = mContext.getResources().getDimensionPixelSize(R.dimen.floating_main_margin_top);
            mButtonView.setOnTouchListener(this);
            mButtonView.setOnClickListener(this);
            mWindowManager.addView(mButtonView, mButtonParams);
        } else if (mType == Constant.FLOATING_TYPE_LOCK) {
            mLockView = inflater.inflate(R.layout.floating_lock_button, null);
            mLockParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    (Build.VERSION.SDK_INT >= 26
                            ? (WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY | WindowManager.LayoutParams.TYPE_PHONE)
                            : WindowManager.LayoutParams.TYPE_PHONE),
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    PixelFormat.TRANSLUCENT);
            mLockParams.gravity = Gravity.TOP | Gravity.START;
            mLockView.measure(mScreenWidth, mScreenHeight);
            mLockParams.x = (mScreenWidth - mLockView.getMeasuredWidth()) / 2;
            mLockParams.y = 0;  //mContext.getResources().getDimensionPixelSize(R.dimen.floating_lock_main_margin_top) - mStatusBarHeight;
            mLayoutLock = (LinearLayout) mLockView.findViewById(R.id.layout_lock);
            mButtonLock = (View) mLockView.findViewById(R.id.btn_lock);
            ((LockView)mButtonLock).setCallBack(new LockView.CallBack() {
                @Override
                public void onLocked() {
                    updateBorderView(true);
                }

                @Override
                public void onUnLocked() {
                    updateBorderView(false);
                }
            });
            mButtonQuit = (View) mLockView.findViewById(R.id.btn_quit);
            mButtonQuit.setOnClickListener(this);
            mWindowManager.addView(mLockView, mLockParams);
        }
        isShowing = true;
        playAnimation(Constant.START_ANIMATION);
    }

    public void removeView() {
        if (!isShowing)
            return;
        Log.i(TAG, "remove floating button view");
        if (mWindowManager != null) {
            if (mType == Constant.FLOATING_TYPE_WAKEUP && mButtonView != null && mButtonView.isShown()) {
                mWindowManager.removeView(mButtonView);
            }
            if (mType == Constant.FLOATING_TYPE_LOCK && mLockView != null && mLockView.isShown()) {
                mWindowManager.removeView(mLockView);
            }
            if (mBorderView != null && mBorderView.isShown()) {
                mWindowManager.removeView(mBorderView);
            }
        }
        isShowing = false;
    }

    private void playAnimation(int type) {
        switch (type) {
            case Constant.START_ANIMATION:
                Animation rightAnim = AnimationUtils.loadAnimation(mContext, R.anim.right);
                rightAnim.setStartOffset(0 * Constant.BORDER_ANIMATION_DURATION);
                Animation downAnim = AnimationUtils.loadAnimation(mContext, R.anim.down);
                downAnim.setStartOffset(1 * Constant.BORDER_ANIMATION_DURATION);
                Animation leftAnim = AnimationUtils.loadAnimation(mContext, R.anim.left);
                leftAnim.setStartOffset(2 * Constant.BORDER_ANIMATION_DURATION);
                Animation upAnim = AnimationUtils.loadAnimation(mContext, R.anim.up);
                upAnim.setStartOffset(3 * Constant.BORDER_ANIMATION_DURATION);

                rightAnim.setDuration(Constant.BORDER_ANIMATION_DURATION);
                downAnim.setDuration(Constant.BORDER_ANIMATION_DURATION);
                leftAnim.setDuration(Constant.BORDER_ANIMATION_DURATION);
                upAnim.setDuration(Constant.BORDER_ANIMATION_DURATION);
                right.startAnimation(rightAnim);
                down.startAnimation(downAnim);
                left.startAnimation(leftAnim);
                up.startAnimation(upAnim);

                if (mType == Constant.FLOATING_TYPE_WAKEUP) {
                    Animation scaleInAnim = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
                    scaleInAnim.setDuration(500);
                    mButtonView.findViewById(R.id.stop_button).startAnimation(scaleInAnim);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mButtonView || v == mButtonQuit) {
            removeView();
            mContext.startService(stopService);
        }
    }

    private int thisWidth, thisHeight, startX, startY;
    private float touchX, touchY;
    private boolean isDragging = false;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                thisWidth = mButtonView.getMeasuredWidth();
                thisHeight = mButtonView.getMeasuredHeight();
                startX = mButtonParams.x;
                startY = mButtonParams.y;
                touchX = event.getRawX();
                touchY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                if (!isDragging) {
                    v.performClick();
                }
                isDragging = false;
                break;
            case MotionEvent.ACTION_MOVE:
                mButtonParams.x = startX + (int) (event.getRawX() - touchX);
                mButtonParams.y = startY + (int) (event.getRawY() - touchY);
                if (mButtonParams.x < 0)
                    mButtonParams.x = 0;
                if (mButtonParams.y < 0)
                    mButtonParams.y = 0;
                if (mButtonParams.x > mScreenWidth - thisWidth)
                    mButtonParams.x = mScreenWidth - thisWidth;
                if (mButtonParams.y > mScreenHeight - thisHeight)
                    mButtonParams.y = mScreenHeight - thisHeight;
                double dist = Math.sqrt((mButtonParams.x - startX) * (mButtonParams.x - startX) + (mButtonParams.y - startY) * (mButtonParams.y - startY));
                if (dist > 50 && !isDragging)
                    isDragging = true;
                if (isDragging) {
                    mWindowManager.updateViewLayout(mButtonView, mButtonParams);
                }
                break;
        }
        return true;
    }


    public void updatePosition(Configuration config) {
        float VRatio = (1.0f * mButtonParams.y / mScreenHeight);    // remember the last position
        float HRatio = (1.0f * mButtonParams.x / mScreenWidth);

        if (mScreenDpi != config.densityDpi) {
            removeView();   // can't update view width and height, so remove and show again
            showView();
        }

        getScreenInfo();    // resolution & densityDpi changed, get screen info again

        if (mType == Constant.FLOATING_TYPE_WAKEUP) {
            mButtonParams.x = (int) (mScreenWidth * HRatio);
            mButtonParams.y = (int) (mScreenHeight * VRatio);

            if (mButtonParams.x > mScreenWidth - thisWidth)
                mButtonParams.x = mScreenWidth - thisWidth;
            if (mButtonParams.y > mScreenHeight - thisHeight)
                mButtonParams.y = mScreenHeight - thisHeight;

            mWindowManager.updateViewLayout(mButtonView, mButtonParams);
            //Log.i(TAG, "mButtonView.width = " + mButtonView.getMeasuredWidth() + ", mButtonView.height = " + mButtonView.getMeasuredHeight());
        }
    }

    private void updateBorderView(boolean isLocked) {
        if (!isShowing) {
            return;
        }

        if (mWindowManager != null) {
            if (mBorderView != null && mBorderView.isShown()) {
                mWindowManager.removeView(mBorderView);
            }
            if (mLockView != null && mLockView.isShown()) {
                mWindowManager.removeView(mLockView);
            }
        }

        if (!isLocked) {
            mBorderParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    (Build.VERSION.SDK_INT >= 26
                            ? (WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY)
                            : WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY),
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                    PixelFormat.TRANSLUCENT);
        } else {
            mBorderParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    (Build.VERSION.SDK_INT >= 26
                            ? (WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY | WindowManager.LayoutParams.TYPE_PHONE)
                            : WindowManager.LayoutParams.TYPE_PHONE),
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                            | WindowManager.LayoutParams.FLAG_IGNORE_CHEEK_PRESSES | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    PixelFormat.TRANSLUCENT);
        }

        mWindowManager.addView(mBorderView, mBorderParams);
        mWindowManager.addView(mLockView, mLockParams);
    }
}
