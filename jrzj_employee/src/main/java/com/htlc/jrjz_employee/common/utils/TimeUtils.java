package com.htlc.jrjz_employee.common.utils;

import java.util.Random;

/**
 * Created by John_Libo on 2016/8/23.
 */
public class TimeUtils {

    /**
    获取当前系统时间
     */
    public static long getSignTime() {
        long t = System.currentTimeMillis();
        return t;
    }

    /**
     * timeStamp时间戳
     */
    public static String genTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    /**
     * nonceStr随机数
     */
    public static String genNonceStr() {
        Random random = new Random();
        return String.valueOf(random.nextInt(10000));
    }

}
