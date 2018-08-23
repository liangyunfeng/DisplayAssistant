package com.lyf.display;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.lyf.display.service.BlueLightFilterService;
import com.lyf.display.service.ScreenLockService;
import com.lyf.display.service.ScreenWakeupService;
import com.lyf.display.util.Constant;
import com.lyf.display.util.DeviceInfo;
import com.lyf.display.util.DisplayInfo;
import com.lyf.display.util.PermissionUtil;

import java.net.Inet4Address;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "MainActivity";
    private static final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.WAKE_LOCK};
    TextView mTxDisplayInfo, mTxDeviceInfo;
    View mBtnScreenOn, mBtnScreenLock;
    ImageView mBtnDisplay;
    LinearLayout mLayoutDisplay, mLayoutScreenColor, mLayoutScreenOn, mLayoutScreenLock;
    DisplayInfo mDisplayInfo;
    Switch mSwitch;
    SeekBar mColorSeekBar;
    private boolean isRequesting = false;
    private boolean isShowDisplayInfo = false;
    private int mDisplayInfoHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTxDisplayInfo = (TextView) findViewById(R.id.tx_display_info);
        mTxDeviceInfo = (TextView) findViewById(R.id.tx_device_info);
        mBtnDisplay = (ImageView) findViewById(R.id.btn_display);
        mBtnScreenOn = (View) findViewById(R.id.btn_screen_on);
        mBtnScreenLock = (View) findViewById(R.id.btn_screen_lock);
        mSwitch = (Switch) findViewById(R.id.swt_screen_color);
        mColorSeekBar = (SeekBar) findViewById(R.id.seekbar_color);
        mBtnDisplay.setOnClickListener(this);
        mBtnScreenOn.setOnClickListener(this);
        mBtnScreenLock.setOnClickListener(this);
        mLayoutDisplay = (LinearLayout) findViewById(R.id.layout_display);
        mLayoutScreenColor = (LinearLayout) findViewById(R.id.layout_screen_color);
        mLayoutScreenOn = (LinearLayout) findViewById(R.id.layout_screen_on);
        mLayoutScreenLock = (LinearLayout) findViewById(R.id.layout_screen_lock);
        mLayoutDisplay.setOnClickListener(this);
        mLayoutScreenColor.setOnClickListener(this);
        mLayoutScreenOn.setOnClickListener(this);
        mLayoutScreenLock.setOnClickListener(this);

        mDisplayInfo = new DisplayInfo(this);
        mTxDisplayInfo.setText(mDisplayInfo.getDisplayInfo());

        if (BlueLightFilterService.isRunning && BlueLightFilterService.mWindow != null) {
            mSwitch.setChecked(true);
        }
        mColorSeekBar.setProgress(BlueLightFilterService.mAlpha);

        isRequesting = false;
        mSwitch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
            if (!isRequesting && canDrawOverlay()) {
                return false;
            }
            isRequesting = true;
            return true;
            }
        });
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Log.v("lyf", "onCheckedChanged: isChecked = " + isChecked);
                Intent intent = new Intent(MainActivity.this, BlueLightFilterService.class);
                if (isChecked && !BlueLightFilterService.isRunning) {
                    //if (canDrawOverlay()) {
                        startService(intent);
                    //}
                } else {
                    if (BlueLightFilterService.isRunning) {
                        stopService(intent);
                    }
                }
            }
        });

        mColorSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.v("lyf", "onProgressChanged: i = " + i);
                BlueLightFilterService.updateBlueLightFilterWindow(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onResume() {
        isRequesting = false;
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_display:
            case R.id.btn_display:
                if (mDisplayInfoHeight == 0) {
                    mDisplayInfoHeight = mTxDisplayInfo.getHeight();
                }
                startAnimation(isShowDisplayInfo);
                break;
            case R.id.layout_screen_on:
            case R.id.btn_screen_on:
                Log.v(TAG, "click btn_screen_on");
                /** Check if app can be shown on the top of windows **/
                if (canDrawOverlay()) {
                    /** Check if app has the proper permissions: wakeup **/
                    //if (requestPermission()) {
                        goAfterPermissionGranted(Constant.FLOATING_TYPE_WAKEUP);
                    //}
                }
                break;
            case R.id.layout_screen_lock:
            case R.id.btn_screen_lock:
                if (canDrawOverlay()) {
                    goAfterPermissionGranted(Constant.FLOATING_TYPE_LOCK);
                }
                break;
            default:
                break;
        }
    }

    private void startScreenWakeupService() {
        Intent intent = new Intent(MainActivity.this, ScreenWakeupService.class);
        intent.setAction(Constant.SCREEN_WAKE_UP_SERVICE_ACTION_START);
        startService(intent);
    }

    private void stopScreenWakeupService() {
        Intent intent = new Intent(MainActivity.this, ScreenWakeupService.class);
        intent.setAction(Constant.SCREEN_WAKE_UP_SERVICE_ACTION_STOP);
        startService(intent);
    }

    private void startScreenLockService() {
        Intent intent = new Intent(MainActivity.this, ScreenLockService.class);
        intent.setAction(Constant.SCREEN_LOCK_SERVICE_ACTION_START);
        startService(intent);
    }

    private void stopScreenLockService() {
        Intent intent = new Intent(MainActivity.this, ScreenLockService.class);
        intent.setAction(Constant.SCREEN_LOCK_SERVICE_ACTION_STOP);
        startService(intent);
    }

    private boolean canDrawOverlay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /** 2. Check if user allow to display on top of window (System preload app) **/
            if (!Settings.canDrawOverlays(this)) {
                Log.i(TAG, "request overlay permission");
                startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION));
                Toast.makeText(this, R.string.grant_permission_overlay, Toast.LENGTH_LONG).show();
                //finish();
                return false;   // only when setting value is 'false', it returns 'false' as well
            }
            return true;
        }
        return true;
    }

    /**
     * Check required permission when starting up: [WAKE_LOCK] etc.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Constant.REQUEST_CODE_APP_PERMISSION) {
            // when start it, app permission not granted, but can display on top, once user confirm it will come here
            if (PermissionUtil.checkPermissions(this, REQUIRED_PERMISSIONS) == PermissionUtil.CHECK_SUCCESS) {
                Log.i(TAG, "onRequestPermissionsResult, permissions are granted!");
                goAfterPermissionGranted(Constant.FLOATING_TYPE_WAKEUP);
            } else {
                Toast.makeText(this, R.string.grant_permission_and_try_again, Toast.LENGTH_LONG).show();
                //finish();
            }
        }
    }

    private boolean requestPermission() {
        if (PermissionUtil.checkPermissions(this, REQUIRED_PERMISSIONS) == PermissionUtil.CHECK_SUCCESS) {
            return true;
        } else {
            Log.i(TAG, "need to grant permissions by user");
            PermissionUtil.requestPermissions(this, REQUIRED_PERMISSIONS, Constant.REQUEST_CODE_APP_PERMISSION);
        }

        return false;
    }

    private void goAfterPermissionGranted(int type) {
        if (type == Constant.FLOATING_TYPE_WAKEUP) {
            if (ScreenLockService.isRunning) {
                stopScreenLockService();
            }
            startScreenWakeupService();
        } else if (type == Constant.FLOATING_TYPE_LOCK) {
            if (ScreenWakeupService.isRunning) {
                stopScreenWakeupService();
            }
            startScreenLockService();
        }
    }

    private void startAnimation(boolean show) {
        ValueAnimator animator ;
        if(show){
            animator = ValueAnimator.ofInt(0,mDisplayInfoHeight);
        }else{
            animator = ValueAnimator.ofInt(mDisplayInfoHeight,0);
        }
        isShowDisplayInfo = !show;
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mTxDisplayInfo.getLayoutParams().height = (Integer) valueAnimator.getAnimatedValue();
                mTxDisplayInfo.requestLayout();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isShowDisplayInfo) {
                    mBtnDisplay.setImageResource(R.drawable.expander_open_mtrl_alpha);
                } else {
                    mBtnDisplay.setImageResource(R.drawable.expander_close_mtrl_alpha);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(300);
        animator.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
