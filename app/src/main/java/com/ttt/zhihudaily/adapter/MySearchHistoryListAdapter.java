package com.ttt.zhihudaily.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ttt.zhihudaily.R;
import com.ttt.zhihudaily.db.DBUtil;

import java.util.List;

public class MySearchHistoryListAdapter extends ArrayAdapter<String>{

    private int resource;
    private Context context;
    private DeleteHistoryListener listener;

    public MySearchHistoryListAdapter(Context context, int resource, List<String> objects){
        super(context,resource,objects);
        this.resource=resource;
        this.context=context;
    }

    public void setDeleteHistoryListener(DeleteHistoryListener listener){
        this.listener=listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view;
        final String key = getItem(position);
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resource, null);
            viewHolder =new ViewHolder();
            viewHolder.imageView=(ImageView)view.findViewById(R.id.search_history_delete);
            viewHolder.textView=(TextView)view.findViewById(R.id.search_history_text);
            view.setTag(viewHolder);
        }else {
            view=convertView;
            viewHolder=(ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(key);
        // 点叉叉删除对应搜索记录
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBUtil.getInstance(context).deleteSearchHistory(key);
                if (listener!=null){
                    listener.onDeleteFinish();
                }
            }
        });
        return view;
    }

    private class ViewHolder{
        private ImageView imageView;
        private TextView textView;
    }

    public interface DeleteHistoryListener{
        void onDeleteFinish();
    }
}
