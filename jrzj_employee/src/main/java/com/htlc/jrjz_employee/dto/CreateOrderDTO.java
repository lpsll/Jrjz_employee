package com.htlc.jrjz_employee.dto;

import com.htlc.jrjz_employee.common.utils.SecurityUtils;

/**
 * Created by Administrator on 2016/8/30.
 * 生成订单请求接口
 * "accessToken": "",
 * "uid": "",
 * "timestamp": "",
 * "random": "",
 * "orderId": 0,
 * "serviceFee": 0,
 * "materialFee": 0,
 * "otherFee": 0,
 * "totalFee": 0,
 * "sign": ""
 */
public class CreateOrderDTO {

    private String accessToken;
    private String uid;
    private long timestamp;
    private String random;
    private String orderId;
    private String serviceFee;
    private String materialFee;
    private String otherFee;
    private String totalFee;
    private String sign;


    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getMaterialFee() {
        return materialFee;
    }

    public void setMaterialFee(String materialFee) {
        this.materialFee = materialFee;
    }

    public String getOtherFee() {
        return otherFee;
    }

    public void setOtherFee(String otherFee) {
        this.otherFee = otherFee;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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
        this.sign = SecurityUtils.md5(sign);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
