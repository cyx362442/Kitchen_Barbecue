package com.duowei.kitchen_barbecue.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

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
        baseViewHolder.setText(R.id.tv_num,cfpb_item.sl1+cfpb_item.dw);
        if(cfpb_item.isSelect==true){
            baseViewHolder.setBackgroundRes(R.id.linearLayout,R.drawable.shape_square_green);
        }else{
            baseViewHolder.setBackgroundRes(R.id.linearLayout,R.drawable.shape_square_table);
        }
        //超时单品
        if(!TextUtils.isEmpty(cfpb_item.cssj)&&cfpb_item.fzs>Integer.parseInt(cfpb_item.cssj)){
            baseViewHolder.setVisible(R.id.tv_over,true);
            baseViewHolder.setText(R.id.tv_over,"超时"+(cfpb_item.fzs-Integer.parseInt(cfpb_item.cssj))+"分");
        }else{
            baseViewHolder.setVisible(R.id.tv_over,false);
        }
    }
}
