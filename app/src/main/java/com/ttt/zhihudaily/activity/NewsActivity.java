package com.ttt.zhihudaily.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.ttt.zhihudaily.util.MyScrollView;

public class NewsActivity extends AppCompatActivity {

    private Boolean isFavourite=false;
    private ZhiHuDailyDB mZhiHuDailyDB;
    private Title title;
    private MyScrollView myScrollView;
    private WebView webView;
    private Toolbar toolbar;
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
        new LoadNewsTask(webView).execute(newsId);
        mZhiHuDailyDB=ZhiHuDailyDB.getInstance(this);
        isFavourite=mZhiHuDailyDB.isFavourite(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options,menu);
        if(isFavourite){
            menu.findItem(R.id.menu_fav).setIcon(R.drawable.fav_selected);
        }
        return true;
    }
//
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_fav:
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
            case R.id.menu_settings:

                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void initToolbarAlphaChange(){
        myScrollView =(MyScrollView)findViewById(R.id.my_scroll_view);
        myScrollView.setMyOnScrollChangedListener(new MyScrollView.MyOnScrollChangedListener() {
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
