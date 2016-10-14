package com.htlc.jrjz_employee.entity;

import com.htlc.jrjz_employee.common.entity.BaseEntity;

/**
 * Created by Administrator on 2016/8/30.
 * 进行中订单返回的结果
 */
public class OrderBookedResult extends BaseEntity{

    OrderBookedData data;

    public OrderBookedData getData() {
        return data;
    }

    public void setData(OrderBookedData data) {
        this.data = data;
    }



}
