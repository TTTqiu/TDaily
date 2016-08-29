package com.ttt.zhihudaily.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ttt.zhihudaily.R;
import com.ttt.zhihudaily.db.ZhiHuDailyDB;
import com.ttt.zhihudaily.entity.Title;
import com.ttt.zhihudaily.adapter.TitleAdapter;
import com.ttt.zhihudaily.task.LoadTitleTask;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {

    private TitleAdapter adapter;
    private ZhiHuDailyDB mZhiHuDailyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        mZhiHuDailyDB=ZhiHuDailyDB.getInstance(this);
        List<Title> list=mZhiHuDailyDB.loadNewsTitle();
        ListView listView=(ListView)findViewById(R.id.list_view_fav);
        adapter=new TitleAdapter(this,R.layout.list_view_item,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                NewsActivity.startNewsActivity(FavouriteActivity.this,adapter.getItem(position));
            }
        });
    }
}
