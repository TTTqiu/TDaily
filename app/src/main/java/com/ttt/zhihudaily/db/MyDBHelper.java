package com.ttt.zhihudaily.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper{

    public static final String DB_NAME="ZhiHuDaily.db";
    public static final int DB_VERSION=1;
    public static final String TABLE_NAME="favourite";
    public static final String COLUMN_ID="id";
    public static final String COLUMN_NEWS_TITLE="news_title";
    public static final String COLUMN_NEWS_IMAGE="news_image";
    public static final String COLUMN_NEWS_ID="news_id";

    public static final String CREATE_FAVOURITE="create table "+TABLE_NAME +"("+
            COLUMN_ID+" integer primary key autoincrement," +
            COLUMN_NEWS_ID+" integer,"+
            COLUMN_NEWS_TITLE+" text,"+
            COLUMN_NEWS_IMAGE+" text)";

    public MyDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_FAVOURITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
