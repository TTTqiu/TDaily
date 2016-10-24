package com.ttt.zhihudaily.activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.ttt.zhihudaily.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
        if (pref.getBoolean("isNightMode",false)){
            setTheme(R.style.MyTheme_NoActionBar_Night);
        }else {
            setTheme(R.style.MyTheme_NoActionBar_Day);
        }

        super.onCreate(savedInstanceState);
    }
}
