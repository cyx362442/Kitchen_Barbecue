package com.duowei.kitchen_barbecue.fragment.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.duowei.kitchen_barbecue.R;
import com.duowei.kitchen_barbecue.app.MyApplication;
import com.duowei.kitchen_barbecue.tools.PreferenceUtils;

/**
 * A simple {} subclass.
 */
public class PrintSettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static PreferenceUtils mPreferenceUtils;
    private static EditTextPreference mEtPrinterIP;
    private static ListPreference listPrint;
    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.printsetting);
        mPreferenceUtils=PreferenceUtils.getInstance(getActivity());
        initPreference();
        mContext = MyApplication.getContext();
    }
    private void initPreference() {
        listPrint = (ListPreference) findPreference(getString(R.string.printstytle));
        mEtPrinterIP = (EditTextPreference) findPreference(getString(R.string.printip));
        listPrint.setSummary(mPreferenceUtils.getPrintStytle(getString(R.string.printstytle),getResources().getString(R.string.closeprint)));
        mEtPrinterIP.setSummary(mPreferenceUtils.getPrinterIp(getString(R.string.printip),""));
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(mContext.getString(R.string.printstytle))){
            String printStytle = sharedPreferences.getString(mContext.getString(R.string.printstytle), "");
            listPrint.setSummary(printStytle);
            mPreferenceUtils.setPrintStytle(mContext.getString(R.string.printstytle),printStytle);
        }else if(key.equals(mContext.getString(R.string.printip))){
            String printerIP = sharedPreferences.getString(mContext.getString(R.string.printip), "")
                    .trim();
            mEtPrinterIP.setSummary(printerIP);
            mPreferenceUtils.setPrinterIp(mContext.getString(R.string.printip),printerIP);
        }
    }
}
