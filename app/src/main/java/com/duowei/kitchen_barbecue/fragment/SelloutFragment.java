package com.duowei.kitchen_barbecue.fragment;


import android.app.Fragment;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.duowei.kitchen_barbecue.R;
import com.duowei.kitchen_barbecue.adapter.SellOutAdapter;
import com.duowei.kitchen_barbecue.bean.Jyxmsz;
import com.duowei.kitchen_barbecue.event.StartAnim;
import com.duowei.kitchen_barbecue.event.StopAnim;
import com.duowei.kitchen_barbecue.httputils.DownHTTP;
import com.duowei.kitchen_barbecue.httputils.Net;
import com.duowei.kitchen_barbecue.httputils.VolleyResultListener;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelloutFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemClickListener{
    List<Jyxmsz> listJyxmsz;
    private List<Jyxmsz>newList=new ArrayList<>();
    private SellOutAdapter mAdapter;
    private ListView mLv;
    private Jyxmsz mJyxmsz;
    private String mUpdateJyxmsz;
    private String mUpdateTbrq;
    public SelloutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_sellout, container, false);
        listJyxmsz=new ArrayList<>();
        TextView tvTop = (TextView) inflate.findViewById(R.id.tv_top);
        tvTop.setText("己沽清单品");
        mLv = (ListView) inflate.findViewById(R.id.listView);
        mAdapter = new SellOutAdapter(getActivity(), listJyxmsz);
        mLv.setAdapter(mAdapter);
        mLv.setOnItemClickListener(this);

        inflate.findViewById(R.id.btn_cancelall).setOnClickListener(this);
        inflate.findViewById(R.id.btn_cancel).setOnClickListener(this);
        return inflate;
    }

    public void notifyAdapter(List<Jyxmsz>listJyxmsz){
        this.listJyxmsz=listJyxmsz;
        if(listJyxmsz.size()>0){
            mJyxmsz=listJyxmsz.get(0);
        }
        mAdapter.setList(listJyxmsz);
        mAdapter.notifyDataSetChanged();
    }

    public void addGuqing(Jyxmsz jyxmsz){
        newList.clear();
        newList.add(jyxmsz);
        listJyxmsz= DataSupport.where("gq=?", "1").find(Jyxmsz.class);
        newList.addAll(listJyxmsz);
        mAdapter.setList(listJyxmsz=newList);
        mAdapter.notifyDataSetChanged();
        jyxmsz.setGq("1");
        jyxmsz.updateAll("xmbh=?",jyxmsz.getXmbh());
        mJyxmsz=listJyxmsz.get(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_cancel:
                if(listJyxmsz.size()<=0){
                    return;
                }
                Http_guqing();
                break;
            case R.id.btn_cancelall:
                if(listJyxmsz.size()<=0){
                    return;
                }
                Http_guqingAll();
                break;
        }
    }

    private void Http_guqingAll() {
        EventBus.getDefault().post(new StartAnim());
        mUpdateJyxmsz = "update jyxmsz set gq=''|";
        mUpdateTbrq = "update tbsj set tbrq=getdate() where tablename='jyxmsz'|";
        DownHTTP.postVolley7(Net.url, mUpdateJyxmsz+mUpdateTbrq, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                EventBus.getDefault().post(new StopAnim());
            }
            @Override
            public void onResponse(String response) {
                if(response.contains("richado")){
                    ContentValues values = new ContentValues();
                    values.put("gq","");
                    DataSupport.updateAll(Jyxmsz.class,values);
                    listJyxmsz.clear();
                    mAdapter.setList(listJyxmsz);
                    mAdapter.notifyDataSetChanged();
                    EventBus.getDefault().post(new StopAnim());
                }
            }
        });
    }

    private void Http_guqing() {
        EventBus.getDefault().post(new StartAnim());
        mUpdateJyxmsz="update jyxmsz set gq='' where xmbh='"+mJyxmsz.getXmbh()+"'|";
        mUpdateTbrq = "update tbsj set tbrq=getdate() where tablename='jyxmsz'|";
        DownHTTP.postVolley7(Net.url, mUpdateJyxmsz+mUpdateTbrq, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                EventBus.getDefault().post(new StopAnim());
            }
            @Override
            public void onResponse(String response) {
                if(response.contains("richado")){
                    listJyxmsz.remove(mJyxmsz);
                    if(listJyxmsz.size()>0){
                        mJyxmsz=listJyxmsz.get(0);
                        mAdapter.setIndex(0);
                        mLv.setSelection(0);
                    }
                    mAdapter.notifyDataSetChanged();
                    EventBus.getDefault().post(new StopAnim());
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mJyxmsz = listJyxmsz.get(i);
        mAdapter.setIndex(i);
        mAdapter.notifyDataSetChanged();
    }
}
