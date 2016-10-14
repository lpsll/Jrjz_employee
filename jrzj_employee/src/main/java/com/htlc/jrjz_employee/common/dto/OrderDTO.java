package com.htlc.jrjz_employee.common.dto;

/**
 * Created by John_Libo on 2016/8/30.
 */
public class OrderDTO extends BaseDTO{
    private String page;
    private String size;

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
