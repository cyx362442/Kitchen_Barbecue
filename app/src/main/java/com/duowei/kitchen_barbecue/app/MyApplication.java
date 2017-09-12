package com.duowei.kitchen_barbecue.app;

import android.content.Context;

import com.duowei.kitchen_barbecue.httputils.MyVolley;

import org.litepal.LitePalApplication;

/**
 * Created by Administrator on 2017-09-04.
 */

public class MyApplication extends LitePalApplication {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        MyVolley.init(this);
    }
    public static Context getContext(){
        return context;
    }
}
