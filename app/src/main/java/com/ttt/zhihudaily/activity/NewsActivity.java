package com.ttt.zhihudaily.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ttt.zhihudaily.R;
import com.ttt.zhihudaily.db.ZhiHuDailyDB;
import com.ttt.zhihudaily.entity.Title;
import com.ttt.zhihudaily.task.LoadNewsTask;
import com.ttt.zhihudaily.myView.MyNestedScrollView;

public class NewsActivity extends AppCompatActivity {

    private Boolean isFavourite=false;
    private ZhiHuDailyDB mZhiHuDailyDB;
    private Title title;
    private MyNestedScrollView myNestedScrollView;
    private WebView webView;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private Intent shareIntent;
    private int currentY=500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        toolbar=(Toolbar)findViewById(R.id.toolbar_news);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initToolbarAlphaChange();

        webView=(WebView)findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        title=(Title) getIntent().getSerializableExtra("title");
        int newsId=title.getId();
        shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        new LoadNewsTask(webView,shareIntent).execute(newsId);
        mZhiHuDailyDB=ZhiHuDailyDB.getInstance(this);
        isFavourite=mZhiHuDailyDB.isFavourite(title);

        NotificationManager nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.cancel(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news,menu);
        if(isFavourite){
            menu.findItem(R.id.menu_news_fav).setIcon(R.drawable.fav_selected);
        }

        MenuItem item=menu.findItem(R.id.menu_news_share);
        // 为了不在右边显示最常用应用图标。暂未出现问题。
        ShareActionProvider shareActionProvider=new ShareActionProvider(this){
            @Override
            public View onCreateActionView() {
                return null;
            }
        };
        item.setIcon(R.drawable.share);
        MenuItemCompat.setActionProvider(item,shareActionProvider);
        shareActionProvider.setShareIntent(shareIntent);
        return true;
    }
//
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_news_fav:
                if(isFavourite){
                    item.setIcon(R.drawable.fav_normal);
                    isFavourite=false;
                    mZhiHuDailyDB.deleteNewsTitle(title.getId());
                }else {
                    item.setIcon(R.drawable.fav_selected);
                    isFavourite=true;
                    mZhiHuDailyDB.saveNewsTitle(title);
                }
                break;
            case R.id.menu_news_settings:
                Intent intent=new Intent(NewsActivity.this,PrefsActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void initToolbarAlphaChange(){
        myNestedScrollView =(MyNestedScrollView)findViewById(R.id.my_scroll_view);
        appBarLayout=(AppBarLayout)findViewById(R.id.appbar_news_toolbar);
        myNestedScrollView.setMyOnScrollChangedListener(new MyNestedScrollView.MyOnScrollChangedListener() {
            @Override
            public void myOnScrollChanged(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                float alpha;
                if(scrollY>=currentY){
                    alpha=0;
                    toolbar.setVisibility(View.INVISIBLE);
                    currentY=scrollY;
                }else {
                    toolbar.setVisibility(View.VISIBLE);
                    if(scrollY<currentY&&scrollY>(currentY-500)){
                        alpha=(float)(currentY-scrollY)/500;
                    }else {
                        alpha=1;
                        currentY=scrollY+500;
                    }
                }
                appBarLayout.setAlpha(alpha);
                toolbar.setAlpha(alpha);
            }
        });
    }

    public static void startNewsActivity(Context context, Title title) {
        Intent intent = new Intent(context, NewsActivity.class);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }
}
