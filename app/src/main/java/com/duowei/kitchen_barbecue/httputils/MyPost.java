package com.duowei.kitchen_barbecue.httputils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.duowei.kitchen_barbecue.R;
import com.duowei.kitchen_barbecue.bean.Cfpb;
import com.duowei.kitchen_barbecue.bean.Cfpb_complete;
import com.duowei.kitchen_barbecue.bean.Cfpb_item;
import com.duowei.kitchen_barbecue.event.Order;
import com.duowei.kitchen_barbecue.event.Update;
import com.duowei.kitchen_barbecue.event.UpdateCfpb;
import com.duowei.kitchen_barbecue.tools.DateTimes;
import com.google.gson.Gson;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017-09-04.
 */

public class MyPost {

    private MyPost() {}

    private static MyPost post = null;

    public synchronized static MyPost getInstance() {
        if (post == null) {
            post = new MyPost();
        }
        return post;
    }
    private List<Cfpb>listCfpb=new ArrayList<>();


    public synchronized void postCfpb(){
        DownHTTP.postVolley6(Net.url, Net.sqlCfpb, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
            @Override
            public void onResponse(String response) {
                if(response.equals("]")){
                    listCfpb.clear();
                    EventBus.getDefault().post(new Order(listCfpb));
                    return;
                }
                String str="";
                listCfpb.clear();
                Gson gson = new Gson();
                Cfpb[] cfpbarray = gson.fromJson(response, Cfpb[].class);
                /*按单品编号及口味不同分类**/
                for(int i=0;i<cfpbarray.length;i++){
                    Cfpb cfpb = cfpbarray[i];
                    //抓取每个单品(不同口味)的数据集
                    for (int j = 0; j < cfpbarray.length; j++) {
                        if ((cfpbarray[j].getXmbh()+"&"+cfpbarray[j].getPz()).equals(cfpb.getXmbh()+"&"+cfpb.getPz())) {
                            Cfpb_item cfpb_item = new Cfpb_item(cfpbarray[j].getXmbh(), cfpbarray[j].getCzmc(),
                                    cfpbarray[j].getSl(), cfpbarray[j].getFzs(),
                                    cfpbarray[j].getXH(), cfpbarray[j].getPz(),
                                    cfpbarray[j].getXszt(),cfpbarray[j].getBy10(),
                                    cfpbarray[j].getCssj(),cfpbarray[j].getDw(),false);
                            List<Cfpb_item> list = cfpb.getListCfpb();
                            list.add(cfpb_item);
                            cfpb.setListCfpb(list);
                        }
                    }

                    if(!str.contains(cfpb.getXmbh()+"&"+cfpb.getPz()+"-")){
                        listCfpb.add(cfpb);
                        str+=cfpb.getXmbh()+"&"+cfpb.getPz()+"-";
                    }
                }
                EventBus.getDefault().post(new Order(listCfpb));
            }
        });
    }

    public void getServerTime(){
        String sql="select CONVERT(varchar(100),getdate(),120) as fwqsj from cfpb|";
        DownHTTP.postVolley6(Net.url, sql, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    String fwqsj = jsonArray.getJSONObject(0).getString("fwqsj");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date datetime = dateFormat.parse(fwqsj);
                    long currentServerTime = datetime.getTime();
                    DateTimes.serverTime=currentServerTime;

//                    /*删除120小时前的历史数据*/
                    long l = currentServerTime - 120*60*60*1000;
                    DataSupport.deleteAll(Cfpb_complete.class,"time<'"+l+"'");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public synchronized void setPost7(String sql) {
        DownHTTP.postVolley7(Net.url, sql, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
            @Override
            public void onResponse(String response) {
                if (response.contains("richado")) {
                    EventBus.getDefault().post(new UpdateCfpb());
                }
            }
        });
    }

    public void checkUpdate(final Context context, final boolean auto){
        String sql="http://ouwtfo4eg.bkt.clouddn.com/kitchen_barbecue.txt";
        final int version = getAPPVersionCode(context);
        DownHTTP.getVolley(sql, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String versionCode = jsonObject.getString("versionCode");
                    int currentVersion = Integer.parseInt(versionCode);
                    if(currentVersion >version){
                        final String url = jsonObject.getString("url");
                        final String name = jsonObject.getString("name");
                        String msg = jsonObject.getString("msg");
                        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("发现新版本");
                        builder.setIcon(R.mipmap.logo);
                        builder.setMessage(msg);
                        builder.setNegativeButton("暂不升级",null);
                        builder.setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EventBus.getDefault().post(new Update(url,name));
                            }
                        });
                        builder.create().show();
                    }else{
                        if(auto==false){
                            Toast.makeText(context,"当前己是最新版本",Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //当前APP版本号
    public int getAPPVersionCode(Context ctx) {
        int currentVersionCode = 0;
        PackageManager manager = ctx.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
//            String appVersionName = info.versionName; // 版本名
            currentVersionCode = info.versionCode; // 版本号
//            System.out.println(currentVersionCode + " " + appVersionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return currentVersionCode;
    }
}
