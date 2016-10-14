package com.htlc.jrjz_employee.common.eventbus;

/**
 * Created by John_Libo on 2016/8/15.
 */
public class DefEvent {
    private boolean status;
    private Object tag;

    public DefEvent(boolean status, Object tag) {
        super();
        this.status = status;
        this.tag = tag;
    }

    public DefEvent(boolean status) {
        super();
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

}
