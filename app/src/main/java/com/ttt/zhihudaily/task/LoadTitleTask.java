package com.ttt.zhihudaily.task;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.ttt.zhihudaily.adapter.MyListAdapter;
import com.ttt.zhihudaily.adapter.MyRecyclerAdapter;
import com.ttt.zhihudaily.db.DBUtil;
import com.ttt.zhihudaily.entity.Title;
import com.ttt.zhihudaily.util.HttpUtil;
import com.ttt.zhihudaily.entity.TitleBean;

import java.util.ArrayList;
import java.util.List;

public class LoadTitleTask extends AsyncTask<Void, Void, TitleBean> {

    private List<Title> list;
    private MyRecyclerAdapter adapter;
    private String date;
    private Context context;
    private DBUtil mDBUtil;

    public LoadTitleTask(MyRecyclerAdapter adapter, List<Title> list, Context context, String date) {
        this.list = list;
        this.adapter = adapter;
        this.context = context;
        this.date = date;
    }

    @Override
    protected TitleBean doInBackground(Void... voids) {
        return HttpUtil.getParsedBeforeTitle(date);
    }

    @Override
    protected void onPostExecute(TitleBean bean) {
        if (list != null) {
            list.clear();
            Title title;
            mDBUtil = DBUtil.getInstance(context);
            for (int i = 0; i < bean.getStories().length; i++) {
                title = new Title(bean.getStories()[i].getTitle(), bean.getStories()[i].getImages()[0],
                        bean.getStories()[i].getId(), bean.getDate(), 0);
                list.add(title);
                if (!mDBUtil.isExist("title",title)){
                    mDBUtil.saveNewsTitle(title);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }
}
