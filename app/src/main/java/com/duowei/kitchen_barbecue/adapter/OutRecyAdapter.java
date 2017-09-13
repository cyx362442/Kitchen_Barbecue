package com.duowei.kitchen_barbecue.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.duowei.kitchen_barbecue.R;
import com.duowei.kitchen_barbecue.bean.Cfpb;
import com.duowei.kitchen_barbecue.event.CountFood;
import com.duowei.kitchen_barbecue.event.OutItem;


import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Administrator on 2017-09-12.
 */

public class OutRecyAdapter extends BaseQuickAdapter<Cfpb>{
    public OutRecyAdapter(List<Cfpb> data) {
        super(R.layout.rv_out_item,data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final Cfpb cfpb) {
        baseViewHolder.setText(R.id.tv_name,cfpb.getXmmc());
        baseViewHolder.setText(R.id.tv_zh,cfpb.getCzmc());
        baseViewHolder.setText(R.id.tv_num,cfpb.getYwcsl()+"");
        baseViewHolder.setText(R.id.tv_dw,cfpb.getDw());
        baseViewHolder.setOnClickListener(R.id.tv_dw, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new OutItem(cfpb));
                EventBus.getDefault().post(new CountFood());
            }
        });
    }
}
