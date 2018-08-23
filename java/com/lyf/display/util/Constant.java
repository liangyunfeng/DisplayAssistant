package com.lyf.display.util;

/**
 * Created by lyf on 2018/2/26.
 */

public class Constant {

    public final static String SCREEN_WAKE_UP_SERVICE_ACTION_STOP = "com.lyf.display.service.SCREEN_WAKE_UP_SERVICE_ACTION_STOP";
    public final static String SCREEN_WAKE_UP_SERVICE_ACTION_START = "com.lyf.display.service.SCREEN_WAKE_UP_SERVICE_ACTION_START";
    public final static String SCREEN_LOCK_SERVICE_ACTION_STOP = "com.lyf.display.service.SCREEN_LOCK_SERVICE_ACTION_STOP";
    public final static String SCREEN_LOCK_SERVICE_ACTION_START = "com.lyf.display.service.SCREEN_LOCK_SERVICE_ACTION_START";
    public final static String BLUE_LIGHT_FILTER_SERVICE_ACTION = "com.lyf.display.service.BLUE_LIGHT_FILTER_SERVICE_ACTION";

    public final static int FLOATING_TYPE_WAKEUP = 0;
    public final static int FLOATING_TYPE_LOCK = 1;

    public static final int REQUEST_CODE_APP_PERMISSION = 1022;

    public final static int SCREEN_WAKEUP_NOTIFICATION_ID = 10001;
    public final static int SCREEN_LOCK_NOTIFICATION_ID = 10002;
    public final static int BLUE_LIGHT_FILTER_NOTIFICATION_ID = 10002;

    public final static int SCREEN_WAKEUP_PENDINGINTENT_ID = 11;
    public final static int SCREEN_LOCK_PENDINGINTENT_ID = 12;
    public final static int BLUE_LIGHT_FILTER_PENDINGINTENT_ID = 12;

    public static final int START_ANIMATION = 1;
    public static final int BORDER_ANIMATION_DURATION = 200;

    public final static String BLUE_LIGHT_FILTER_ENABLE = "enable_blue_light_filter";
}
