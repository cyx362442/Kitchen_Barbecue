package com.duowei.kitchen_barbecue.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.duowei.kitchen_barbecue.fragment.dialog.OrderDetailFragment;

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

        mPb = inflate.findViewById(R.id.pb);
        RecyclerView rv = inflate.findViewById(R.id.rv);
        mRecyAdapter = new MainRecyAdapter(listCfpb);
        rv.setLayoutManager(new GridLayoutManager(getActivity(),4));
        rv.addItemDecoration(new SpacesItemDecoration(5));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(mRecyAdapter);
        mRecyAdapter.setOnRecyclerViewItemClickListener(this);
        return inflate;
    }

    @Subscribe
    public void getOrderList(Order event){
        mRecyAdapter.setNewData(listCfpb=event.getListCfpb());
        mPb.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onItemClick(View view, int i) {
        OrderDetailFragment detailFragment = OrderDetailFragment.newInstance(listCfpb.get(i));
        detailFragment.show(getFragmentManager(),getString(R.string.order_detail));
    }
}
