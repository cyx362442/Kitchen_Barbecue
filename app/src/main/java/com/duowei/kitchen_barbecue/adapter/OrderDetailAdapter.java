package com.duowei.kitchen_barbecue.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.duowei.kitchen_barbecue.R;
import com.duowei.kitchen_barbecue.bean.Cfpb_item;

import java.util.List;

/**
 * Created by Administrator on 2017-09-07.
 */

public class OrderDetailAdapter extends BaseQuickAdapter<Cfpb_item>{

    public OrderDetailAdapter(List<Cfpb_item> data) {
        super(R.layout.order_detail_item, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Cfpb_item cfpb_item) {
        baseViewHolder.setText(R.id.tv_zh,cfpb_item.czmc1);
        baseViewHolder.setText(R.id.tv_time,cfpb_item.fzs+"分钟");
        baseViewHolder.setText(R.id.tv_num,cfpb_item.sl1+"份");
        if(cfpb_item.isSelect==true){
            baseViewHolder.setBackgroundRes(R.id.linearLayout,R.drawable.shape_square_green);
        }else{
            baseViewHolder.setBackgroundRes(R.id.linearLayout,R.drawable.shape_square_table);
        }
    }
}
