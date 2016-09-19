package com.ttt.zhihudaily.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.widget.CheckBox;
import android.widget.Toast;

import com.ttt.zhihudaily.R;

public class PrefsFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener,Preference.OnPreferenceClickListener{

    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.prefs);

        prefs=PreferenceManager.getDefaultSharedPreferences(getActivity());

        CheckBoxPreference cp=(CheckBoxPreference)findPreference("checkbox_preference");
        SwitchPreference sp=(SwitchPreference)findPreference("switch_preference");
        cp.setOnPreferenceClickListener(this);
        sp.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Toast.makeText(getActivity(), "开关按钮2的值是："+newValue, Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        Boolean checkboxValue=prefs.getBoolean(preference.getKey(),false);
        Toast.makeText(getActivity(), "开关按钮1的值是："+checkboxValue, Toast.LENGTH_SHORT).show();
        return true;
    }
}
