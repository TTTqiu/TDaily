package com.ttt.zhihudaily.task;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.ttt.zhihudaily.adapter.TitleAdapter;
import com.ttt.zhihudaily.entity.Title;
import com.ttt.zhihudaily.util.HttpUtil;
import com.ttt.zhihudaily.entity.TitleBean;

import java.util.ArrayList;
import java.util.List;

public class LoadTitleTask extends AsyncTask<Void,Void,TitleBean>{

    private List<Title> list=new ArrayList<>();
    private TitleAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Context context;

    public LoadTitleTask(TitleAdapter adapter){
        this.adapter=adapter;
    }

    public LoadTitleTask(TitleAdapter adapter,SwipeRefreshLayout swipeRefreshLayout,Context context){
        this.adapter=adapter;
        this.swipeRefreshLayout=swipeRefreshLayout;
        this.context=context;
    }

    @Override
    protected TitleBean doInBackground(Void... voids) {
        return HttpUtil.getParsedTitle();
    }

    @Override
    protected void onPostExecute(TitleBean bean) {
        Title title;
        for(int i=0;i<bean.getStories().length;i++){
            title=new Title(bean.getStories()[i].getTitle(),bean.getStories()[i].getImages()[0],
                    bean.getStories()[i].getId());
            list.add(title);
        }
        adapter.refreshList(list);
        if(swipeRefreshLayout!=null){
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(context, "刷新成功", Toast.LENGTH_SHORT).show();
        }
    }
}
