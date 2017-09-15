package com.duowei.kitchen_barbecue.fragment.setting;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.duowei.kitchen_barbecue.R;
import com.duowei.kitchen_barbecue.app.MyApplication;
import com.duowei.kitchen_barbecue.event.Update;
import com.duowei.kitchen_barbecue.fragment.UpdateFragment;
import com.duowei.kitchen_barbecue.httputils.MyPost;
import com.duowei.kitchen_barbecue.tools.PreferenceUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * A simple {@link} subclass.
 */
public class BaseFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener, SharedPreferences.OnSharedPreferenceChangeListener {
    private static PreferenceUtils mPreferenceUtils;
    private static EditTextPreference mEtServiceIP;
    private static EditTextPreference etKetchen;
    private static Preference etVersion;
    private static String mVersionName;
    private static int mVersionCode;
    private static ListPreference listColums;

    private Context mContext;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mPreferenceUtils=PreferenceUtils.getInstance(getActivity());
        addPreferencesFromResource(R.xml.preference);
        getAPPVersionName();
        mContext = MyApplication.getContext();
        initPreference();
    }

    private void initPreference() {
        mEtServiceIP = (EditTextPreference)findPreference(getString(R.string.serverip));
        etKetchen = (EditTextPreference) findPreference(getString(R.string.kitchen));
        listColums = (ListPreference) findPreference(getString(R.string.columnnum));
        etVersion = findPreference("et_version");
        mEtServiceIP.setSummary(mPreferenceUtils.getServiceIp(getString(R.string.serverip),""));
        etKetchen.setSummary(mPreferenceUtils.getKetchen(getString(R.string.kitchen),""));
        listColums.setSummary(mPreferenceUtils.getListColums(getString(R.string.columnnum),getString(R.string.four)));
        etVersion.setSummary(mVersionName);
        etVersion.setTitle("版本更新(V"+mVersionCode+")");
        etVersion.setOnPreferenceClickListener(this);
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(mContext.getString(R.string.serverip))){
            String serviceIP = sharedPreferences.getString(mContext.getString(R.string.serverip), "")
                    .trim();
            mEtServiceIP.setSummary(serviceIP);
            mPreferenceUtils.setServiceIp(mContext.getString(R.string.serverip),serviceIP);
        }else if(key.equals(mContext.getString(R.string.kitchen))){
            String kitChen = sharedPreferences.getString(mContext.getString(R.string.kitchen), "")
                    .trim();
            etKetchen.setSummary(kitChen);
            mPreferenceUtils.setKetchen(mContext.getString(R.string.kitchen),kitChen);
        }else if(key.equals(mContext.getString(R.string.columnnum))){
            String colums = sharedPreferences.getString(mContext.getString(R.string.columnnum), "");
            listColums.setSummary(colums);
            mPreferenceUtils.setListColums(mContext.getString(R.string.columnnum),colums);
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        MyPost.getInstance().checkUpdate(getActivity(),false);
        return false;
    }

    @Subscribe
    public void appUpdate(Update event){
        UpdateFragment updateFragment = UpdateFragment.newInstance(event.url, event.name);
        updateFragment.show(getFragmentManager(),getString(R.string.update));
    }

    //当前APP版本号
    public void getAPPVersionName() {
        PackageManager manager = getActivity().getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
            // 版本名
            mVersionName = info.versionName;
            mVersionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
