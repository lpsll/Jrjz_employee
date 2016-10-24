package com.htlc.jrjz_employee.entity;

/**
 * Created by John_Libo on 2016/9/12.
 */
public class OrderEvent {
    private String msg;
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public OrderEvent(String msg) {
        super();
        this.msg = msg;
    }

}
