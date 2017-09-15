package com.duowei.kitchen_barbecue.app;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.duowei.kitchen_barbecue.R;
import com.duowei.kitchen_barbecue.adapter.LvAdapter;
import com.duowei.kitchen_barbecue.adapter.PassRecyAdapter;
import com.duowei.kitchen_barbecue.bean.Cfpb_complete;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class PassRecordsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private PassRecyAdapter mAdapter;
    private List<Cfpb_complete> mCompleteList;
    private TextView mTvName;
    private boolean isShow=false;
    private PopupWindow mPopupWindow;
    private List<Cfpb_complete>tempList;
    private String str="";
    private int count=0;
    private TextView mTvBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_records);
        mCompleteList = DataSupport.findAll(Cfpb_complete.class);
        tempList=new ArrayList<>();

        RecyclerView rv = (RecyclerView) findViewById(R.id.recycleView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this,1));
        mAdapter = new PassRecyAdapter(mCompleteList);
        rv.setAdapter(mAdapter);

        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvName.setOnClickListener(this);
        mTvBottom = (TextView) findViewById(R.id.tv_count);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu,menu);
        MenuItem item = menu.findItem(R.id.menu_clear);
        item.setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_exit){
            finish();
        }else if(item.getItemId()==R.id.menu_clear){
            DataSupport.deleteAll(Cfpb_complete.class);
            List<Cfpb_complete> cfpbCompletes = DataSupport.order("wcsj desc").find(Cfpb_complete.class);
            mAdapter.setNewData(cfpbCompletes);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.tv_name){
            isShow=!isShow;
            if(isShow==true){
                View popuView = getLayoutInflater().inflate(R.layout.popuwindow_item, null);
                ListView lsvMore = popuView.findViewById(R.id.listView);
                lsvMore.setDivider(new ColorDrawable(Color.WHITE));
                lsvMore.setDividerHeight(1);
                LvAdapter lvAdapter = new LvAdapter(this, tempList);
                lsvMore.setAdapter(lvAdapter);
                lsvMore.setOnItemClickListener(this);

                mPopupWindow = new PopupWindow(popuView,400,1000);
                mPopupWindow.setOutsideTouchable(false);
                mPopupWindow.showAsDropDown(mTvName);
            }else{
                mPopupWindow.dismiss();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        String xmbh = tempList.get(position).getXmbh();
        count=0;
        List<Cfpb_complete>list;
        if(position==0){
            list=DataSupport.findAll(Cfpb_complete.class);
        }else{
            list = DataSupport.where("xmbh=?", xmbh).find(Cfpb_complete.class);
        }
        mAdapter.setNewData(list);
        for(int i=0;i<list.size();i++){
            count+=list.get(i).getSl();
        }
        isShow=false;
        mTvBottom.setText(count+"份");
        mPopupWindow.dismiss();
    }
}
