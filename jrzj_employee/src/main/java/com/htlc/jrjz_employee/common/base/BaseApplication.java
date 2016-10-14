package com.htlc.jrjz_employee.common.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.DisplayMetrics;

import com.htlc.jrjz_employee.common.cache.DiskLruCacheHelper;
import com.htlc.jrjz_employee.common.utils.StringUtils;

import java.io.IOException;

/**
 * Created by John_Libo on 2016/8/15.
 */
public class BaseApplication extends Application {
    private static String PREF_NAME = "jr_app.pref";
    private static String LAST_REFRESH_TIME = "last_refresh_time.pref";
    static Context _context;
    static DiskLruCacheHelper helper;
    private static boolean sIsAtLeastGB;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            sIsAtLeastGB = true;
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        _context = getApplicationContext();
//        initImageLoader(this);
        try {
            helper=new DiskLruCacheHelper(_context);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }


    public static synchronized BaseApplication context() {
        return (BaseApplication) _context;
    }

    public static synchronized DiskLruCacheHelper helper() {
        return helper;
    }

    /**
     * 记录列表上次刷新时间
     */
    public static void putToLastRefreshTime(String key, String value) {
        SharedPreferences preferences = getPreferences(LAST_REFRESH_TIME);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        apply(editor);
    }

    /**
     * 获取列表的上次刷新时间
     *
     * @param key
     * @return
     */
    public static String getLastRefreshTime(String key) {
        return getPreferences(LAST_REFRESH_TIME).getString(key, StringUtils.getCurTimeStr());
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void apply(SharedPreferences.Editor editor) {
        if (sIsAtLeastGB) {
            editor.apply();
        } else {
            editor.commit();
        }
    }


    public static void set(String key, int value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(key, value);
        apply(editor);
    }

    public static void set(String key, boolean value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(key, value);
        apply(editor);
    }

    public static void set(String key, String value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(key, value);
        apply(editor);
    }

    public static boolean get(String key, boolean defValue) {
        return getPreferences().getBoolean(key, defValue);
    }

    public static String get(String key, String defValue) {
        return getPreferences().getString(key, defValue);
    }

    public static int get(String key, int defValue) {
        return getPreferences().getInt(key, defValue);
    }

    public static long get(String key, long defValue) {
        return getPreferences().getLong(key, defValue);
    }

    public static float get(String key, float defValue) {
        return getPreferences().getFloat(key, defValue);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static SharedPreferences getPreferences() {
        SharedPreferences pre = context().getSharedPreferences(PREF_NAME,
                Context.MODE_MULTI_PROCESS);
        return pre;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static SharedPreferences getPreferences(String prefName) {
        return context().getSharedPreferences(prefName,
                Context.MODE_MULTI_PROCESS);
    }

    public static int[] getDisplaySize() {
        return new int[]{getPreferences().getInt("screen_width", 480),
                getPreferences().getInt("screen_height", 854)};
    }

    public static void saveDisplaySize(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt("screen_width", displaymetrics.widthPixels);
        editor.putInt("screen_height", displaymetrics.heightPixels);
        editor.putFloat("density", displaymetrics.density);
        editor.commit();
    }

    /**
     * 初始化加载图片工具类
     *
     * @param context
     *            上下文对象
     */
//    private void initImageLoader(Context context) {
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//                context).threadPriority(Thread.NORM_PRIORITY - 2)
//                .denyCacheImageMultipleSizesInMemory()
//                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
//                .diskCacheSize(50*1024*1024)
//                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .writeDebugLogs()
//                // Remove for release appLruMemoryCache
//                .memoryCache(new LruMemoryCache(4 * 1024 * 1024))
//                .memoryCacheSize(4 * 1024 * 1024).build();
//        // Initialize ImageLoader with configuration.
//        ImageLoader.getInstance().init(config);
//    }
}
