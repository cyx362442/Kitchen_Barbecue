package com.duowei.kitchen_barbecue.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.duowei.kitchen_barbecue.R;
import com.duowei.kitchen_barbecue.bean.Cfpb_complete;

import java.util.List;

/**
 * Created by Administrator on 2017-09-14.
 */

public class PassRecyAdapter extends BaseQuickAdapter<Cfpb_complete>{
    public PassRecyAdapter(List<Cfpb_complete> data) {
        super(R.layout.history_item,data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Cfpb_complete cfpb_complete) {
        baseViewHolder.setText(R.id.tv_index,(baseViewHolder.getPosition()+1)+"");
        baseViewHolder.setText(R.id.tv_name,cfpb_complete.getXmmc());
        baseViewHolder.setText(R.id.tv_zh,cfpb_complete.getZh());
        baseViewHolder.setText(R.id.tv_num,cfpb_complete.getSl()+"");
        baseViewHolder.setText(R.id.tv_pz,cfpb_complete.getPz());
        baseViewHolder.setText(R.id.tv_yhmc,cfpb_complete.getYhmc());
        baseViewHolder.setText(R.id.tv_xdsj,cfpb_complete.getXdsj());
        baseViewHolder.setText(R.id.tv_wcsj,cfpb_complete.getWcsj());
    }
}
