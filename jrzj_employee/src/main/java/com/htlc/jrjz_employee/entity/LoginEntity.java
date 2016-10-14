package com.htlc.jrjz_employee.entity;


import com.htlc.jrjz_employee.common.entity.BaseEntity;

/**
 * Created by Administrator on 2016/8/24.
 */
public class LoginEntity extends BaseEntity {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
