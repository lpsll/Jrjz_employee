package com.htlc.jrjz_employee.entity;

import com.htlc.jrjz_employee.common.entity.BaseEntity;

/**
 * Created by Administrator on 2016/8/30.
 * 历史订单返回的数据
 */
public class OrderFinishedResult extends BaseEntity{

    OrderFinishedData data;

    public OrderFinishedData getData() {
        return data;
    }

    public void setData(OrderFinishedData data) {
        this.data = data;
    }
}
