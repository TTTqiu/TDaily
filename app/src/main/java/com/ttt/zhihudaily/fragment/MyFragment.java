package com.ttt.zhihudaily.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ttt.zhihudaily.R;
import com.ttt.zhihudaily.activity.NewsActivity;
import com.ttt.zhihudaily.adapter.MyListAdapter;
import com.ttt.zhihudaily.adapter.MyRecyclerAdapter;
import com.ttt.zhihudaily.entity.Title;
import com.ttt.zhihudaily.task.LoadTitleTask;
import com.ttt.zhihudaily.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

public class MyFragment extends Fragment{

    private SwipeRefreshLayout swipeRefreshLayout;
    private MyRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private View view;
    private List<Title> list;

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

//        initListView();
        initRecyclerView();
        pullToRefresh();

        if(HttpUtil.isNetworkConnected(getActivity())){
            Bundle bundle=getArguments();
            if(bundle!=null){
                new LoadTitleTask(adapter,list,bundle.getString("date")).execute();
            }else {
                new LoadTitleTask(adapter,list).execute();
            }
        }else {
            Toast.makeText(getActivity(), "No Network", Toast.LENGTH_SHORT).show();
        }
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
                        new LoadTitleTask(adapter,list,swipeRefreshLayout,getActivity(),
                                bundle.getString("date")).execute();
                    }else {
                        new LoadTitleTask(adapter,list,swipeRefreshLayout,getActivity()).execute();
                    }
                }else {
                    Toast.makeText(getActivity(), "No Network", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

//    private void initListView(){
//        listView=(ListView)view.findViewById(R.id.list_view);
//        adapter=new MyListAdapter(getActivity(),R.layout.recycler_view_item);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                if(HttpUtil.isNetworkConnected(getActivity())){
//                    NewsActivity.startNewsActivity(getActivity(),adapter.getItem(position));
//                }else {
//                    Toast.makeText(getActivity(), "No Network", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

    private void initRecyclerView(){
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        list=new ArrayList<>();
        adapter=new MyRecyclerAdapter(getActivity(),list);
        adapter.setMyOnItemClickListener(new MyRecyclerAdapter.MyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                NewsActivity.startNewsActivity(getActivity(),list.get(position));
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(adapter);
    }
}
