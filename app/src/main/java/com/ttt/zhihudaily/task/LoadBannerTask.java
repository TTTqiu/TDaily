package com.ttt.zhihudaily.task;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ttt.zhihudaily.R;
import com.ttt.zhihudaily.adapter.TitleAdapter;
import com.ttt.zhihudaily.entity.Title;
import com.ttt.zhihudaily.entity.TitleBean;
import com.ttt.zhihudaily.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

public class LoadBannerTask extends AsyncTask<Void,Void,TitleBean> {
    private Context context;
    private List<ImageView> imageViewList;

    public LoadBannerTask(List<ImageView> imageViewList,Context context){
        this.context=context;
        this.imageViewList=imageViewList;
    }

    @Override
    protected TitleBean doInBackground(Void... voids) {
        return HttpUtil.getParsedLatestTitle();
    }

    @Override
    protected void onPostExecute(TitleBean bean) {
        for(int i=0;i<imageViewList.size();i++){
            Glide
                    .with(context)
                    .load(bean.getTop_stories()[i].getImage())
                    .placeholder(R.drawable.loading_image)
                    .error(R.drawable.fail_image)
                    .into(imageViewList.get(i));
        }
    }
}
