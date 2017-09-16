package com.duowei.kitchen_barbecue.fragment.setting;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.util.Log;

import com.duowei.kitchen_barbecue.R;
import com.duowei.kitchen_barbecue.app.MyApplication;
import com.duowei.kitchen_barbecue.tools.PreferenceUtils;
import com.duowei.kitchen_barbecue.tools.ToastUtil;

/**
 * A simple {@link} subclass.
 */
public class ColorSettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private PreferenceUtils mPreferenceUtils;
    private Context mContext;
    private Preference mEtColor1;
    private Preference mEtColor2;
    private Preference mEtColor3;
    private Preference mEtColor4;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.colorsetting);
        mPreferenceUtils = PreferenceUtils.getInstance(getActivity());
        mContext = MyApplication.getContext();

        mEtColor1 = findPreference(getString(R.string.color1));
        mEtColor2 = findPreference(getString(R.string.color2));
        mEtColor3 = findPreference(getString(R.string.color3));
        mEtColor4 = findPreference(getString(R.string.color4));
        mEtColor1.setSummary(mPreferenceUtils.getColor1(getString(R.string.color1),1)+"分钟");
        mEtColor2.setSummary(mPreferenceUtils.getColor1(getString(R.string.color2),9999999)+"分钟");
        mEtColor3.setSummary(mPreferenceUtils.getColor1(getString(R.string.color3),99999999)+"分钟");
        mEtColor4.setSummary(mPreferenceUtils.getColor1(getString(R.string.color4),999999999)+"分钟");
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key==mContext.getString(R.string.color1)){
            String color1 = sharedPreferences.getString(mContext.getString(R.string.color1), "1");
            mEtColor1.setSummary(color1+"分钟");
            int colorTime1 = Integer.parseInt(color1);
            mPreferenceUtils.setColor1(mContext.getString(R.string.color1), colorTime1);
        }else if(key==mContext.getString(R.string.color2)){
            String color2 = sharedPreferences.getString(mContext.getString(R.string.color2), "9999999");
            int colorTime2 = Integer.parseInt(color2);
            int colorTime1 = mPreferenceUtils.getColor1(mContext.getString(R.string.color1), 1);
            if(colorTime2<=colorTime1){
                ToastUtil.show("请设置大于超时颜色1的分钟数");
                return;
            }
            mEtColor2.setSummary(color2+"分钟");
            mPreferenceUtils.setColor2(getString(R.string.color2), colorTime2);
        }else if(key==mContext.getString(R.string.color3)){
            String color3 = sharedPreferences.getString(mContext.getString(R.string.color3), "99999999");
            int colorTime3 = Integer.parseInt(color3);
            int colorTime2 = mPreferenceUtils.getColor2(mContext.getString(R.string.color2), 1);
            if(colorTime3<=colorTime2){
                ToastUtil.show("请设置大于超时颜色2的分钟数");
                return;
            }
            mEtColor3.setSummary(color3+"分钟");
            mPreferenceUtils.setColor3(mContext.getString(R.string.color3), colorTime3);
        }else if(key==mContext.getString(R.string.color4)){
            String color4 = sharedPreferences.getString(mContext.getString(R.string.color4), "999999999");
            int colorTime4 = Integer.parseInt(color4);
            int colorTime3 = mPreferenceUtils.getColor3(mContext.getString(R.string.color3), 1);
            if(colorTime4<=colorTime3){
                ToastUtil.show("请设置大于超时颜色3的分钟数");
                return;
            }
            mEtColor4.setSummary(color4+"分钟");
            mPreferenceUtils.setColor4(getString(R.string.color4), colorTime4);
        }
    }
}
