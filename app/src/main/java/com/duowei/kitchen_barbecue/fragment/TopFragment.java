package com.duowei.kitchen_barbecue.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.duowei.kitchen_barbecue.R;
import com.duowei.kitchen_barbecue.app.PassRecordsActivity;
import com.duowei.kitchen_barbecue.app.SellOutActivity;
import com.duowei.kitchen_barbecue.app.SettingActivity;
import com.duowei.kitchen_barbecue.bean.Cfpb;
import com.duowei.kitchen_barbecue.bean.Cfpb_item;
import com.duowei.kitchen_barbecue.event.CountFood;
import com.duowei.kitchen_barbecue.event.Order;
import com.duowei.kitchen_barbecue.event.OutTime;
import com.duowei.kitchen_barbecue.event.ShowOut;
import com.duowei.kitchen_barbecue.sound.KeySound;
import com.duowei.kitchen_barbecue.tools.ColorAnim;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopFragment extends Fragment {
    @BindView(R.id.tv_uncook)
    TextView mTvUncook;
    @BindView(R.id.tv_cooked)
    TextView mTvCooked;
    Unbinder unbinder;
    @BindView(R.id.tv_count)
    TextView mTvCount;
    @BindView(R.id.btn_out)
    Button mBtnOut;
    @BindView(R.id.btn_overtime)
    Button mBtnOvertime;

    private float tempNum = 0;
    private float tempOutTime = 0;
    private KeySound mSound;
    private boolean isOutTime = false;

    private boolean isShow = false;
    private Intent mIntent;

    public TopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_top, container, false);
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, inflate);
        mSound = KeySound.getContext(getActivity());
        return inflate;
    }

    //待煮菜品
    @Subscribe
    public void setListCfpb(Order event) {
        List<Cfpb> listCfpb = event.getListCfpb();
        mTvUncook.setText(listCfpb.size() + "种");
        float foodCount = 0;
        float outTime = 0;
        for (int i = 0; i < listCfpb.size(); i++) {
            Cfpb cfpb = listCfpb.get(i);
            List<Cfpb_item> list = cfpb.getListCfpb();
            for (int j = 0; j < list.size(); j++) {
                foodCount += list.get(j).sl1;
            }
            String cssj = cfpb.getCssj();
            if (!TextUtils.isEmpty(cssj) && cfpb.getFzs() > Integer.parseInt(cssj)) {
                outTime = outTime + cfpb.getSl();
            }
        }
        String str=foodCount+"";
        if((str).endsWith(".0")){
            mTvCooked.setText(str.substring(0,str.length()-2)+"份");
        }else{
            mTvCooked.setText(foodCount + "份");
        }
        Handler handler = new Handler();
        //超时单品、新订单声音、动画
        if (foodCount > tempNum && outTime > tempOutTime) {
            mSound.playSound('4', 0);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSound.playSound('0', 0);
                    ColorAnim.getInstacne(getActivity()).startColor(mTvCooked);
                }
            },2000);
        }
        //超时单品声音
        else if (outTime > tempOutTime) {
            mSound.playSound('4', 0);
            ColorAnim.getInstacne(getActivity()).startBackground(mBtnOvertime);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSound.playSound('4', 0);
                }
            },2000);
        }
        //新的订单
        else if (foodCount > tempNum) {
            mSound.playSound('0', 0);
            ColorAnim.getInstacne(getActivity()).startColor(mTvCooked);
        }
        tempNum = foodCount;
        tempOutTime = outTime;
    }

    @Subscribe
    public void setShow(ShowOut event) {
        isShow = event.isShow();
    }

    @Subscribe
    public void conut(CountFood event) {
        List<Cfpb> cfpbList = DataSupport.findAll(Cfpb.class);
        mTvCount.setText(cfpbList.size() + "");
    }

//    @Subscribe
//    public void getAnimView(AddAnim event){
//        ShoppingCartAnimationView shoppingCartAnimationView = new ShoppingCartAnimationView(getActivity());
//        int positions[] = new int[2];
//        event.getView().getLocationInWindow(positions);
//        shoppingCartAnimationView.setStartPosition(new Point(positions[0], positions[1]));
//        ViewGroup rootView = (ViewGroup) getActivity().getWindow().getDecorView();
//        rootView.addView(shoppingCartAnimationView);
//        int endPosition[] = new int[2];
//        mBtnOut.getLocationInWindow(endPosition);
//        shoppingCartAnimationView.setEndPosition(new Point(endPosition[0], endPosition[1]));
//        shoppingCartAnimationView.startBeizerAnimation();
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.btn_history,R.id.btn_overtime, R.id.btn_saleout, R.id.btn_setting, R.id.btn_exit, R.id.btn_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_history:
                mIntent=new Intent(getActivity(), PassRecordsActivity.class);
                startActivity(mIntent);
                break;
            case R.id.btn_overtime:
                isOutTime=!isOutTime;
                if(isOutTime==true){
                    mBtnOvertime.setText(getString(R.string.allfood));
                    mBtnOvertime.setBackgroundResource(R.drawable.button_blue);
                }else{
                    mBtnOvertime.setText(getString(R.string.outtime));
                    mBtnOvertime.setBackgroundResource(R.drawable.button_orange);
                }
                EventBus.getDefault().post(new OutTime(isOutTime));
                break;
            case R.id.btn_saleout:
                mIntent = new Intent(getActivity(), SellOutActivity.class);
                startActivity(mIntent);
                break;
            case R.id.btn_setting:
                mIntent = new Intent(getActivity(), SettingActivity.class);
                startActivity(mIntent);
                break;
            case R.id.btn_exit:
                getActivity().finish();
                break;
            case R.id.btn_out:
                EventBus.getDefault().post(new ShowOut(isShow = !isShow));
                break;
        }
    }
}
