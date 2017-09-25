package com.duowei.kitchen_barbecue.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.duowei.kitchen_barbecue.R;
import com.duowei.kitchen_barbecue.adapter.MainRecyAdapter;
import com.duowei.kitchen_barbecue.adapter.SpacesItemDecoration;
import com.duowei.kitchen_barbecue.bean.Cfpb;
import com.duowei.kitchen_barbecue.event.Order;
import com.duowei.kitchen_barbecue.event.OutTime;
import com.duowei.kitchen_barbecue.fragment.dialog.OrderDetailFragment;
import com.duowei.kitchen_barbecue.tools.PreferenceUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements BaseQuickAdapter.OnRecyclerViewItemClickListener {
    private List<Cfpb> listCfpb;
    private MainRecyAdapter mRecyAdapter;
    private ProgressBar mPb;
    private PreferenceUtils mPreferenceUtils;
    private RecyclerView mRv;
    public  boolean outTime=false;
    private List<Cfpb>listTemp;
    private View currentView;

    public MainFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_main, container, false);
        EventBus.getDefault().register(this);
        listCfpb=new ArrayList<>();
        listTemp=new ArrayList<>();

        mPreferenceUtils = PreferenceUtils.getInstance(getActivity());

        mPb = inflate.findViewById(R.id.pb);
        mRv = inflate.findViewById(R.id.rv);
        mRecyAdapter = new MainRecyAdapter(listCfpb);
        mRv.addItemDecoration(new SpacesItemDecoration(5));
        mRv.setItemAnimator(new DefaultItemAnimator());
        mRv.setAdapter(mRecyAdapter);
        mRecyAdapter.setOnRecyclerViewItemClickListener(this);
        return inflate;
    }

    @Override
    public void onStart() {
        super.onStart();
        String colums = mPreferenceUtils.getListColums(getString(R.string.columnnum), getString(R.string.four));
        int colum = Integer.parseInt(colums);
        mRv.setLayoutManager(new GridLayoutManager(getActivity(),colum));
    }

    @Subscribe
    public void getOrderList(Order event){
        listCfpb=event.getListCfpb();
        if(outTime==true){
            setOutListCfpb();
        }else{
            mRecyAdapter.setNewData(listCfpb);
        }
        mPb.setVisibility(View.GONE);
    }

    private void setOutListCfpb() {
        listTemp.clear();
        for(int i=0;i<listCfpb.size();i++){
            Cfpb cfpb = listCfpb.get(i);
            String cssj = cfpb.getCssj();
            if(!TextUtils.isEmpty(cssj)&&cfpb.getFzs()>Integer.parseInt(cssj)){
                listTemp.add(cfpb);
            }
        }
        mRecyAdapter.setNewData(listTemp);
    }

    @Subscribe
    public void isOutTime(OutTime event){
        outTime=event.isOutTime();
        if(outTime==true){
            setOutListCfpb();
        }else{
            mRecyAdapter.setNewData(listCfpb);
        }
    }

//    @Subscribe
//    public void huacai(CountFood event){
//        EventBus.getDefault().post(new AddAnim(currentView));
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onItemClick(View view, int i) {
        OrderDetailFragment detailFragment;
        if(outTime==true){//超时单品
            detailFragment = OrderDetailFragment.newInstance(listTemp.get(i));
        }else{
            detailFragment = OrderDetailFragment.newInstance(listCfpb.get(i));
        }
        detailFragment.show(getFragmentManager(),getString(R.string.order_detail));
        currentView=view;
    }
}
