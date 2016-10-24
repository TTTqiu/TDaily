package com.ttt.zhihudaily.activity;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
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

public class HistoryActivity extends BaseActivity {

    private MyListAdapter adapter;
    private ListView listView;
    private List<Title> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_history);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_history_clear:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage("确定删除所有记录？");
                dialog.setCancelable(true);
                dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBUtil mDBUtil = DBUtil.getInstance(HistoryActivity.this);
                        mDBUtil.clearHistory();
                        list.clear();
                        adapter.notifyDataSetChanged();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        list = DBUtil.getInstance(this).loadHistoryTitle();
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
