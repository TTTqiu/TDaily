package com.ttt.zhihudaily.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ttt.zhihudaily.R;
import com.ttt.zhihudaily.activity.MainActivity;
import com.ttt.zhihudaily.activity.NewsActivity;
import com.ttt.zhihudaily.entity.Title;
import com.ttt.zhihudaily.entity.TitleBean;
import com.ttt.zhihudaily.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

public class LoadBannerTask extends AsyncTask<Void,Void,TitleBean>{
    private Context context;
    private List<View> bannerList;
    private List<Title> titleList;

    public LoadBannerTask(List<View> bannerList, Context context,List<Title> titleList){
        this.context=context;
        this.bannerList=bannerList;
        this.titleList=titleList;
    }

    @Override
    protected TitleBean doInBackground(Void... voids) {
        return HttpUtil.getParsedLatestTitle();
    }

    @Override
    protected void onPostExecute(TitleBean bean) {
        for(int i=0;i<bannerList.size();i++){
            String name=bean.getTop_stories()[i].getTitle();
            String image=bean.getTop_stories()[i].getImage();
            int id=bean.getTop_stories()[i].getId();
            Title title=new Title(name,image,id);
            titleList.add(title);

            TextView textView=(TextView)bannerList.get(i).findViewById(R.id.banner_text);
            textView.setText(name);
            ImageView imageView=(ImageView)bannerList.get(i).findViewById(R.id.banner_image);
            Glide.with(context).load(image).into(imageView);
        }
    }
}
