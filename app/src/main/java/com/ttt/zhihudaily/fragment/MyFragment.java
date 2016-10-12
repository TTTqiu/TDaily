package com.ttt.zhihudaily.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ttt.zhihudaily.R;
import com.ttt.zhihudaily.activity.NewsActivity;
import com.ttt.zhihudaily.adapter.MyRecyclerAdapter;
import com.ttt.zhihudaily.entity.Title;
import com.ttt.zhihudaily.myView.MyNestedScrollView;
import com.ttt.zhihudaily.task.LoadBannerTask;
import com.ttt.zhihudaily.task.LoadTitleTask;
import com.ttt.zhihudaily.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

public class MyFragment extends Fragment {

    private MyRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private View view;
    private List<Title> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initRecyclerView();

        if (HttpUtil.isNetworkConnected(getActivity())) {
            Bundle bundle = getArguments();
            if (bundle != null) {
                new LoadTitleTask(adapter, list, bundle.getString("date")).execute();
            } else {
                new LoadTitleTask(adapter, list).execute();
            }
        } else {
            Snackbar.make(recyclerView, "没有网络", Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * 下拉刷新
     */
    public void refreshTitleList() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            new LoadTitleTask(adapter, list, bundle.getString("date")).execute();
        } else {
            new LoadTitleTask(adapter, list).execute();
        }
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        list = new ArrayList<>();
        adapter = new MyRecyclerAdapter(getActivity(), list);
        adapter.setMyOnItemClickListener(new MyRecyclerAdapter.MyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (HttpUtil.isNetworkConnected(getActivity())) {
                    NewsActivity.startNewsActivity(getActivity(), list.get(position));
                } else {
                    Snackbar.make(recyclerView, "没有网络", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
    }

    public List<Title> getList() {
        return list;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}
