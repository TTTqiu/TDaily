package com.ttt.zhihudaily.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ttt.zhihudaily.R;
import com.ttt.zhihudaily.adapter.MyPagerAdapter;
import com.ttt.zhihudaily.entity.Title;
import com.ttt.zhihudaily.fragment.MyFragment;
import com.ttt.zhihudaily.myView.MyNestedScrollView;
import com.ttt.zhihudaily.task.LoadBannerTask;
import com.ttt.zhihudaily.util.HttpUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private List<Fragment> fragmentList;
    private String[] titles = new String[5];
    private ViewPager viewPager;
    private ViewPager banner;
    private List<View> dotList;
    private List<View> bannerList;
    private List<Title> bannerTitleList;
    private Boolean isAutoPlay = false;
    private int currentItem;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private TabLayout tabTop;
    private TabLayout tabCenter;
    private MyNestedScrollView myNestedScrollView;
    private ScheduledExecutorService executorService;
    private Boolean isViewPagerHeightSet = false;
    private int viewPagerHeight = 0;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Boolean prepareExit = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            banner.setCurrentItem(currentItem);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        initBanner();
        initNavigation();
        initViewPager();
        initTabLayout();
        refreshBannerAndTitleList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        if (HttpUtil.isNetworkConnected(this)) {
            new LoadBannerTask(bannerList, this, bannerTitleList).execute();
        } else {
            Snackbar.make(myNestedScrollView, "没有网络", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            Intent intent1 = new Intent(MainActivity.this, FavouriteActivity.class);
            startActivity(intent1);
        } else {
            if (HttpUtil.isNetworkConnected(this)) {
                switch (currentItem) {
                    case 0:
                        NewsActivity.startNewsActivity(this, bannerTitleList.get(0));
                        break;
                    case 1:
                        NewsActivity.startNewsActivity(this, bannerTitleList.get(1));
                        break;
                    case 2:
                        NewsActivity.startNewsActivity(this, bannerTitleList.get(2));
                        break;
                    case 3:
                        NewsActivity.startNewsActivity(this, bannerTitleList.get(3));
                        break;
                    case 4:
                        NewsActivity.startNewsActivity(this, bannerTitleList.get(4));
                        break;
                    default:
                        break;
                }
            } else {
                Snackbar.make(myNestedScrollView, "没有网络", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_settings:
                Intent intent2 = new Intent(MainActivity.this, PrefsActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 获得日期
     *
     * @return 2016年8月23日 格式的日期
     */
    private String getDate(int i, Boolean isFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -i);
        SimpleDateFormat simpleDateFormat;
        if (isFormat) {
            simpleDateFormat = new SimpleDateFormat("yyyy年M月d日");
        } else {
            simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        }
        return simpleDateFormat.format(calendar.getTime());
    }

    private void startPlay() {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new SlideTask(), 4, 5, TimeUnit.SECONDS);
        isAutoPlay = true;
    }

    private void stopPlay() {
        executorService.shutdown();
        isAutoPlay = false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(findViewById(R.id.navigation_view))) {
            // 按返回时若侧边导航栏是打开的，先退出
            drawerLayout.closeDrawers();
        } else {
            // 按两次退出
            if (!prepareExit) {
                Snackbar.make(myNestedScrollView, "再按一次退出", Snackbar.LENGTH_SHORT).show();
                prepareExit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prepareExit = false;
                    }
                }, 2000);
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startPlay();
        isAutoPlay = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlay();
        isAutoPlay = false;
    }

    private class SlideTask implements Runnable {

        @Override
        public void run() {
            currentItem = (currentItem + 1) % bannerList.size();
            handler.obtainMessage().sendToTarget();
        }
    }

    private void initViewPager() {
        fragmentList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            titles[i] = getDate(i, true);
            MyFragment fragment = new MyFragment();
            if (i != 0) {
                Bundle bundle = new Bundle();
                bundle.putString("date", getDate(i - 1, false));
                fragment.setArguments(bundle);
            }
            fragmentList.add(fragment);
        }
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragmentList, titles));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 每次切换时回到ViewPager顶端
                if (myNestedScrollView.getScrollY() > banner.getHeight()) {
                    myNestedScrollView.scrollTo(0, banner.getHeight());
                }

                // 每次切换设置ViewPager高度
                ViewGroup.LayoutParams params = viewPager.getLayoutParams();
                if (position != 0) {
                    params.height = 8030;
                    viewPager.setLayoutParams(params);
                } else {
                    if (isViewPagerHeightSet) {
                        params.height = viewPagerHeight;
                        viewPager.setLayoutParams(params);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initBannerList() {
        bannerList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.banner_item, null);
            view.setOnClickListener(this);
            bannerList.add(view);
        }
    }

    private void initDotList() {
        dotList = new ArrayList<>();
        dotList.add(findViewById(R.id.dot1));
        dotList.add(findViewById(R.id.dot2));
        dotList.add(findViewById(R.id.dot3));
        dotList.add(findViewById(R.id.dot4));
        dotList.add(findViewById(R.id.dot5));
    }

    private void initBanner() {
        initDotList();
        initBannerList();
        currentItem = 0;
        bannerTitleList = new ArrayList<>();
        banner = (ViewPager) findViewById(R.id.banner);
        banner.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return bannerList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(bannerList.get(position));
                return bannerList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(bannerList.get(position));
            }
        });
        banner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                for (int i = 0; i < 5; i++) {
                    if (i == position) {
                        dotList.get(i).setBackgroundResource(R.drawable.dot_focus);
                    } else {
                        dotList.get(i).setBackgroundResource(R.drawable.dot_normal);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case 0:
                        if (!isAutoPlay) {
                            startPlay();
                            isAutoPlay = true;
                        }
                        break;
                    case 1:
                        stopPlay();
                        isAutoPlay = false;
                        break;
                    default:
                        break;
                }
            }
        });
        // 防止ViewPager自动跳到屏幕顶端
        banner.setFocusable(true);
        banner.setFocusableInTouchMode(true);
        banner.requestFocus();
    }

    private void initNavigation() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        // 去掉scrollbar。scrollbar在NavigationView的child:NavigationMenuView中，
        navigationView.getChildAt(0).setVerticalScrollBarEnabled(false);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        item.setChecked(true);
                        Toast.makeText(MainActivity.this, "" + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
    }

    private void initTabLayout() {
        tabTop = (TabLayout) findViewById(R.id.tab_top);
        tabTop.setupWithViewPager(viewPager);
        tabCenter = (TabLayout) findViewById(R.id.tab_center);
        tabCenter.setupWithViewPager(viewPager);

        myNestedScrollView = (MyNestedScrollView) findViewById(R.id.nested_scroll_view);
        myNestedScrollView.setMyOnScrollChangedListener(new MyNestedScrollView.MyOnScrollChangedListener() {
            @Override
            public void myOnScrollChanged(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int height = banner.getHeight();
                AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar_main_toolbar);
                if (scrollY >= height) {
                    tabTop.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        appBarLayout.setElevation(0);
                    }
                } else {
                    tabTop.setVisibility(View.GONE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        appBarLayout.setElevation(12);
                    }
                }

                // 设定ViewPager第一页高度
                if (!isViewPagerHeightSet) {
                    MyFragment myFragment = (MyFragment) fragmentList.get(0);
                    RecyclerView recyclerView = myFragment.getRecyclerView();
                    View lastView1 = recyclerView.getChildAt(myFragment.getList().size() - 1);
                    View lastView2 = recyclerView.getChildAt(myFragment.getList().size() - 2);
                    if (lastView1!=null&&lastView2!=null){
                        viewPagerHeight = Math.max(lastView1.getBottom(), lastView2.getBottom()) + 15;
                        ViewGroup.LayoutParams params = viewPager.getLayoutParams();
                        params.height = viewPagerHeight;
                        viewPager.setLayoutParams(params);
                    }
                    isViewPagerHeightSet = true;
                }
            }
        });
    }

    private void refreshBannerAndTitleList() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(Color.GREEN, Color.YELLOW, Color.RED);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (HttpUtil.isNetworkConnected(MainActivity.this)) {
                    new LoadBannerTask(bannerList, MainActivity.this, bannerTitleList,
                            swipeRefreshLayout).execute();
                    for (int i = 0; i < 5; i++) {
                        MyFragment myFragment = (MyFragment) fragmentList.get(i);
                        myFragment.refreshTitleList();
                    }
                    isViewPagerHeightSet = false;
                } else {
                    Snackbar.make(myNestedScrollView, "没有网络", Snackbar.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }
}