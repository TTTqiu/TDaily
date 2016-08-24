package com.ttt.zhihudaily;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getTime());


        ListView listView=(ListView)findViewById(R.id.list_view);

        List<Title> list=new ArrayList<>();
        Title title1=new Title("第一个新第一个新闻第一个新闻第一个新闻第一个新闻闻",R.drawable.image);
        Title title2=new Title("第二个新第二个新闻第二个新闻闻",R.drawable.image2);
        Title title3=new Title("第三个第二个新闻第二个新闻第二个新个新闻第二个新闻新闻",R.drawable.image2);
        Title title4=new Title("第四第二个新闻新闻",R.drawable.image);
        Title title5=new Title("第五个第二个新闻新闻",R.drawable.image);
        Title title6=new Title("第五个第二个新闻新闻",R.drawable.image);
        Title title7=new Title("第五个第二个新闻新闻",R.drawable.image);
        Title title8=new Title("第五个第二个新闻新闻",R.drawable.image);
        list.add(title1);
        list.add(title2);
        list.add(title3);
        list.add(title4);
        list.add(title5);
        list.add(title6);
        list.add(title7);
        list.add(title8);

        listView.setAdapter(new TitleAdapter(this,R.layout.list_view_item,list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(MainActivity.this,NewsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_fav:
                Intent intent=new Intent(MainActivity.this,FavouriteActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_settings:

                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 获得日期和星期
     * @return 2016年8月23日 星期三 格式的日期
     */
    private String getTime(){
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat format=new SimpleDateFormat("yyyy年M月d日 EEEE");
        return format.format(calendar.getTime());
    }
}
