package com.ttt.zhihudaily.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.Glide;
import com.ttt.zhihudaily.R;
import com.ttt.zhihudaily.adapter.MyPagerAdapter;
import com.ttt.zhihudaily.fragment.MyFragment;
import com.ttt.zhihudaily.task.LoadBannerTask;
import com.ttt.zhihudaily.util.HttpUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private List<Fragment> list=new ArrayList<>();
    private String[] titles=new String[5];
    private ViewPager viewPager;
    private ViewPager banner;
    private List<View> dotList;
    private List<ImageView> imageViewList;
    private Boolean isAutoPlay=false;
    private int currentItem;
    private ScheduledExecutorService executorService;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            banner.setCurrentItem(currentItem);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().setElevation(0);

        initViewPager();
        TabLayout tabLayout=(TabLayout)findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        initDotList();
        initImageList();
        initBanner();

        if(HttpUtil.isNetworkConnected(this)){
            new LoadBannerTask(imageViewList,this).execute();
        }else {
            Toast.makeText(this, "No Network", Toast.LENGTH_SHORT).show();
        }
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

    private void startPlay(){
        executorService=Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new SlideTask(),3,5, TimeUnit.SECONDS);
        isAutoPlay=true;
    }

    private void stopPlay(){
        executorService.shutdown();
        isAutoPlay=false;
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

    @Override
    protected void onStart() {
        super.onStart();
        startPlay();
        isAutoPlay=true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlay();
        isAutoPlay=false;
    }

    private class SlideTask implements Runnable{

        @Override
        public void run() {
            currentItem=(currentItem+1)%imageViewList.size();
            handler.obtainMessage().sendToTarget();
        }
    }

    private void initImageList(){
        imageViewList=new ArrayList<>();
        for(int i=0;i<5;i++){
            ImageView imageView=new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViewList.add(imageView);
        }
    }

    private void initDotList(){
        dotList=new ArrayList<>();
        dotList.add(findViewById(R.id.dot1));
        dotList.add(findViewById(R.id.dot2));
        dotList.add(findViewById(R.id.dot3));
        dotList.add(findViewById(R.id.dot4));
        dotList.add(findViewById(R.id.dot5));
    }

    private void initBanner(){
        currentItem=0;
        banner=(ViewPager)findViewById(R.id.banner);
        banner.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imageViewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(imageViewList.get(position));
                return imageViewList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(imageViewList.get(position));
            }
        });
        banner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem=position;
                for(int i=0;i<5;i++){
                    if(i==position){
                        dotList.get(i).setBackgroundResource(R.drawable.dot_focus);
                    }else {
                        dotList.get(i).setBackgroundResource(R.drawable.dot_normal);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state){
                    case 0:
                        if(!isAutoPlay){
                            startPlay();
                            isAutoPlay=true;
                        }
                        break;
                    case 1:
                        stopPlay();
                        isAutoPlay=false;
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
