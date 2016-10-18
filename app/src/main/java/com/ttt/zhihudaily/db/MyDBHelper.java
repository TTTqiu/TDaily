package com.ttt.zhihudaily.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper{

    private static final String DB_NAME="ZhiHuDaily.db";
    private static final int DB_VERSION=1;

    private static final String CREATE_TITLE="create table title("+
            "id integer primary key autoincrement," +
            "news_id integer,"+
            "news_title text,"+
            "news_image text,"+
            "news_date text,"+
            "is_favourite integer)";

    private static final String CREATE_HISTORY="create table history("+
            "id integer primary key autoincrement," +
            "news_id integer,"+
            "news_title text,"+
            "news_image text)";

    private static final String CREATE_SEARCH="create table search("+
            "id integer primary key autoincrement," +
            "history_key text)";

    public MyDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TITLE);
        sqLiteDatabase.execSQL(CREATE_HISTORY);
        sqLiteDatabase.execSQL(CREATE_SEARCH);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
