package com.htlc.jrjz_employee.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/8/23.
 * 验证电话号码格式
 */
public class PhoneUtils {

    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;
        CharSequence inputStr = phoneNumber;
        //正则表达式

        String phone="^1[3456789]\\d{9}$" ;


        Pattern pattern = Pattern.compile(phone);
        Matcher matcher = pattern.matcher(inputStr);


        if(matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

}
