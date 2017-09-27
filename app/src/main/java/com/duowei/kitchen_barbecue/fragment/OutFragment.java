package com.duowei.kitchen_barbecue.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.duowei.kitchen_barbecue.R;
import com.duowei.kitchen_barbecue.adapter.OutRecyAdapter;
import com.duowei.kitchen_barbecue.bean.Cfpb;
import com.duowei.kitchen_barbecue.bean.Cfpb_complete;
import com.duowei.kitchen_barbecue.event.CountFood;
import com.duowei.kitchen_barbecue.event.OutItem;
import com.duowei.kitchen_barbecue.event.ShowOut;
import com.duowei.kitchen_barbecue.event.UpdateCfpb;
import com.duowei.kitchen_barbecue.httputils.MyPost;
import com.duowei.kitchen_barbecue.tools.DateTimes;
import com.duowei.kitchen_barbecue.tools.PreferenceUtils;
import com.duowei.kitchen_barbecue.tools.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OutFragment extends Fragment implements View.OnClickListener {

    private OutRecyAdapter mAdapter;
    private ProgressBar mPb;
    private String mPrinterIp;
    private PreferenceUtils mPreferenceUtils;
    private String mPrintStytle;
    private String mKetchen;

    public OutFragment() {
        // Required empty public constructor
    }
    private List<Cfpb> listCfpb;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_out, container, false);
        listCfpb=new ArrayList<>();
        mPreferenceUtils = PreferenceUtils.getInstance(getActivity());
        mKetchen = mPreferenceUtils.getKetchen(getString(R.string.kitchen), "");

        mPb = inflate.findViewById(R.id.pb2);
        RecyclerView rv = inflate.findViewById(R.id.rv_out);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new OutRecyAdapter(listCfpb);
        rv.setAdapter(mAdapter);

        inflate.findViewById(R.id.btn_clear).setOnClickListener(this);
        inflate.findViewById(R.id.btn_cancel).setOnClickListener(this);
        inflate.findViewById(R.id.btn_confirm).setOnClickListener(this);
        return inflate;
    }


    @Subscribe
    public void setListCfpb(ShowOut event){
        List<Cfpb> cfpbList = DataSupport.findAll(Cfpb.class);
        mAdapter.setNewData(listCfpb=cfpbList);
    }

    //划菜成功
    @Subscribe
    public void updateCfpb(UpdateCfpb event){
        mPb.setVisibility(View.GONE);
        DataSupport.deleteAll(Cfpb.class);
        EventBus.getDefault().post(new ShowOut(false));
        EventBus.getDefault().post(new CountFood());
        MyPost.getInstance().postCfpb();

        //保存历史记录到本地
        for(int i=0;i<listCfpb.size();i++){
            Cfpb cfpb = listCfpb.get(i);
            Cfpb_complete cfpbComplete = new Cfpb_complete();
            cfpbComplete.setXmbh(cfpb.getXmbh());
            cfpbComplete.setXmmc(cfpb.getXmmc());
            cfpbComplete.setZh(cfpb.getCzmc());
            cfpbComplete.setPz(cfpb.getPz());
            cfpbComplete.setSl(cfpb.getSl());
            cfpbComplete.setXdsj(cfpb.getXdsj());
            cfpbComplete.setWcsj(DateTimes.getSysTime());
            cfpbComplete.setZzry("");
            cfpbComplete.setYhmc(cfpb.getYhmc());
            cfpbComplete.setTime(cfpb.getTime());
            cfpbComplete.save();
        }
    }

    @Subscribe
    public void deleteOutItem(OutItem event){
        event.getCfpb().delete();
        listCfpb = DataSupport.findAll(Cfpb.class);
        mAdapter.setNewData(listCfpb);
        if(listCfpb.size()<=0){
            EventBus.getDefault().post(new ShowOut(false));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        mPrintStytle = mPreferenceUtils.getPrintStytle(getString(R.string.printstytle), getString(R.string.closeprint));
        mPrinterIp = mPreferenceUtils.getPrinterIp(getString(R.string.printip), "");
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btn_clear){
            DataSupport.deleteAll(Cfpb.class);
            listCfpb = DataSupport.findAll(Cfpb.class);
            mAdapter.setNewData(listCfpb);
            EventBus.getDefault().post(new CountFood());
            EventBus.getDefault().post(new ShowOut(false));
        }
        else if(view.getId()==R.id.btn_cancel){
            EventBus.getDefault().post(new ShowOut(false));
        }else if(view.getId()==R.id.btn_confirm){
            String sql="";
            for(int i=0;i<listCfpb.size();i++){
                Cfpb cfpb = listCfpb.get(i);
                if(cfpb.getSl()<=cfpb.getYwcsl()){
                    sql += "insert into CFPBYWC (XH, MTXH, WMDBH, XMBH, XMMC, DW, SL, PZ, XSZT, YHMC, POS, TDSL, XDSJ, WCSJ,      BY1, BY2, BY3, BY4, BY5, BY6, BY7) " +
                            "             select XH, MTXH, WMDBH, XMBH, XMMC, DW, SL, PZ, XSZT, YHMC, POS, TDSL, XDSJ, GETDATE(), BY1, BY2, BY3, BY4, BY5, BY6, BY7 " +
                            "             from CFPB where xh = '" + cfpb.getXH() + "'|";
                    if(mPrintStytle.equals(getString(R.string.print_server))&&!TextUtils.isEmpty(mPrinterIp)){//打印服务器
                        sql+="insert into pbdyxxb(xh,wmdbh,xmbh,xmmc,dw,sl,pz,syyxm,xtbz,czsj,zh,jsj)" +
                                "select xh,wmdbh,xmbh,xmmc,dw,"+cfpb.getSl()+",pz,'"+mKetchen+"','3',getdate(),by1,'"+mPrinterIp+"' from cfpb where XH='"+cfpb.getXH()+"'|";
                    }
                    sql+="delete from cfpb where xh='"+cfpb.getXH()+"'|";
                }else if(cfpb.getSl()>cfpb.getYwcsl()){
                    if(mPrintStytle.equals(getString(R.string.print_server))&&!TextUtils.isEmpty(mPrinterIp)){//打印服务器
                        sql+="insert into pbdyxxb(xh,wmdbh,xmbh,xmmc,dw,sl,pz,syyxm,xtbz,czsj,zh,jsj)" +
                                "select xh,wmdbh,xmbh,xmmc,dw,"+cfpb.getYwcsl()+",pz,'"+mKetchen+"','3',getdate(),by1,'"+mPrinterIp+"' from cfpb where XH='"+cfpb.getXH()+"'|";
                    }
                    sql+="update cfpb set ywcsl=isnull(ywcsl,0)+"+cfpb.getYwcsl()+" where xh="+cfpb.getXH()+"|";
                }
                sql+="update cfpb set by10='1' where xmbh='"+cfpb.getXmbh()+"' and pz='"+ cfpb.getPz()+"' and isnull(by10,'0')='0'|";
            }
            if(!TextUtils.isEmpty(sql)){
                mPb.setVisibility(View.VISIBLE);
                MyPost.getInstance().setPost7(sql);
            }else{
                ToastUtil.show("请先划菜");
            }
        }
    }
}
