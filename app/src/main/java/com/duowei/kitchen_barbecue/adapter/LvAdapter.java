package com.duowei.kitchen_barbecue.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.duowei.kitchen_barbecue.R;
import com.duowei.kitchen_barbecue.bean.Cfpb_complete;

import java.util.List;

/**
 * Created by Administrator on 2017-09-14.
 */

public class LvAdapter extends BaseAdapter {
    private List<Cfpb_complete> list;
    private final LayoutInflater mLayoutInflater;

    public LvAdapter(Context context, List<Cfpb_complete> list) {
        this.list = list;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHold hold;
        if(convertView==null){
            hold=new ViewHold();
            convertView = mLayoutInflater.inflate(R.layout.lv_item, null);
            hold.tvItem=convertView.findViewById(R.id.tv_item);
            convertView.setTag(hold);
        }else{
            hold= (ViewHold) convertView.getTag();
        }
        if(position==0){
            hold.tvItem.setText("全部");
        }else{
            hold.tvItem.setText(list.get(position).getXmmc());
        }
        return convertView;
    }
    class ViewHold{
        private TextView tvItem;
    }
}
