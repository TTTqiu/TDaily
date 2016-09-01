package com.ttt.zhihudaily.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ttt.zhihudaily.R;
import com.ttt.zhihudaily.activity.MainActivity;
import com.ttt.zhihudaily.activity.NewsActivity;
import com.ttt.zhihudaily.adapter.TitleAdapter;
import com.ttt.zhihudaily.task.LoadTitleTask;
import com.ttt.zhihudaily.util.HttpUtil;

public class MyFragment extends Fragment{

    private SwipeRefreshLayout swipeRefreshLayout;
    private TitleAdapter adapter;
    private ListView listView;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_list,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initListView();
        if(HttpUtil.isNetworkConnected(getActivity())){
            Bundle bundle=getArguments();
            if(bundle!=null){
                new LoadTitleTask(adapter,bundle.getString("date")).execute();
            }else {
                new LoadTitleTask(adapter).execute();
            }
        }else {
            Toast.makeText(getActivity(), "No Network", Toast.LENGTH_SHORT).show();
        }
        pullToRefresh();
    }

    /**
     * 下拉刷新
     */
    private void pullToRefresh(){
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeColors(Color.GREEN,Color.YELLOW,Color.RED);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(HttpUtil.isNetworkConnected(getActivity())){
                    Bundle bundle=getArguments();
                    if(bundle!=null){
                        new LoadTitleTask(adapter,swipeRefreshLayout,getActivity(),
                                bundle.getString("date")).execute();
                    }else {
                        new LoadTitleTask(adapter,swipeRefreshLayout,getActivity()).execute();
                    }
                }else {
                    Toast.makeText(getActivity(), "No Network", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void initListView(){
        listView=(ListView)view.findViewById(R.id.list_view);
        adapter=new TitleAdapter(getActivity(),R.layout.list_view_item);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                NewsActivity.startNewsActivity(getActivity(),adapter.getItem(position));
            }
        });
    }
}
