package com.duowei.kitchen_barbecue.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.duowei.kitchen_barbecue.R;
import com.duowei.kitchen_barbecue.bean.Cfpb;
import com.duowei.kitchen_barbecue.bean.Cfpb_item;

import java.util.List;

/**
 * Created by Administrator on 2017-09-04.
 */

public class MainRecyAdapter extends BaseQuickAdapter<Cfpb> {

    public MainRecyAdapter(List<Cfpb> data) {
        super(R.layout.recycleview_order_item, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Cfpb cfpb) {
        baseViewHolder.setText(R.id.tv_name,cfpb.getXmmc());
        baseViewHolder.setText(R.id.tv_pz,cfpb.getPz());

        float count=0;
        List<Cfpb_item> listCfpb = cfpb.getListCfpb();
        for(Cfpb_item cfpbitem:listCfpb){
            count+=cfpbitem.sl1;
        }
        baseViewHolder.setText(R.id.tv_num,count+cfpb.getDw());

        //超时单品
        String cssj = cfpb.getCssj();
        if(!TextUtils.isEmpty(cssj)&&cfpb.getFzs()>Integer.parseInt(cssj)){
            baseViewHolder.setBackgroundRes(R.id.linearLayout,R.drawable.shape_contiune_outtime);
        }else{
            baseViewHolder.setBackgroundRes(R.id.linearLayout,R.drawable.shape_order_green);
        }
    }
}
