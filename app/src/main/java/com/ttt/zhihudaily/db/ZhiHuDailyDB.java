package com.ttt.zhihudaily.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ttt.zhihudaily.entity.Title;

import java.util.ArrayList;
import java.util.List;

public class ZhiHuDailyDB {

    private SQLiteDatabase db;
    private static ZhiHuDailyDB mZhiHuDailyDB;

    public ZhiHuDailyDB(Context context){
        MyDBHelper helper=new MyDBHelper(context);
        db=helper.getWritableDatabase();
    }

    public synchronized static ZhiHuDailyDB getInstance(Context context){
        if(mZhiHuDailyDB==null){
            mZhiHuDailyDB=new ZhiHuDailyDB(context);
        }
        return mZhiHuDailyDB;
    }

    public void saveNewsTitle(Title title){
        ContentValues values=new ContentValues();
        values.put("news_id",title.getId());
        values.put("news_title",title.getName());
        values.put("news_image",title.getImage());
        db.insert("favourite",null,values);
    }

    public List<Title> loadNewsTitle(){
        List<Title> list=new ArrayList<>();
        Title title;
        Cursor cursor=db.query("favourite",null,null,null,null,null,null);
        while (cursor.moveToNext()){
            title=new Title(cursor.getString(cursor.getColumnIndex("news_title")),
                    cursor.getString(cursor.getColumnIndex("news_image")),
                    cursor.getInt(cursor.getColumnIndex("news_id")));
            list.add(title);
        }
        cursor.close();
        return list;
    }

    public void deleteNewsTitle(int id){
        db.delete("favourite","news_id=?",new String[]{""+id});
    }

    public Boolean isFavourite(Title title){
        Cursor cursor=db.query("favourite",null,"news_id=?",
                new String[]{""+title.getId()},null,null,null);
        Boolean isFavourite=cursor.moveToNext();
        cursor.close();
        return isFavourite;
    }
}
