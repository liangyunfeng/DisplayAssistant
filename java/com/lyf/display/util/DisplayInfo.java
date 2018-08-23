package com.lyf.display.util;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by lyf on 2018/2/25.
 */

public class DisplayInfo {

    private DisplayMetrics mDisplayMetrics = new DisplayMetrics();
    private int widthPixels;
    private int heightPixels;
    private float density;
    private float densityDpi;
    private float scaledDensity;
    private float xdpi;
    private float ydpi;
    private String mDisplayDpi;
    private double mDisplaySize;
    private String mDisplayType;

    public DisplayInfo(Activity activity) {
//      DisplayMetrics dm = new DisplayMetrics();
//      dm = getResources().getDisplayMetrics();
//
//      float density  = dm.density;        // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
//      int densityDpi = dm.densityDpi;     // 屏幕密度（每寸像素：120/160/240/320）
//      float xdpi = dm.xdpi;
//      float ydpi = dm.ydpi;
//      int screenWidth  = dm.widthPixels;      // 屏幕宽（像素，如：480px）
//      int screenHeight = dm.heightPixels;     // 屏幕高（像素，如：800px）

        activity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        widthPixels = mDisplayMetrics.widthPixels;  // 屏幕宽度（像素）
        heightPixels = mDisplayMetrics.heightPixels;  // 屏幕高度（像素）
        density = mDisplayMetrics.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        densityDpi = mDisplayMetrics.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
        scaledDensity = mDisplayMetrics.scaledDensity;
        xdpi = mDisplayMetrics.xdpi;
        ydpi = mDisplayMetrics.ydpi;
        mDisplayDpi = getDeviceDpi();
        mDisplaySize = getDeviceSize();
        mDisplayType = getDeviceType();
    }

    public int getDeviceWidthPixels() {
        return widthPixels;
    }

    public int getDeviceHeightPixels() {
        return heightPixels;
    }

    public float getDeviceDensity() {
        return density;
    }

    public float getDeviceDensityDpi() {
        return densityDpi;
    }

    public float getDeviceXDpi() {
        return xdpi;
    }

    public float getDeviceYDpi() {
        return ydpi;
    }

    public String getDeviceDpi() {
        int min = widthPixels < heightPixels ? widthPixels : heightPixels;
        int swXXXdp = (int) (min * (160f / (float) densityDpi));

        String dpi = "No match.";

        if (densityDpi >= DisplayMetrics.DENSITY_LOW && densityDpi < DisplayMetrics.DENSITY_DEFAULT) {
            dpi = "ldpi";
        } else if (densityDpi >= DisplayMetrics.DENSITY_DEFAULT && densityDpi < DisplayMetrics.DENSITY_HIGH) {
            dpi = "mdpi";
        } else if (densityDpi >= DisplayMetrics.DENSITY_HIGH && densityDpi < DisplayMetrics.DENSITY_XHIGH) {
            dpi = "hdpi";
        } else if (densityDpi >= DisplayMetrics.DENSITY_XHIGH && densityDpi < DisplayMetrics.DENSITY_XXHIGH) {
            dpi = "xhdpi";
        } else if (densityDpi >= DisplayMetrics.DENSITY_XXHIGH && densityDpi < DisplayMetrics.DENSITY_XXXHIGH) {
            dpi = "xxhdpi";
        } else if (densityDpi >= DisplayMetrics.DENSITY_XXXHIGH) {
            dpi = "xxxhdpi";
        }

        return "sw" + swXXXdp + "dp-" + dpi;
    }

    public String getDeviceType() {
        String pixels = "No match.";
        int screenWidth = widthPixels;
        int screenHeight = heightPixels;

        if ((screenWidth == 160 && screenHeight == 120)
                || (screenWidth == 120 && screenHeight == 160)) {
            pixels = "QQVGA";
        } else if ((screenWidth == 320 && screenHeight == 240)
                || (screenWidth == 240 && screenHeight == 320)) {
            pixels = "QVGA";
        } else if ((screenWidth == 400 && screenHeight == 240)
                || (screenWidth == 240 && screenHeight == 400)
                || (screenWidth == 480 && screenHeight == 272)
                || (screenWidth == 272 && screenHeight == 480)) {
            pixels = "WQVGA";
        } else if ((screenWidth == 480 && screenHeight == 320)
                || (screenWidth == 320 && screenHeight == 480)) {
            pixels = "HVGA";
        } else if ((screenWidth == 640 && screenHeight == 480)
                || (screenWidth == 480 && screenHeight == 640)) {
            pixels = "VGA";
        } else if ((screenWidth == 800 && screenHeight == 480)
                || (screenWidth == 480 && screenHeight == 800)) {
            pixels = "WVGA";
        } else if ((screenWidth == 854 && screenHeight == 480)
                || (screenWidth == 480 && screenHeight == 854)) {
            pixels = "FWVGA";
        } else if ((screenWidth == 800 && screenHeight == 600)
                || (screenWidth == 600 && screenHeight == 800)) {
            pixels = "SVGA";
        } else if ((screenWidth == 1024 && screenHeight == 600)
                || (screenWidth == 600 && screenHeight == 1024)
                || (screenWidth == 1280 && screenHeight == 600)
                || (screenWidth == 600 && screenHeight == 1280)) {
            pixels = "SVGA+";
        } else if ((screenWidth == 1024 && screenHeight == 768)
                || (screenWidth == 768 && screenHeight == 1024)) {
            pixels = "XGA";
        } else if ((screenWidth == 1280 && screenHeight == 768)
                || (screenWidth == 768 && screenHeight == 1280)
                || (screenWidth == 1280 && screenHeight == 800)
                || (screenWidth == 800 && screenHeight == 1280)
                || (screenWidth == 1366 && screenHeight == 768)
                || (screenWidth == 768 && screenHeight == 1366)) {
            pixels = "WXGA";
        } else if ((screenWidth == 1280 && screenHeight == 854)
                || (screenWidth == 854 && screenHeight == 1280)) {
            pixels = "WSXGA";
        } else if ((screenWidth == 1280 && screenHeight == 960)
                || (screenWidth == 960 && screenHeight == 1280)) {
            pixels = "Quad-VGA";
        } else if ((screenWidth == 1440 && screenHeight == 900)
                || (screenWidth == 900 && screenHeight == 1440)) {
            pixels = "WXGA+";
        } else if ((screenWidth == 1280 && screenHeight == 1024)
                || (screenWidth == 1024 && screenHeight == 1280)) {
            pixels = "SXGA";
        } else if ((screenWidth == 1600 && screenHeight == 900)
                || (screenWidth == 900 && screenHeight == 1600)) {
            pixels = "WXGA++";
        } else if ((screenWidth == 1400 && screenHeight == 1050)
                || (screenWidth == 1050 && screenHeight == 1400)) {
            pixels = "SXGA+";
        } else if ((screenWidth == 1680 && screenHeight == 1050)
                || (screenWidth == 1050 && screenHeight == 1680)) {
            pixels = "WSXGA+";
        } else if ((screenWidth == 1600 && screenHeight == 1200)
                || (screenWidth == 1200 && screenHeight == 1600)) {
            pixels = "UXGA";
        } else if ((screenWidth == 1920 && screenHeight == 1200)
                || (screenWidth == 1200 && screenHeight == 1920)) {
            pixels = "WUXGA";
        } else if ((screenWidth == 2048 && screenHeight == 1536)
                || (screenWidth == 1536 && screenHeight == 2048)) {
            pixels = "QXGA";
        } else if ((screenWidth == 2560 && screenHeight == 1600)
                || (screenWidth == 1600 && screenHeight == 2560)) {
            pixels = "WQXGA";
        } else if ((screenWidth == 2560 && screenHeight == 2048)
                || (screenWidth == 2048 && screenHeight == 2560)) {
            pixels = "QSXGA";
        } else if ((screenWidth == 3200 && screenHeight == 2400)
                || (screenWidth == 2400 && screenHeight == 3200)) {
            pixels = "QUXGA";
        } else if ((screenWidth == 4094 && screenHeight == 2048)
                || (screenWidth == 2048 && screenHeight == 4094)) {
            pixels = "4K2K";
        } else if ((screenWidth == 4094 && screenHeight == 4094)
                || (screenWidth == 4094 && screenHeight == 4094)) {
            pixels = "4K4K";
        } else if ((screenWidth == 960 && screenHeight == 540)
                || (screenWidth == 540 && screenHeight == 960)) {
            pixels = "qHD";
        } else if ((screenWidth == 1280 && screenHeight == 720)
                || (screenWidth == 720 && screenHeight == 1280)) {
            pixels = "HD";
        } else if ((screenWidth == 1920 && screenHeight == 1080)
                || (screenWidth == 1080 && screenHeight == 1920)) {
            pixels = "FHD";
        } else if ((screenWidth == 2560 && screenHeight == 1440)
                || (screenWidth == 1440 && screenHeight == 2560)) {
            pixels = "QHD";
        } else if ((screenWidth == 3840 && screenHeight == 2160)
                || (screenWidth == 2160 && screenHeight == 3840)) {
            pixels = "UHD";
        }

        return pixels;
    }

    public double getDeviceSize() {
        float widthSize = widthPixels / xdpi;
        float heightSize = heightPixels / ydpi;
        double size = Math.sqrt((widthSize * widthSize) + (heightSize * heightSize));

        Log.v("lyf", "size = " + size);

        return size;
    }

    public String getDisplayInfo() {
        StringBuilder result = new StringBuilder("");
        result.append("Screen Inches : " + mDisplaySize);
        result.append("\n");
        result.append("widthPixels = " + widthPixels);
        result.append("\n");
        result.append("heightPixels = " + heightPixels);
        result.append("\n");
        result.append("xdpi = " + xdpi);
        result.append("\n");
        result.append("ydpi = " + ydpi);
        result.append("\n");
        result.append("density = " + density);
        result.append("\n");
        result.append("scaledDensity = " + scaledDensity);
        result.append("\n");
        result.append("densityDpi = " + densityDpi);
        result.append("\n");
        result.append("\n");
        result.append("ScreenDpi = " + mDisplayDpi);
        result.append("\n");
        result.append("ScreenType = " + mDisplayType);

        return result.toString();
    }
}
