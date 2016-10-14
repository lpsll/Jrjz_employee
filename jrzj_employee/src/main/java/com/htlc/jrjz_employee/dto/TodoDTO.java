package com.htlc.jrjz_employee.dto;

import com.htlc.jrjz_employee.common.utils.SecurityUtils;

/**
 * Created by Administrator on 2016/9/1.


 */
public class TodoDTO {

    private String accessToken;
    private String uid;
    private String timestamp;
    private String random;
    private String orderId;
    private String sign;


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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = SecurityUtils.md5(sign);
    }
}
