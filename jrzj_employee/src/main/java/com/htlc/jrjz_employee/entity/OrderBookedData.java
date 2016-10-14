package com.htlc.jrjz_employee.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/8/31.
 */
public class OrderBookedData {



    private String page;
    private String pageCount;
    private String total;
    List<OrderBookedEntity> data;

    public List<OrderBookedEntity> getData() {
        return data;
    }

    public void setData(List<OrderBookedEntity> data) {
        this.data = data;
    }


    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPageCount() {
        return pageCount;
    }

    public void setPageCount(String pageCount) {
        this.pageCount = pageCount;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }


    @Override
    public String toString() {
        return "OrderBookedData{" +
                "page='" + page + '\'' +
                ", pageCount='" + pageCount + '\'' +
                ", total='" + total + '\'' +
                ", data=" + data +
                '}';
    }
}
