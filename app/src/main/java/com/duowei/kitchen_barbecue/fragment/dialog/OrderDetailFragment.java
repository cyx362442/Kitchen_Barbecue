package com.duowei.kitchen_barbecue.fragment.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.duowei.kitchen_barbecue.R;
import com.duowei.kitchen_barbecue.adapter.OrderDetailAdapter;
import com.duowei.kitchen_barbecue.adapter.SpacesItemDecoration;
import com.duowei.kitchen_barbecue.bean.Cfpb;
import com.duowei.kitchen_barbecue.bean.Cfpb_item;
import com.duowei.kitchen_barbecue.event.CountFood;
import com.duowei.kitchen_barbecue.event.UpdateCfpb;
import com.duowei.kitchen_barbecue.tools.DateTimes;
import com.duowei.kitchen_barbecue.tools.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDetailFragment extends DialogFragment implements View.OnClickListener,
        BaseQuickAdapter.OnRecyclerViewItemClickListener {
    private Cfpb mCfpb;
    private OrderDetailAdapter mDetailAdapter;
    private TextView mTvInput;
    private String str="";
    private List<Cfpb_item> mListCfpbItem;
    private float count=0;

    private List<Cfpb> listCfpbComplete=new ArrayList<>();//己划菜的

    public static OrderDetailFragment newInstance(Cfpb cfpb) {
        Bundle args = new Bundle();
        args.putSerializable("cfpb", cfpb);
        OrderDetailFragment fragment = new OrderDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mCfpb = (Cfpb) getArguments().getSerializable("cfpb");
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View inflate = layoutInflater.inflate(R.layout.fragment_order_detail, null);
        initUI(inflate);

        RecyclerView rv = inflate.findViewById(R.id.rv_orderDetail);
        mDetailAdapter = new OrderDetailAdapter(mCfpb.getListCfpb());
        rv.setLayoutManager(new GridLayoutManager(getActivity(),4));
        rv.addItemDecoration(new SpacesItemDecoration(10));
        rv.setAdapter(mDetailAdapter);
        mDetailAdapter.setOnRecyclerViewItemClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog dialog = builder.setView(inflate).show();
        //设置dialog宽度
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay();
        //获取屏幕宽
        wlp.width = d.getWidth() * 3 / 4;
        wlp.height=d.getHeight()*4/5;
        window.setAttributes(wlp);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void updateSuccess(UpdateCfpb event){
        dismiss();
    }

    private void initUI(View inflate) {
        count=0;
        mListCfpbItem = mCfpb.getListCfpb();
        for(Cfpb_item cfpbitem: mListCfpbItem){
            count+=cfpbitem.sl1;
        }
        TextView tvTitle = inflate.findViewById(R.id.tv_title);
        TextView tvNum = inflate.findViewById(R.id.tv_num);
        TextView tvDw = inflate.findViewById(R.id.tv_dw);
        mTvInput = inflate.findViewById(R.id.tv_input);
        tvTitle.setText(mCfpb.getXmmc());
        tvNum.setText(count+"");
        tvDw.setText(mCfpb.getDw());

        inflate.findViewById(R.id.tv_zero).setOnClickListener(this);
        inflate.findViewById(R.id.tv_one).setOnClickListener(this);
        inflate.findViewById(R.id.tv_two).setOnClickListener(this);
        inflate.findViewById(R.id.tv_three).setOnClickListener(this);
        inflate.findViewById(R.id.tv_four).setOnClickListener(this);
        inflate.findViewById(R.id.tv_five).setOnClickListener(this);
        inflate.findViewById(R.id.tv_six).setOnClickListener(this);
        inflate.findViewById(R.id.tv_seven).setOnClickListener(this);
        inflate.findViewById(R.id.tv_eight).setOnClickListener(this);
        inflate.findViewById(R.id.tv_nine).setOnClickListener(this);
        inflate.findViewById(R.id.tv_dot).setOnClickListener(this);
        inflate.findViewById(R.id.tv_del).setOnClickListener(this);
        inflate.findViewById(R.id.tv_confirm).setOnClickListener(this);
        inflate.findViewById(R.id.tv_exit).setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_seven:
                input("7");
                break;
            case R.id.tv_eight:
                input("8");
                break;
            case R.id.tv_nine:
                input("9");
                break;
            case R.id.tv_four:
                input("4");
                break;
            case R.id.tv_five:
                input("5");
                break;
            case R.id.tv_six:
                input("6");
                break;
            case R.id.tv_one:
                input("1");
                break;
            case R.id.tv_two:
                input("2");
                break;
            case R.id.tv_three:
                input("3");
                break;
            case R.id.tv_zero:
                input("0");
                break;
            case R.id.tv_dot:
                if(TextUtils.isEmpty(str)||str.contains(".")){
                    return;
                }
                input(".");
                break;
            case R.id.tv_del:
                if(str.length()>0){
                    str=str.substring(0,str.length()-1);
                    mTvInput.setText(str);
                }
                break;
            case R.id.tv_confirm:
                float tempNum=0;
                Cfpb cfpb21=null;
                String trim = mTvInput.getText().toString().trim();
                if(!TextUtils.isEmpty(trim)){//手输数量
                    float num = Float.parseFloat(trim);
                    if(num>count){
                        ToastUtil.show("输入数量不能大于待划菜数量");
                        return;
                    }
                    for(int i=0;i<mListCfpbItem.size();i++){
                        if(num>0){
                            Cfpb_item cfpbItem = mListCfpbItem.get(i);
                            if(cfpbItem.sl1<=num){//当前桌号待删除的单品数量<=num,直接删除这行
                                tempNum=cfpbItem.sl1;
                            }else {//当前桌号待删除的单品数量>num,更新己用数量字段
                                tempNum=num;
                            }
                            //己完成
                            cfpb21 = new Cfpb(cfpbItem.getXh(), mCfpb.getXmbh(), mCfpb.getXmmc(), mCfpb.getDw(),
                                    cfpbItem.sl1, cfpbItem.pz, mCfpb.getXdsj(), cfpbItem.czmc1,
                                    cfpbItem.fzs, mCfpb.getYhmc(), tempNum, DateTimes.getSysTime(),
                                    DateTimes.getTime2(mCfpb.getXdsj()));
                            listCfpbComplete.add(cfpb21);
                            num=num-cfpbItem.sl1;
                        }else{
                            break;
                        }
                    }
                }
                else{//点选桌号
                    for(int i=0;i<mListCfpbItem.size();i++){
                        Cfpb_item cfpbItem = mListCfpbItem.get(i);
                        if(cfpbItem.isSelect==true){
                            cfpb21 = new Cfpb(cfpbItem.getXh(), mCfpb.getXmbh(), mCfpb.getXmmc(), mCfpb.getDw(),
                                    cfpbItem.sl1, cfpbItem.pz, mCfpb.getXdsj(), cfpbItem.czmc1,
                                    cfpbItem.fzs, mCfpb.getYhmc(), cfpbItem.sl1, DateTimes.getSysTime(),
                                    DateTimes.getTime2(mCfpb.getXdsj()));
                            listCfpbComplete.add(cfpb21);
                        }
                    }
                }

                if(listCfpbComplete.size()<=0){
                    ToastUtil.show("请选择餐桌或输入数量");
                }else{
                    for(int i=0;i<mListCfpbItem.size();i++){//删除之前此菜品的己点数据
                        Cfpb_item cfpb_item = mListCfpbItem.get(i);
                        DataSupport.deleteAll(Cfpb.class,"xh=?",cfpb_item.getXh());
                    }

                    for(int i=0;i<listCfpbComplete.size();i++){
                        Cfpb cfpb = listCfpbComplete.get(i);
                        cfpb.saveOrUpdate("xh=?",cfpb.getXH());
                    }
                    EventBus.getDefault().post(new CountFood());
                    dismiss();
                }
                break;
            case R.id.tv_exit:
                dismiss();
                break;
        }
    }

    private void input(String num) {
        str+=num;
        mTvInput.setText(str);
        for(int i=0;i<mListCfpbItem.size();i++){
            mListCfpbItem.get(i).isSelect=false;
        }
        mDetailAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int i) {
        Cfpb_item cfpb_item = mListCfpbItem.get(i);
        cfpb_item.isSelect=!cfpb_item.isSelect;
        mDetailAdapter.notifyDataSetChanged();
        mTvInput.setText(str="");
    }
}
