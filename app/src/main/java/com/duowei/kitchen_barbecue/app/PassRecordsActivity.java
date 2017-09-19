package com.duowei.kitchen_barbecue.app;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.duowei.kitchen_barbecue.R;
import com.duowei.kitchen_barbecue.adapter.LvAdapter;
import com.duowei.kitchen_barbecue.adapter.PassRecyAdapter;
import com.duowei.kitchen_barbecue.bean.Cfpb_complete;
import com.duowei.kitchen_barbecue.view.wheelview.DateUtils;
import com.duowei.kitchen_barbecue.view.wheelview.JudgeDate;
import com.duowei.kitchen_barbecue.view.wheelview.ScreenInfo;
import com.duowei.kitchen_barbecue.view.wheelview.WheelMain;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PassRecordsActivity extends AppCompatActivity implements
        View.OnClickListener, AdapterView.OnItemClickListener {

    private PassRecyAdapter mAdapter;
    private List<Cfpb_complete> mCompleteList;
    private TextView mTvName;
    private boolean isShow=false;
    private PopupWindow mPopupWindow;
    private List<Cfpb_complete>tempList;
    private String str="";
    private int count=0;
    private TextView mTvBottom;
    private TextView mTvStartDate;
    private WheelMain wheelMainDate;
    private String beginTime="2017-01-01 00:00";//开始时间
    private String endTime="2099-01-01 00:00";//结束时间
    private TextView mTvEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_records);
        mCompleteList = DataSupport.order("xdsj desc").find(Cfpb_complete.class);
        tempList=new ArrayList<>();

        RecyclerView rv = (RecyclerView) findViewById(R.id.recycleView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this,1));
        mAdapter = new PassRecyAdapter(mCompleteList);
        rv.setAdapter(mAdapter);

        mTvStartDate = (TextView) findViewById(R.id.tv_start);
        mTvEndDate = (TextView) findViewById(R.id.tv_end);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvBottom = (TextView) findViewById(R.id.tv_count);
        findViewById(R.id.tv_back).setOnClickListener(this);
        findViewById(R.id.tv_clear).setOnClickListener(this);
        mTvStartDate.setOnClickListener(this);
        mTvEndDate.setOnClickListener(this);
        mTvName.setOnClickListener(this);
        tempList.add(new Cfpb_complete());
        for(Cfpb_complete cfpb: mCompleteList){
            count+=cfpb.getSl();
            if(!str.contains(cfpb.getXmbh())){
                tempList.add(cfpb);
                str+=cfpb.getXmbh()+"-";
            }
        }
        mTvBottom.setText(count+"份");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_clear:
                DataSupport.deleteAll(Cfpb_complete.class);
                List<Cfpb_complete> cfpbCompletes = DataSupport.order("xdsj desc").find(Cfpb_complete.class);
                mAdapter.setNewData(cfpbCompletes);
                break;
            case R.id.tv_start:
                showBottoPopupWindow(mTvStartDate,getString(R.string.begintime));
                break;
            case R.id.tv_end:
                showBottoPopupWindow(mTvEndDate,getString(R.string.endtime));
                break;
            case R.id.tv_name:
                isShow=!isShow;
                if(isShow==true){
                    View popuView = getLayoutInflater().inflate(R.layout.popuwindow_item, null);
                    ListView lsvMore = popuView.findViewById(R.id.listView);
                    lsvMore.setDivider(new ColorDrawable(Color.WHITE));
                    lsvMore.setDividerHeight(1);
                    LvAdapter lvAdapter = new LvAdapter(this, tempList);
                    lsvMore.setAdapter(lvAdapter);
                    lsvMore.setOnItemClickListener(this);

                    mPopupWindow = new PopupWindow(popuView);
                    mPopupWindow.setWidth(350);
                    mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                    mPopupWindow.setOutsideTouchable(false);
                    mPopupWindow.showAsDropDown(mTvName);
                }else{
                    mPopupWindow.dismiss();
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        String xmbh = tempList.get(position).getXmbh();
        count=0;
        List<Cfpb_complete>list;
        if(position==0){
            list = DataSupport
                    .where("xdsj>=? and xdsj<=?",beginTime,endTime)
                    .order("xdsj desc")
                    .find(Cfpb_complete.class);
        }else{
            list = DataSupport
                    .where("xmbh=? and xdsj>=? and xdsj<=?",xmbh,beginTime,endTime)
                    .order("xdsj desc")
                    .find(Cfpb_complete.class);
        }
        mAdapter.setNewData(list);
        for(int i=0;i<list.size();i++){
            count+=list.get(i).getSl();
        }
        isShow=false;
        mTvBottom.setText(count+"份");
        mPopupWindow.dismiss();
    }

    public void showBottoPopupWindow(final TextView view, final String str) {
        WindowManager manager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = manager.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        View menuView = LayoutInflater.from(this).inflate(R.layout.show_popup_window,null);
        final PopupWindow mPopupWindow = new PopupWindow(menuView, (int)(width*0.8),
                ActionBar.LayoutParams.WRAP_CONTENT);
        ScreenInfo screenInfoDate = new ScreenInfo(this);
        wheelMainDate = new WheelMain(menuView, true);
        wheelMainDate.screenheight = screenInfoDate.getHeight();
        String time = DateUtils.currentMonth().toString();
        Calendar calendar = Calendar.getInstance();
        if (JudgeDate.isDate(time, "yyyy-MM-DD")) {
            try {
                calendar.setTime(new Date(time));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelMainDate.initDateTimePicker(year, month, day, hours,minute);
        mPopupWindow.setAnimationStyle(R.style.AnimationPreview);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.showAtLocation(mTvStartDate, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(new poponDismissListener());
        backgroundAlpha(0.6f);
        TextView tv_cancle = menuView.findViewById(R.id.tv_cancle);
        TextView tv_ensure = menuView.findViewById(R.id.tv_ensure);
        TextView tv_pop_title = menuView.findViewById(R.id.tv_pop_title);
        tv_pop_title.setText(str);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mPopupWindow.dismiss();
                backgroundAlpha(1f);
            }
        });
        tv_ensure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(str.equals(getString(R.string.begintime))){
                    beginTime= DateUtils.formateStringH(wheelMainDate.getTime().toString(),DateUtils.yyyyMMddHHmm);
                    view.setText(beginTime);
                }else if(str.equals(getString(R.string.endtime))){
                    endTime=wheelMainDate.getTime().toString();
                    endTime=DateUtils.formateStringH(wheelMainDate.getTime().toString(),DateUtils.yyyyMMddHHmm);
                    view.setText(endTime);
                }
                mPopupWindow.dismiss();
                backgroundAlpha(1f);

//                if(str.equals(getString(R.string.endtime))){
//                    List<Cfpb_complete> cfpbCompletes = DataSupport
//                            .where("xdsj>=? and xdsj<=?",beginTime,endTime)
//                            .find(Cfpb_complete.class);
//                    mAdapter.setNewData(cfpbCompletes);
//                }

                List<Cfpb_complete> cfpbCompletes = DataSupport
                        .where("xdsj>=? and xdsj<=?",beginTime,endTime)
                        .find(Cfpb_complete.class);
                mAdapter.setNewData(cfpbCompletes);
            }
        });
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }
    class poponDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }
}
