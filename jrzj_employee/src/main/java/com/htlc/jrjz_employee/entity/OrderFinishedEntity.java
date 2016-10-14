package com.htlc.jrjz_employee.entity;

/**
 * Created by Administrator on 2016/8/31.
 *  "paidAmount": ""
 */
public class OrderFinishedEntity extends OrderBookedEntity {


    private String paidAmount;

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }
}
