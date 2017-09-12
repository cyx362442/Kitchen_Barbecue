package com.duowei.kitchen_barbecue.event;

import com.duowei.kitchen_barbecue.bean.Cfpb;

import java.util.List;

/**
 * Created by Administrator on 2017-09-12.
 */

public class CompleteCfpb {
    private List<Cfpb> mList;

    public CompleteCfpb(List<Cfpb> list) {
        mList = list;
    }

    public List<Cfpb> getList() {
        return mList;
    }
}
