package com.duowei.kitchen_barbecue.app;

import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.duowei.kitchen_barbecue.R;
import com.duowei.kitchen_barbecue.fragment.setting.BaseFragment;
import com.duowei.kitchen_barbecue.fragment.setting.ColorSettingFragment;
import com.duowei.kitchen_barbecue.fragment.setting.PrintSettingFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.tv_basic)
    TextView mTvBasic;
    @BindView(R.id.tv_print)
    TextView mTvPrint;
    @BindView(R.id.tv_outtime)
    TextView mTvOuttime;
    @BindView(R.id.view1)
    View mView1;
    @BindView(R.id.view2)
    View mView2;
    @BindView(R.id.view3)
    View mView3;
    private FragmentManager mFm;
    public final static int RESULTCODE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        mFm = getFragmentManager();
        mFm.beginTransaction().
                replace(R.id.frame, new BaseFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.menu_exit) {
            setResult(RESULTCODE);
            finish();
        }
        return true;
    }

    @OnClick({R.id.tv_basic, R.id.tv_print, R.id.tv_outtime})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_basic:
                mFm.beginTransaction().
                        replace(R.id.frame, new BaseFragment())
                        .commit();
                mView1.setVisibility(View.VISIBLE);
                mView2.setVisibility(View.INVISIBLE);
                mView3.setVisibility(View.INVISIBLE);
                break;
            case R.id.tv_print:
                mFm.beginTransaction().
                        replace(R.id.frame, new PrintSettingFragment())
                        .commit();
                mView1.setVisibility(View.INVISIBLE);
                mView2.setVisibility(View.VISIBLE);
                mView3.setVisibility(View.INVISIBLE);
                break;
            case R.id.tv_outtime:
                mFm.beginTransaction().
                        replace(R.id.frame, new ColorSettingFragment())
                        .commit();
                mView1.setVisibility(View.INVISIBLE);
                mView2.setVisibility(View.INVISIBLE);
                mView3.setVisibility(View.VISIBLE);
                break;
        }
    }
}
