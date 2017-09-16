package com.duowei.kitchen_barbecue.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.duowei.kitchen_barbecue.R;
import com.duowei.kitchen_barbecue.app.MyApplication;
import com.duowei.kitchen_barbecue.bean.Cfpb;
import com.duowei.kitchen_barbecue.bean.Cfpb_item;
import com.duowei.kitchen_barbecue.tools.PreferenceUtils;

import java.util.List;

/**
 * Created by Administrator on 2017-09-04.
 */

public class MainRecyAdapter extends BaseQuickAdapter<Cfpb> {

    private final int mColor1;
    private final int mColor2;
    private final int mColor3;
    private final int mColor4;

    public MainRecyAdapter(List<Cfpb> data) {
        super(R.layout.recycleview_order_item, data);
        Context context = MyApplication.getContext();
        PreferenceUtils preferenceUtils = PreferenceUtils.getInstance(context);
        mColor1 = preferenceUtils.getColor1(context.getString(R.string.color1),1);
        mColor2 = preferenceUtils.getColor1(context.getString(R.string.color2), 9999999);
        mColor3 = preferenceUtils.getColor1(context.getString(R.string.color3), 99999999);
        mColor4 = preferenceUtils.getColor1(context.getString(R.string.color4), 999999999);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Cfpb cfpb) {
        baseViewHolder.setText(R.id.tv_name,cfpb.getXmmc());
        baseViewHolder.setText(R.id.tv_pz,cfpb.getPz().
                replaceAll("&lt;","<").replaceAll("&gt;",">"));

        float count=0;
        List<Cfpb_item> listCfpb = cfpb.getListCfpb();
        for(Cfpb_item cfpbitem:listCfpb){
            count+=cfpbitem.sl1;
        }
        baseViewHolder.setText(R.id.tv_num,count+cfpb.getDw());

        //超时单品
        if(!TextUtils.isEmpty(cfpb.getCssj())){
            int fzs = cfpb.getFzs();
            int cssj = Integer.parseInt(cfpb.getCssj());
            if((fzs-cssj)>=mColor1&&(fzs-cssj)<mColor2){
                baseViewHolder.setBackgroundRes(R.id.linearLayout,R.drawable.shape_outtime_yellow);
            }else if((fzs-cssj)>=mColor2&&(fzs-cssj)<mColor3){
                baseViewHolder.setBackgroundRes(R.id.linearLayout,R.drawable.shape_outtime_orange);
            }else if((fzs-cssj)>=mColor3&&(fzs-cssj)<mColor4){
                baseViewHolder.setBackgroundRes(R.id.linearLayout,R.drawable.shape_outtime_purple);
            }else if((fzs-cssj)>=mColor4){
                baseViewHolder.setBackgroundRes(R.id.linearLayout,R.drawable.shape_outtime_red);
            }else{
                baseViewHolder.setBackgroundRes(R.id.linearLayout,R.drawable.shape_order_green);
            }
        }else{
            baseViewHolder.setBackgroundRes(R.id.linearLayout,R.drawable.shape_order_green);
        }


        if("1".equals(cfpb.getBy10())){
            baseViewHolder.setTextColor(R.id.tv_name, Color.parseColor("#3F51B5"));
        }else{
            baseViewHolder.setTextColor(R.id.tv_name,Color.WHITE);
        }
    }
}
