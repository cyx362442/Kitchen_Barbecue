package com.duowei.kitchen_barbecue.httputils;

import android.util.Log;

import com.android.volley.VolleyError;
import com.duowei.kitchen_barbecue.bean.Cfpb;
import com.duowei.kitchen_barbecue.bean.Cfpb_item;
import com.duowei.kitchen_barbecue.event.Order;
import com.duowei.kitchen_barbecue.event.UpdateCfpb;
import com.google.gson.Gson;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
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
    private String url="http://192.168.1.78:2233/server/ServerSvlt?";
    private String sqlCfpb="select A.XH,A.xmbh,LTrim(A.xmmc)as xmmc,A.dw,(isnull(A.sl,0)-isnull(A.tdsl,0)-isnull(A.YWCSL,0))sl," +
            "A.pz,CONVERT(varchar(100), a.xdsj, 120)as xdsj,A.BY1 as czmc,datediff(minute,A.xdsj,getdate())fzs,A.yhmc,isnull(A.xszt,'')xszt," +
            "A.ywcsl,j.py,isnull(j.by13,9999999)cssj,A.by9,A.by10 from cfpb A LEFT JOIN JYXMSZ J ON A.XMBH=J.XMBH where A.XDSJ BETWEEN DATEADD(mi,-180,GETDATE()) " +
            "AND GETDATE() and (isnull(A.sl,0)-isnull(A.tdsl,0))>0 and a.pos='cyy'order by A.xdsj,A.xmmc|";

    public synchronized void postCfpb(){
        DownHTTP.postVolley6(url, sqlCfpb, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
            @Override
            public void onResponse(String response) {
                if(response.equals("]")){
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
                                    cfpbarray[j].getXszt(),cfpbarray[j].getBy10(),false);
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


    public synchronized void setPost7(String sql) {
        DownHTTP.postVolley7(url, sql, new VolleyResultListener() {
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
}
