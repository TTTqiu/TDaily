package com.ttt.zhihudaily.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper{

    private static final String DB_NAME="ZhiHuDaily.db";
    private static final int DB_VERSION=1;
    private static final String TABLE_NAME="title";
    private static final String COLUMN_ID="id";
    private static final String COLUMN_NEWS_TITLE="news_title";
    private static final String COLUMN_NEWS_IMAGE="news_image";
    private static final String COLUMN_NEWS_ID="news_id";
    private static final String COLUMN_NEWS_DATE="news_date";
    private static final String COLUMN_IS_FAVOURITE="is_favourite";

    private static final String CREATE_TITLE="create table "+TABLE_NAME +"("+
            COLUMN_ID+" integer primary key autoincrement," +
            COLUMN_NEWS_ID+" integer,"+
            COLUMN_NEWS_TITLE+" text,"+
            COLUMN_NEWS_IMAGE+" text,"+
            COLUMN_NEWS_DATE+" text,"+
            COLUMN_IS_FAVOURITE+" integer)";

    public MyDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TITLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
