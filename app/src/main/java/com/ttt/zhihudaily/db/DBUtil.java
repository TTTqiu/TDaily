package com.ttt.zhihudaily.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ttt.zhihudaily.entity.Title;

import java.util.ArrayList;
import java.util.List;

public class DBUtil {

    private static final String TABLE_TITLE = "title";
    private static final String TABLE_HISTORY = "history";
    private static final String TABLE_SEARCH = "search";
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
        db.insert(TABLE_TITLE, null, values);
    }

    public void deleteNewsTitle(Title title) {
        db.delete(TABLE_TITLE, "news_id=?", new String[]{"" + title.getId()});
    }

    public void deleteAllNewsTitleAtDate(String date) {
        db.delete(TABLE_TITLE, "news_date=?", new String[]{date});
    }

    public void loadNewsTitleAtDate(String date, List<Title> list) {
        Title title;
        Cursor cursor = db.query(TABLE_TITLE, null, "news_date=?", new String[]{date},
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

    public void saveHistoryTitle(Title title) {
        ContentValues values = new ContentValues();
        values.put("news_id", title.getId());
        values.put("news_title", title.getName());
        values.put("news_image", title.getImage());
        db.insert(TABLE_HISTORY, null, values);
    }

    public void deleteHistoryTitle(Title title) {
        db.delete(TABLE_HISTORY, "news_id=?", new String[]{"" + title.getId()});
    }

    public void clearHistory() {
        db.delete(TABLE_HISTORY, null, null);
    }

    public List<Title> loadHistoryTitle() {
        List<Title> list = new ArrayList<>();
        Title title;
        Cursor cursor = db.query(TABLE_HISTORY, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            title = new Title(cursor.getString(cursor.getColumnIndex("news_title")),
                    cursor.getString(cursor.getColumnIndex("news_image")),
                    cursor.getInt(cursor.getColumnIndex("news_id")));
            list.add(title);
        }
        cursor.close();
        return list;
    }

    public List<Title> loadFavourite() {
        List<Title> list = new ArrayList<>();
        Title title;
        Cursor cursor = db.query(TABLE_TITLE, null, "is_favourite=?", new String[]{"" + 1},
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

    public Boolean isFavourite(Title title) {
        Cursor cursor = db.query(TABLE_TITLE, null, "news_id=?",
                new String[]{"" + title.getId()}, null, null, null);
        int isFavourite = 0;
        if (cursor.moveToNext()) {
            isFavourite = cursor.getInt(cursor.getColumnIndex("is_favourite"));
        }
        cursor.close();
        return isFavourite == 1;
    }

    public void setFavourite(Title title) {
        ContentValues values = new ContentValues();
        values.put("is_favourite", 1);
        db.update(TABLE_TITLE, values, "news_id=?", new String[]{"" + title.getId()});
    }

    public void cancelFavourite(Title title) {
        ContentValues values = new ContentValues();
        values.put("is_favourite", 0);
        db.update(TABLE_TITLE, values, "news_id=?", new String[]{"" + title.getId()});
    }

    public Boolean isExist(String tableName, Title title) {
        Cursor cursor = db.query(tableName, null, "news_id=?", new String[]{"" + title.getId()},
                null, null, null);
        Boolean isExist = false;
        if (cursor.moveToNext()) {
            isExist = true;
        }
        cursor.close();
        return isExist;
    }

    public List<Title> searchNewsTitle(String key) {
        Title title;
        List<Title> list = new ArrayList<>();
        Cursor cursor = db.query(TABLE_TITLE, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String name=cursor.getString(cursor.getColumnIndex("news_title"));
            if (name.contains(key)){
                title = new Title(cursor.getString(cursor.getColumnIndex("news_title")),
                        cursor.getString(cursor.getColumnIndex("news_image")),
                        cursor.getInt(cursor.getColumnIndex("news_id")),
                        cursor.getString(cursor.getColumnIndex("news_date")),
                        cursor.getInt(cursor.getColumnIndex("is_favourite")));
                list.add(title);
            }
        }
        cursor.close();
        return list;
    }

    public void saveSearchHistory(String key){
        ContentValues values = new ContentValues();
        values.put("history_key", key);
        db.insert(TABLE_SEARCH, null, values);
    }

    public void deleteSearchHistory(String key) {
        db.delete(TABLE_SEARCH, "history_key=?", new String[]{key});
    }

    public void deleteAllSearchHistory(){
        db.delete(TABLE_SEARCH, null, null);
    }

    public List<String> loadSearchHistory() {
        List<String> list = new ArrayList<>();
        Cursor cursor = db.query(TABLE_SEARCH, null, null, null,null, null, null);
        while (cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndex("history_key")));
        }
        cursor.close();
        return list;
    }

    public Boolean isExistInSearch(String key) {
        Cursor cursor = db.query(TABLE_SEARCH, null, "history_key=?", new String[]{key},null, null, null);
        Boolean isExist = false;
        if (cursor.moveToNext()) {
            isExist = true;
        }
        cursor.close();
        return isExist;
    }
}
