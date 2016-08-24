package com.ttt.zhihudaily;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TitleAdapter extends ArrayAdapter<Title>{

    private int resource;

    public TitleAdapter(Context context, int resource, List<Title> objects){
        super(context,resource,objects);
        this.resource=resource;
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
        viewHolder.imageView.setImageResource(title.getImageId());
        viewHolder.textView.setText(title.getName());
        return view;
    }

    private class ViewHolder{
        private ImageView imageView;
        private TextView textView;
    }
}
