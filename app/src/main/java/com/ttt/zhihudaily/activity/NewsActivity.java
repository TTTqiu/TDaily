package com.ttt.zhihudaily.activity;

import android.content.Context;
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
import com.ttt.zhihudaily.task.LoadNewsTask;
import com.ttt.zhihudaily.util.HttpUtil;

public class NewsActivity extends AppCompatActivity {

    private Boolean isFavourite=false;
    private ZhiHuDailyDB mZhiHuDailyDB;
    private Title title;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        getMenuInflater().inflate(R.menu.menu,menu);
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
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void startNewsActivity(Context context,Title title){
        if(HttpUtil.isNetworkConnected(context)){
            Intent intent=new Intent(context,NewsActivity.class);
            intent.putExtra("title",title);
            context.startActivity(intent);
        }else {
            Toast.makeText(context, "No Network", Toast.LENGTH_SHORT).show();
        }
    }
}
