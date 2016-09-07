package com.ttt.zhihudaily.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.ttt.zhihudaily.R;
import com.ttt.zhihudaily.adapter.MyPagerAdapter;
import com.ttt.zhihudaily.fragment.MyFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Fragment> list=new ArrayList<>();
    private String[] titles=new String[5];
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().setElevation(0);

        initViewPager();
        TabLayout tabLayout=(TabLayout)findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_fav:
                Intent intent=new Intent(MainActivity.this,FavouriteActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_settings:

                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 获得日期
     * @return 2016年8月23日 格式的日期
     */
    private String getDate(int i,Boolean isFormat){
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DATE,-i);
        SimpleDateFormat simpleDateFormat;
        if(isFormat){
            simpleDateFormat=new SimpleDateFormat("yyyy年M月d日");
        }else {
            simpleDateFormat=new SimpleDateFormat("yyyyMMdd");
        }
        return simpleDateFormat.format(calendar.getTime());
    }

    private void initViewPager(){
        for(int i=0;i<5;i++){
            titles[i]= getDate(i,true);
            MyFragment fragment=new MyFragment();
            if(i!=0){
                Bundle bundle=new Bundle();
                bundle.putString("date",getDate(i-1,false));
                fragment.setArguments(bundle);
            }
            list.add(fragment);

            viewPager=(ViewPager)findViewById(R.id.view_pager);
            viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),list,titles));
        }
    }
}
