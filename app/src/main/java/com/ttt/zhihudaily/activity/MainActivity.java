package com.ttt.zhihudaily.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ttt.zhihudaily.R;
import com.ttt.zhihudaily.entity.Title;
import com.ttt.zhihudaily.adapter.TitleAdapter;
import com.ttt.zhihudaily.task.LoadTitleTask;
import com.ttt.zhihudaily.util.HttpUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private TitleAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getTime());

        initListView();
        new LoadTitleTask(adapter).execute();
        pullToRefresh();
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
     * 获得日期和星期
     * @return 2016年8月23日 星期三 格式的日期
     */
    private String getTime(){
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat format=new SimpleDateFormat("yyyy年M月d日 EEEE");
        return format.format(calendar.getTime());
    }

    // 下拉刷新
    private void pullToRefresh(){
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeColors(Color.GREEN,Color.YELLOW,Color.RED);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new LoadTitleTask(adapter,swipeRefreshLayout,MainActivity.this).execute();
            }
        });
    }

    private void initListView(){
        listView=(ListView)findViewById(R.id.list_view);
        adapter=new TitleAdapter(this,R.layout.list_view_item);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                NewsActivity.startNewsActivity(MainActivity.this,adapter.getItem(position));
            }
        });
    }
}
