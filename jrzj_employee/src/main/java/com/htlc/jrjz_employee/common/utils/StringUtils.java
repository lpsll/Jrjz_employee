package com.htlc.jrjz_employee.common.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by John_Libo on 2016/8/15.
 */
public class StringUtils {
    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    public static String getCurTimeStr() {
        Calendar cal = Calendar.getInstance();
        String curDate = dateFormater.get().format(cal.getTime());
        return curDate;
    }

    /***
     * 计算两个时间差，返回的是的秒s
     *
     * @param dete1
     * @param date2
     * @return
     */
    public static long calDateDifferent(String dete1, String date2) {

        long diff = 0;

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = dateFormater.get().parse(dete1);
            d2 = dateFormater.get().parse(date2);

            // 毫秒ms
            diff = d2.getTime() - d1.getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return diff / 1000;
    }

    /**
     * 如果用户输入的金额末尾是“.”，就自动补成“.00”
     *
     * @param str
     * @return
     */
    public static String addTwoZero(String str) {
        if (str.endsWith(".")) {
            str += "00";
        }
        return str;
    }

    /**
     * 获取4位随机整数
     *
     * @return
     */
    public static int getRandom() {
        int x;//定义两变量
        Random ne = new Random();//实例化一个random的对象ne
        x = ne.nextInt(9999 - 1000 + 1) + 1000;//为变量赋随机值1000-9999
        return x;
    }

    /**
     * 计算两个double的和
     * @param v1
     * @param v2
     * @return
     */
    public static String add(String v1, String v2) {

        BigDecimal b1 = new BigDecimal(v1);

        BigDecimal b2 = new BigDecimal(v2);

        return b1.add(b2).toString();
    }


    public static double add(double v1, double v2) {

        BigDecimal b1 = new BigDecimal(Double.toString(v1));

        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.add(b2).doubleValue();
    }

}
