package com.htlc.jrjz_employee.common.cache;

import android.content.Context;

import com.htlc.jrjz_employee.AppContext;
import com.htlc.jrjz_employee.common.utils.TDevice;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class CacheManager {

    // wifi缓存时间为5分钟
    private static long wifi_cache_time = 5 * 60 * 1000;
    // 其他网络环境为1小时
    private static long other_cache_time = 60 * 60 * 1000;

    /**
     * 保存对象
     *
     * @param ser
     * @param file
     */
    public static void saveObject(Serializable ser,
                                  String file) {
        AppContext.helper().put(file, ser);
    }

    /**
     * 读取对象
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static Serializable readObject(String file) {
        return AppContext.helper().getAsSerializable(file);
    }


    /**
     * 一个key可能对应多个缓存文件，这里使用时限定只对应一个
     * @param cachefile
     * @return
     */
    public static boolean isExistDataCache(String cachefile){
        File data = AppContext.helper().getCacheFile(cachefile);
        if (data!=null&&data.exists()) {
            return true;
        }
        return false;
    }

    /**
     * 判断缓存是否已经失效 true失效，false未失效
     */
    public static boolean isCacheDataFailure(Context context, String cachefile) {
        File data = AppContext.helper().getCacheFile(cachefile);
        if (data==null||!data.exists()) {
            return true;
        }
        long existTime = System.currentTimeMillis() - data.lastModified();
        boolean failure = false;
        if (TDevice.getNetworkType(context) == TDevice.NETTYPE_WIFI) {
            failure = existTime > wifi_cache_time ? true : false;
        } else {
            failure = existTime > other_cache_time ? true : false;
        }
        return failure;
    }
}
