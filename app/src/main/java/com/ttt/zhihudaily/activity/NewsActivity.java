package com.ttt.zhihudaily.activity;

import android.content.Intent;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ttt.zhihudaily.R;
import com.ttt.zhihudaily.db.ZhiHuDailyDB;
import com.ttt.zhihudaily.entity.Title;

public class NewsActivity extends AppCompatActivity {

    private Boolean isFavourite=false;
    private ZhiHuDailyDB mZhiHuDailyDB;
    private Title title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        WebView webView=(WebView)findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        title=(Title) getIntent().getSerializableExtra("title");
        if(title!=null){
            webView.loadUrl("http://daily.zhihu.com/story/"+title.getId());
        }
        mZhiHuDailyDB=ZhiHuDailyDB.getInstance(this);
        isFavourite=mZhiHuDailyDB.isFavourite(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        if(isFavourite){
            menu.findItem(R.id.menu_fav).setIcon(R.drawable.fav_selected);
        }
        return true;
    }

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
            default:
                break;
        }
        return true;
    }
}
