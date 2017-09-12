package com.duowei.kitchen_barbecue.event;

import com.duowei.kitchen_barbecue.bean.Cfpb;

import java.util.List;

/**
 * Created by Administrator on 2017-09-04.
 */

public class Order {
    private List<Cfpb> listCfpb;

    public Order(List<Cfpb> listCfpb) {
        this.listCfpb = listCfpb;
    }

    public List<Cfpb> getListCfpb() {
        return listCfpb;
    }
}
