package com.ttt.zhihudaily.activity;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ttt.zhihudaily.R;
import com.ttt.zhihudaily.adapter.MyListAdapter;
import com.ttt.zhihudaily.db.DBUtil;
import com.ttt.zhihudaily.entity.Title;
import com.ttt.zhihudaily.util.HttpUtil;

import java.util.Collections;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private MyListAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        List<Title> list = DBUtil.getInstance(this).loadHistoryTitle();
        listView = (ListView) findViewById(R.id.list_view_history);
        // 倒序排列list
        Collections.reverse(list);
        adapter = new MyListAdapter(this, R.layout.list_view_item, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (HttpUtil.isNetworkConnected(HistoryActivity.this)) {
                    NewsActivity.startNewsActivity(HistoryActivity.this, adapter.getItem(position));
                } else {
                    Snackbar.make(listView, "没有网络", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
}
