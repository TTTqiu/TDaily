package com.ttt.zhihudaily.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ttt.zhihudaily.R;
import com.ttt.zhihudaily.db.DBUtil;
import com.ttt.zhihudaily.entity.Title;
import com.ttt.zhihudaily.task.LoadNewsTask;
import com.ttt.zhihudaily.myView.MyNestedScrollView;
import com.ttt.zhihudaily.util.DensityUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

public class NewsActivity extends BaseActivity {

    private Boolean isFavourite=false;
    private DBUtil mDBUtil;
    private Title title;
    private MyNestedScrollView myNestedScrollView;
    private WebView webView;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private Intent shareIntent;
    private int currentY;
    private int px500;
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Snackbar.make(myNestedScrollView,"分享成功",Snackbar.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Snackbar.make(myNestedScrollView,"分享失败",Snackbar.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Snackbar.make(myNestedScrollView,"取消分享",Snackbar.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        toolbar=(Toolbar)findViewById(R.id.toolbar_news);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        px500 =DensityUtil.dip2px(this,179);
        currentY= px500;
        initToolbarAlphaChange();

        webView=(WebView)findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        title=(Title) getIntent().getSerializableExtra("title");
        int newsId=title.getId();
        shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        new LoadNewsTask(webView,shareIntent).execute(newsId);

        mDBUtil = DBUtil.getInstance(this);
        isFavourite= mDBUtil.isFavourite(title);
        if (!mDBUtil.isExist("history",title)){
            mDBUtil.saveHistoryTitle(title);
        }else {
            mDBUtil.deleteHistoryTitle(title);
            mDBUtil.saveHistoryTitle(title);
        }
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
        MenuItemCompat.setActionProvider(item,shareActionProvider);
        shareActionProvider.setShareIntent(shareIntent);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        NotificationManager nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.cancel(1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_news_fav:
                if(isFavourite){
                    item.setIcon(R.drawable.fav_normal);
                    isFavourite=false;
                    mDBUtil.cancelFavourite(title);
                }else {
                    item.setIcon(R.drawable.fav_selected);
                    isFavourite=true;
                    mDBUtil.setFavourite(title);
                }
                break;
            case R.id.menu_news_share_wx:
                // 分享到微信
                UMImage image = new UMImage(NewsActivity.this, title.getImage());
                new ShareAction(NewsActivity.this).setPlatform(SHARE_MEDIA.WEIXIN)
                        .withText("分享自 知乎日报 By TTTqiu")
                        .withTitle(title.getName())
                        .withMedia(image)
                        .withTargetUrl(shareIntent.getStringExtra(Intent.EXTRA_TEXT))
                        .setCallback(umShareListener)
                        .share();
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
        myNestedScrollView =(MyNestedScrollView)findViewById(R.id.nested_scroll_view_news);
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
                    if(scrollY<currentY&&scrollY>(currentY-px500)){
                        alpha=(float)(currentY-scrollY)/px500;
                    }else {
                        alpha=1;
                        currentY=scrollY+px500;
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
