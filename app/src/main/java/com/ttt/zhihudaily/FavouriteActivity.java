package com.ttt.zhihudaily;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        ListView listView=(ListView)findViewById(R.id.list_view_fav);
        List<Title> list=new ArrayList<>();
        Title title1=new Title("第一个新个新闻第一个新闻第一个新闻闻",R.drawable.image);
        Title title2=new Title("第二个新第二个新闻第二闻",R.drawable.image2);
        Title title3=new Title("第三个第二个新个新个新闻第二个新闻新闻",R.drawable.image2);
        Title title4=new Title("第新闻新闻",R.drawable.image);
        Title title5=new Title("第五个新闻新闻",R.drawable.image);
        list.add(title1);
        list.add(title2);
        list.add(title3);
        list.add(title4);
        list.add(title5);
        listView.setAdapter(new TitleAdapter(this,R.layout.list_view_item,list));
    }
}
