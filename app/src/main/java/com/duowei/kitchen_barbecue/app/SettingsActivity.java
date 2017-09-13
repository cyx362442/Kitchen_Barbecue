package com.duowei.kitchen_barbecue.app;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.duowei.kitchen_barbecue.R;
import com.duowei.kitchen_barbecue.tools.PreferenceUtils;

public class SettingsActivity extends AppCompatActivity {

    private SettingFragment mFragment;
    private static PreferenceUtils mPreferenceUtils;
    private static EditTextPreference mEtServiceIP;
    private static EditTextPreference mEtPrinterIP;
    private static CheckBoxPreference mCheckbox;
    private static ListPreference listPrint;
    private static EditTextPreference etKetchen;

    public final static int RESULTCODE=300;
    private static Preference etVersion;
    private static String mVersionName;
    private static int mVersionCode;
    private static ListPreference listColums;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_settings);
        getAPPVersionName();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        mFragment = new SettingFragment();
        ft.replace(R.id.frame_setting, mFragment).commit();
    }

    public static class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {

        private Context mContext;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mPreferenceUtils=PreferenceUtils.getInstance(getActivity());
            addPreferencesFromResource(R.xml.preference);
            mContext = MyApplication.getContext();
            initPreference();
        }
        private void initPreference() {
            mEtServiceIP = (EditTextPreference)findPreference(getString(R.string.serverip));
            etKetchen = (EditTextPreference) findPreference(getString(R.string.kitchen));
            listPrint = (ListPreference) findPreference(getString(R.string.printstytle));
            listColums = (ListPreference) findPreference(getString(R.string.columnnum));
            mEtPrinterIP = (EditTextPreference) findPreference(getString(R.string.printip));
            etVersion = findPreference("et_version");
            mCheckbox = (CheckBoxPreference) findPreference("checkbox");
            mEtServiceIP.setSummary(mPreferenceUtils.getServiceIp(getString(R.string.serverip),""));
            etKetchen.setSummary(mPreferenceUtils.getKetchen(getString(R.string.kitchen),""));
            listPrint.setSummary(mPreferenceUtils.getPrintStytle(getString(R.string.printstytle),getResources().getString(R.string.closeprint)));
            listColums.setSummary(mPreferenceUtils.getListColums(getString(R.string.columnnum),getString(R.string.four)));

            mEtPrinterIP.setSummary(mPreferenceUtils.getPrinterIp(getString(R.string.printip),""));
            etVersion.setSummary(mVersionName);
            etVersion.setTitle("版本更新(V"+mVersionCode+")");
            etVersion.setOnPreferenceClickListener(this);
            mCheckbox.setChecked(mPreferenceUtils.getAutoStart("auto",true));
            SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
            sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(key.equals(mContext.getString(R.string.serverip))){
                String serviceIP = sharedPreferences.getString(mContext.getString(R.string.serverip), "");
                mEtServiceIP.setSummary(serviceIP);
                mPreferenceUtils.setServiceIp(mContext.getString(R.string.serverip),serviceIP);
            }else if(key.equals(mContext.getString(R.string.kitchen))){
                String kitChen = sharedPreferences.getString(mContext.getString(R.string.kitchen), "");
                etKetchen.setSummary(kitChen);
                mPreferenceUtils.setKetchen(mContext.getString(R.string.kitchen),kitChen);
            }else if(key.equals(mContext.getString(R.string.printstytle))){
                String printStytle = sharedPreferences.getString(mContext.getString(R.string.printstytle), "");
                listPrint.setSummary(printStytle);
                mPreferenceUtils.setPrintStytle(mContext.getString(R.string.printstytle),printStytle);
            }else if(key.equals(mContext.getString(R.string.printip))){
                String printerIP = sharedPreferences.getString(mContext.getString(R.string.printip), "");
                mEtPrinterIP.setSummary(printerIP);
                mPreferenceUtils.setPrinterIp(mContext.getString(R.string.printip),printerIP);
            }else if(key.equals(mContext.getString(R.string.columnnum))){
                String colums = sharedPreferences.getString(mContext.getString(R.string.columnnum), "");
                listColums.setSummary(colums);
                mPreferenceUtils.setListColums(mContext.getString(R.string.columnnum),colums);
            }else if(key.equals("checkbox")){
                boolean auto=mPreferenceUtils.getAutoStart("auto", true);
                auto=!auto;
                mCheckbox.setChecked(auto);
                mPreferenceUtils.setAutoStart("auto",auto);
            }
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
//            Post.getInstance().checkUpdate(getActivity(),false);
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.menu_exit){
            setResult(RESULTCODE);
            finish();
        }
        return true;
    }

    //当前APP版本号
    public void getAPPVersionName() {
        PackageManager manager = getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            // 版本名
            mVersionName = info.versionName;
            mVersionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }
}
