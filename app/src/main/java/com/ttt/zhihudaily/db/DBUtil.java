package com.ttt.zhihudaily.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ttt.zhihudaily.entity.Title;

import java.util.ArrayList;
import java.util.List;

public class DBUtil {

    private static final String TABLE_NAME = "title";
    private SQLiteDatabase db;
    private static DBUtil mDBUtil;

    private DBUtil(Context context) {
        MyDBHelper helper = new MyDBHelper(context);
        db = helper.getWritableDatabase();
    }

    public synchronized static DBUtil getInstance(Context context) {
        if (mDBUtil == null) {
            mDBUtil = new DBUtil(context);
        }
        return mDBUtil;
    }

    public void saveNewsTitle(Title title) {
        ContentValues values = new ContentValues();
        values.put("news_id", title.getId());
        values.put("news_title", title.getName());
        values.put("news_image", title.getImage());
        values.put("news_date", title.getDate());
        values.put("is_favourite", title.getIsFavourite());
        db.insert(TABLE_NAME, null, values);
    }

    public void loadNewsTitleAtDate(String date,List<Title> list) {
        Title title;
        Cursor cursor = db.query(TABLE_NAME, null, "news_date=?", new String[]{date},
                null, null, null);
        while (cursor.moveToNext()) {
            title = new Title(cursor.getString(cursor.getColumnIndex("news_title")),
                    cursor.getString(cursor.getColumnIndex("news_image")),
                    cursor.getInt(cursor.getColumnIndex("news_id")),
                    cursor.getString(cursor.getColumnIndex("news_date")),
                    cursor.getInt(cursor.getColumnIndex("is_favourite")));
            list.add(title);
        }
        cursor.close();
    }

    public List<Title> loadFavourite() {
        List<Title> list=new ArrayList<>();
        Title title;
        Cursor cursor = db.query(TABLE_NAME, null, "is_favourite=?", new String[]{""+1},
                null, null, null);
        while (cursor.moveToNext()) {
            title = new Title(cursor.getString(cursor.getColumnIndex("news_title")),
                    cursor.getString(cursor.getColumnIndex("news_image")),
                    cursor.getInt(cursor.getColumnIndex("news_id")),
                    cursor.getString(cursor.getColumnIndex("news_date")),
                    cursor.getInt(cursor.getColumnIndex("is_favourite")));
            list.add(title);
        }
        cursor.close();
        return list;
    }

    public void deleteNewsTitle(int id) {
        db.delete(TABLE_NAME, "news_id=?", new String[]{"" + id});
    }

    public Boolean isFavourite(Title title){
        Cursor cursor=db.query(TABLE_NAME,null,"news_id=?",
                new String[]{""+title.getId()},null,null,null);
        int isFavourite=0;
        if (cursor.moveToNext()){
            isFavourite=cursor.getInt(cursor.getColumnIndex("is_favourite"));
        }
        cursor.close();
        return isFavourite==1;
    }

    public void setFavourite(Title title){
        ContentValues values=new ContentValues();
        values.put("is_favourite",1);
        db.update(TABLE_NAME,values,"news_id=?",new String[]{""+title.getId()});
    }

    public void cancelFavourite(Title title){
        ContentValues values=new ContentValues();
        values.put("is_favourite",0);
        db.update(TABLE_NAME,values,"news_id=?",new String[]{""+title.getId()});
    }

    public Boolean isExist(Title title){
        Cursor cursor=db.query(TABLE_NAME,null,"news_id=?",new String[]{""+title.getId()},
                null,null,null);
        Boolean isExist=false;
        if (cursor.moveToNext()){
            isExist=true;
        }
        cursor.close();
        return isExist;
    }

    public List<Title> showNewsTitle(){
        Title title;
        List<Title> list=new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME, null, null, null,null, null, null);
        while (cursor.moveToNext()) {
            title = new Title(cursor.getString(cursor.getColumnIndex("news_title")),
                    cursor.getString(cursor.getColumnIndex("news_image")),
                    cursor.getInt(cursor.getColumnIndex("news_id")),
                    cursor.getString(cursor.getColumnIndex("news_date")),
                    cursor.getInt(cursor.getColumnIndex("is_favourite")));
            list.add(title);
        }
        cursor.close();
        return list;
    }
}
