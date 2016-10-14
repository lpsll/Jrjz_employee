package com.htlc.jrjz_employee;

import android.content.Context;
import android.content.SharedPreferences;

import com.baidu.mapapi.SDKInitializer;
import com.htlc.jrjz_employee.common.base.BaseApplication;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by John_Libo on 2016/8/15.
 */
public class AppContext extends BaseApplication {
    private static AppContext instance;
    private SharedPreferences.Editor editor;
    private SharedPreferences sp;
    /**
     * 清单文件名称
     */
    public static final String SP_NAME = "jrjz_app";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        this.sp = this.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        this.editor = this.sp.edit();

        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);


    }


    /**
     * 获得当前app运行的AppContext
     *
     * @return
     */
    public static AppContext getInstance() {
        return instance;
    }


}
