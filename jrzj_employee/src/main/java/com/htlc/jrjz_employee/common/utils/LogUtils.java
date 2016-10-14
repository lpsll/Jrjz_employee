package com.htlc.jrjz_employee.common.utils;

import android.util.Log;

import com.htlc.jrjz_employee.AppConfig;


/**
 * Log调试工具
 *
 */
public class LogUtils {
    public static final String TAG = "tag";

    public static void i(String msg) {
        if (AppConfig.DEBUG) {
            Log.i(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (AppConfig.DEBUG) {
            Log.e(TAG, msg);
        }
    }

    public static void w(String msg) {
        if (AppConfig.DEBUG) {
            Log.w(TAG, msg);
        }
    }

    public static void d(String msg) {
        if (AppConfig.DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void v(String msg) {
        if (AppConfig.DEBUG) {
            Log.v(TAG, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (AppConfig.DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (AppConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (AppConfig.DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (AppConfig.DEBUG) {
            Log.v(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (AppConfig.DEBUG) {
            Log.w(tag, msg);
        }
    }
}
