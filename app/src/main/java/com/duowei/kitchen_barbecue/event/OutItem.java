package com.duowei.kitchen_barbecue.event;

import com.duowei.kitchen_barbecue.bean.Cfpb;

/**
 * Created by Administrator on 2017-09-12.
 */

public class OutItem {
    private Cfpb mCfpb;

    public OutItem(Cfpb cfpb) {
        mCfpb = cfpb;
    }

    public Cfpb getCfpb() {
        return mCfpb;
    }
}
