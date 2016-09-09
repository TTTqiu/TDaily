package com.ttt.zhihudaily.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ttt.zhihudaily.R;
import com.ttt.zhihudaily.db.ZhiHuDailyDB;
import com.ttt.zhihudaily.entity.Title;
import com.ttt.zhihudaily.adapter.MyListAdapter;
import com.ttt.zhihudaily.util.HttpUtil;

import java.util.List;

public class FavouriteActivity extends AppCompatActivity {

    private MyListAdapter adapter;
    private ZhiHuDailyDB mZhiHuDailyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        mZhiHuDailyDB=ZhiHuDailyDB.getInstance(this);
        List<Title> list=mZhiHuDailyDB.loadNewsTitle();
        ListView listView=(ListView)findViewById(R.id.list_view_fav);
        adapter=new MyListAdapter(this,R.layout.recycler_view_item,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(HttpUtil.isNetworkConnected(FavouriteActivity.this)){
                    NewsActivity.startNewsActivity(FavouriteActivity.this,adapter.getItem(position));
                }else {
                    Toast.makeText(FavouriteActivity.this, "No Network", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
