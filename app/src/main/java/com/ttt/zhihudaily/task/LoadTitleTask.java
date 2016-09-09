package com.ttt.zhihudaily.task;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.ttt.zhihudaily.adapter.MyListAdapter;
import com.ttt.zhihudaily.adapter.MyRecyclerAdapter;
import com.ttt.zhihudaily.entity.Title;
import com.ttt.zhihudaily.util.HttpUtil;
import com.ttt.zhihudaily.entity.TitleBean;

import java.util.ArrayList;
import java.util.List;

public class LoadTitleTask extends AsyncTask<Void,Void,TitleBean>{

    private List<Title> list;
    private MyRecyclerAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Context context;
    private String date;

    public LoadTitleTask(MyRecyclerAdapter adapter,List<Title> list){
        this.adapter=adapter;
        this.list=list;
    }

    public LoadTitleTask(MyRecyclerAdapter adapter,List<Title> list, String date){
        this(adapter,list);
        this.date=date;
    }

    public LoadTitleTask(MyRecyclerAdapter adapter,List<Title> list,SwipeRefreshLayout swipeRefreshLayout, Context context){
        this(adapter,list);
        this.swipeRefreshLayout=swipeRefreshLayout;
        this.context=context;
    }

    public LoadTitleTask(MyRecyclerAdapter adapter,List<Title> list, SwipeRefreshLayout swipeRefreshLayout,
                         Context context, String date){
        this(adapter,list,swipeRefreshLayout,context);
        this.date=date;
    }

    @Override
    protected TitleBean doInBackground(Void... voids) {
        if(date!=null){
            return HttpUtil.getParsedBeforeTitle(date);
        }else {
            return HttpUtil.getParsedLatestTitle();
        }
    }

    @Override
    protected void onPostExecute(TitleBean bean) {
        Title title;
        for(int i=0;i<bean.getStories().length;i++){
            title=new Title(bean.getStories()[i].getTitle(),bean.getStories()[i].getImages()[0],
                    bean.getStories()[i].getId());
            list.add(title);
        }
        adapter.notifyDataSetChanged();
        if(swipeRefreshLayout!=null){
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(context, "刷新成功", Toast.LENGTH_SHORT).show();
        }
    }
}
