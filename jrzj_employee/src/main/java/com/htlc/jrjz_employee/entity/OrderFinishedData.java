package com.htlc.jrjz_employee.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/8/31.
 */
public class OrderFinishedData {

    private String page;
    private String pageCount;
    private String total;
    List<OrderFinishedEntity> data;


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

    public List<OrderFinishedEntity> getData() {
        return data;
    }

    public void setData(List<OrderFinishedEntity> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "OrderFinishedData{" +
                "page='" + page + '\'' +
                ", pageCount='" + pageCount + '\'' +
                ", total='" + total + '\'' +
                ", data=" + data +
                '}';
    }
}
