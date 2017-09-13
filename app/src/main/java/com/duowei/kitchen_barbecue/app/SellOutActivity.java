package com.duowei.kitchen_barbecue.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.VolleyError;
import com.duowei.kitchen_barbecue.R;
import com.duowei.kitchen_barbecue.bean.Jyxmsz;
import com.duowei.kitchen_barbecue.event.SaleOut;
import com.duowei.kitchen_barbecue.event.StartAnim;
import com.duowei.kitchen_barbecue.event.StopAnim;
import com.duowei.kitchen_barbecue.fragment.SelloutFragment;
import com.duowei.kitchen_barbecue.fragment.UnSellOutFragment;
import com.duowei.kitchen_barbecue.httputils.DownHTTP;
import com.duowei.kitchen_barbecue.httputils.Net;
import com.duowei.kitchen_barbecue.httputils.VolleyResultListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;

import java.util.List;

public class SellOutActivity extends AppCompatActivity {
    private SelloutFragment mSelloutFragment;
    private UnSellOutFragment mUnSellOutFragment;
    private List<Jyxmsz> mList;
    private View mLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_out);
        EventBus.getDefault().register(this);
        initFragment();
        mLoad = findViewById(R.id.loading);
        anim();
        getJyxmsz();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.menu_exit){
            finish();
        }
        return true;
    }

    private void initFragment() {
        mSelloutFragment = new SelloutFragment();
        mUnSellOutFragment = new UnSellOutFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.frame01, mSelloutFragment).commit();
        getFragmentManager().beginTransaction()
                .replace(R.id.frame02, mUnSellOutFragment).commit();
    }

    public synchronized void getJyxmsz(){
        DownHTTP.postVolley6(Net.url, Net.sqlJyxmsz, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
            @Override
            public void onResponse(String response) {
                DataSupport.deleteAll(Jyxmsz.class);
                Gson gson = new Gson();
                List<Jyxmsz> listJyxmsz = gson.fromJson(response, new TypeToken<List<Jyxmsz>>() {
                }.getType());
                DataSupport.saveAll(listJyxmsz);
                mList = DataSupport.where("gq=?", "1").find(Jyxmsz.class);
                mSelloutFragment.notifyAdapter(mList);
                mLoad.setVisibility(View.GONE);
            }
        });
    }

    @Subscribe
    public void SaleOut(SaleOut event){
        mSelloutFragment.addGuqing(event.mJyxmsz);
        mLoad.setVisibility(View.GONE);
    }

    @Subscribe
    public void startAnim(StartAnim event){
        anim();
    }

    private void anim() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLoad.setVisibility(View.VISIBLE);
            }
        });
    }

    @Subscribe
    public void stopAnim(StopAnim event){
        mLoad.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
