package com.ttt.zhihudaily.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ttt.zhihudaily.R;
import com.ttt.zhihudaily.entity.Title;
import com.ttt.zhihudaily.util.DensityUtil;
import com.ttt.zhihudaily.util.HttpUtil;
import com.tttqiu.library.TUtil;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

    private List<Title> list;
    private Context context;
    private MyOnItemClickListener myOnItemClickListener;
    private int heightsPX[]=new int[25];

    public MyRecyclerAdapter(Context context, List<Title> list) {
        super();
        this.context = context;
        this.list = list;
    }

    public void setMyOnItemClickListener(MyOnItemClickListener myOnItemClickListener) {
        this.myOnItemClickListener = myOnItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setText(list.get(position).getName());
        // 为imageView设置随机高度
        ViewGroup.LayoutParams params = holder.imageView.getLayoutParams();
        for (int i=0;i<25;i++){
            heightsPX[i]=DensityUtil.dip2px(context,150+(int)(Math.random()*60));
        }
        if (position<25){
            params.height= heightsPX[position];
        }
        holder.imageView.setLayoutParams(params);
//        Glide
//                .with(context)
//                .load(list.get(position).getImage())
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .into(holder.imageView);
        TUtil.loadImageInto(context,list.get(position).getImage(),holder.imageView,
                TUtil.DEFAULT,TUtil.DEFAULT);
        final int pos = holder.getAdapterPosition();
        if (myOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myOnItemClickListener.onItemClick(v, pos);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    myOnItemClickListener.onItemLongClick(v, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView imageView;

        MyViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.recycler_title_text);
            imageView = (ImageView) itemView.findViewById(R.id.recycler_title_image);
        }
    }

    public interface MyOnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
}
