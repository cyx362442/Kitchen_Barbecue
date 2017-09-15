package com.duowei.kitchen_barbecue.fragment.setting;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.duowei.kitchen_barbecue.R;

/**
 * A simple {@link} subclass.
 */
public class ColorSettingFragment extends PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.colorsetting);
    }
}
