package com.duowei.kitchen_barbecue.event;

/**
 * Created by Administrator on 2017-09-13.
 */

public class OutTime {
    private boolean isOutTime;

    public OutTime(boolean isOutTime) {
        this.isOutTime = isOutTime;
    }

    public boolean isOutTime() {
        return isOutTime;
    }
}
