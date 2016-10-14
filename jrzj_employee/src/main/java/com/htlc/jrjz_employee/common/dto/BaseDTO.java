package com.htlc.jrjz_employee.common.dto;


import com.htlc.jrjz_employee.common.utils.LogUtils;
import com.htlc.jrjz_employee.common.utils.SecurityUtils;

import java.io.Serializable;

/**
 * DTO的基类
 */

public class BaseDTO implements Serializable {
    public String uid;
    public long timestamp;
    public String random;
    public String sign;
    public String accessToken;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }



    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        LogUtils.e("setSign--",""+ SecurityUtils.md5(sign));
        this.sign = SecurityUtils.md5(sign);
    }


}
