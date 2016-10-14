package com.htlc.jrjz_employee.dto;

import com.htlc.jrjz_employee.common.utils.SecurityUtils;

/**
 * Created by Administrator on 2016/8/30.
 * 进行中的订单请求接口
 * <p/>
 * accessToken:访问授权码
 * <p/>
 * uid:用户ID，默认为手机号码
 * <p/>
 * timestamp:当前时间戳
 * <p/>
 * random:随机数
 * <p/>
 * sign:签名【生成规则uid+timestamp+random 后md5加密串】
 * <p/>
 * page:指定页数
 * <p/>
 * size:每页条数
 */
public class OrderBookedDTO {

    private String accessToken;
    private String uid;
    private long timestamp;
    private String random;
    private String sign;
    private String page;
    private String size;


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

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
