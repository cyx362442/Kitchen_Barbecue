package com.duowei.kitchen_barbecue.tools;

import android.widget.Toast;

import com.duowei.kitchen_barbecue.app.MyApplication;

/**
 * Created by Administrator on 2017-09-07.
 */

public class ToastUtil {

    private static Toast toast;

    public static void show(String msg){
        if(toast==null){
            toast = Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT);
        }else{
            toast.setText(msg);
        }
        toast.show();
    }
}
