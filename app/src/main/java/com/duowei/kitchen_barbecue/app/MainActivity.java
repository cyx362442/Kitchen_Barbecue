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
import com.duowei.kitchen_barbecue.server.PollingService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {
    private Intent mServerIntent;
    private FrameLayout mFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mServerIntent = new Intent(this, PollingService.class);
        startService(mServerIntent);
        EventBus.getDefault().register(this);

        mFrameLayout = (FrameLayout) findViewById(R.id.frame_out);
        initFragment();
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
    protected void onDestroy() {
        super.onDestroy();
        stopService(mServerIntent);
        EventBus.getDefault().unregister(this);
    }
}
