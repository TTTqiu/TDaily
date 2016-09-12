package com.ttt.zhihudaily.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ttt.zhihudaily.R;
import com.ttt.zhihudaily.entity.Title;

import java.util.List;

public class MyListAdapter extends ArrayAdapter<Title>{

    private int resource;
    private Context mContext;

    public MyListAdapter(Context context, int resource, List<Title> objects){
        super(context,resource,objects);
        this.resource=resource;
        mContext=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view;
        Title title=getItem(position);
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resource,null);
            viewHolder=new ViewHolder();
            viewHolder.imageView=(ImageView)view.findViewById(R.id.title_image);
            viewHolder.textView=(TextView)view.findViewById(R.id.title_text);
            view.setTag(viewHolder);
        }else {
            view=convertView;
            viewHolder=(ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(title.getName());
        Glide.with(mContext).load(title.getImage()).placeholder(R.drawable.loading_image)
                .error(R.drawable.fail_image).into(viewHolder.imageView);
        return view;
    }

    private class ViewHolder{
        private ImageView imageView;
        private TextView textView;
    }
}
