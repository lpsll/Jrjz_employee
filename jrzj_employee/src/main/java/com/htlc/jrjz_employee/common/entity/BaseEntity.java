package com.htlc.jrjz_employee.common.entity;

import java.io.Serializable;

/**
 实体类基类
 */
public class BaseEntity implements Serializable {
    private String code;
    private String msg;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }



}
