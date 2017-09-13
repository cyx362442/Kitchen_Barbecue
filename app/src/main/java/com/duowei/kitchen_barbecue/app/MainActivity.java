package com.duowei.kitchen_barbecue.app;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.duowei.kitchen_barbecue.R;
import com.duowei.kitchen_barbecue.event.ShowOut;
import com.duowei.kitchen_barbecue.fragment.MainFragment;
import com.duowei.kitchen_barbecue.fragment.OutFragment;
import com.duowei.kitchen_barbecue.fragment.TopFragment;
import com.duowei.kitchen_barbecue.httputils.Net;
import com.duowei.kitchen_barbecue.server.PollingService;
import com.duowei.kitchen_barbecue.tools.PreferenceUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {
    private Intent mServerIntent;
    private FrameLayout mFrameLayout;
    private PreferenceUtils mPreferenceUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        mPreferenceUtils = PreferenceUtils.getInstance(this);

        mFrameLayout = (FrameLayout) findViewById(R.id.frame_out);
        initFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String serviceIp = mPreferenceUtils.getServiceIp(getString(R.string.serverip), "");
        String ketchen = mPreferenceUtils.getKetchen(getString(R.string.kitchen), "");
        Net.url="http://"+serviceIp+":2233/server/ServerSvlt?";
        Net.sqlCfpb="select A.XH,A.xmbh,LTrim(A.xmmc)as xmmc,A.dw,(isnull(A.sl,0)-isnull(A.tdsl,0)-isnull(A.YWCSL,0))sl," +
                "A.pz,CONVERT(varchar(100), a.xdsj, 120)as xdsj,A.BY1 as czmc,datediff(minute,A.xdsj,getdate())fzs,A.yhmc,isnull(A.xszt,'')xszt," +
                "A.ywcsl,j.py,isnull(j.by13,9999999)cssj,A.by9,A.by10 from cfpb A LEFT JOIN JYXMSZ J ON A.XMBH=J.XMBH where A.XDSJ BETWEEN DATEADD(mi,-180,GETDATE()) " +
                "AND GETDATE() and (isnull(A.sl,0)-isnull(A.tdsl,0))>0 and a.pos='"+ketchen+"'order by A.xdsj,A.xmmc|";

        mServerIntent = new Intent(this, PollingService.class);
        startService(mServerIntent);
    }

    private void initFragment() {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().
                replace(R.id.frame_main,new MainFragment()).
                commit();
        fm.beginTransaction().
                replace(R.id.frame_top,new TopFragment()).
                commit();
        fm.beginTransaction().
                replace(R.id.frame_out,new OutFragment()).
                commit();
    }

    @Subscribe
    public void showFragment(ShowOut event){
        if(event.isShow()){
            mFrameLayout.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.animleft);
            mFrameLayout.startAnimation(animation);
        }else{
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.animright);
            mFrameLayout.startAnimation(animation);
            mFrameLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(mServerIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
