package com.ttt.zhihudaily.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ttt.zhihudaily.entity.NewsBean;
import com.ttt.zhihudaily.entity.TitleBean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
    private static String LATEST_TITLE="http://news-at.zhihu.com/api/4/news/latest";
    private static String BEFORE_TITLE="http://news-at.zhihu.com/api/4/news/before/";
    private static String NEWS_DETAIL="http://news-at.zhihu.com/api/4/news/";

    public static String sendHttpRequest(String address){
        HttpURLConnection connection=null;
        try {
            URL url=new URL(address);
            connection=(HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            if(connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                InputStream is=connection.getInputStream();
                BufferedReader reader=new BufferedReader(new InputStreamReader(is));
                StringBuilder sb=new StringBuilder();
                String line;
                while((line=reader.readLine())!=null){
                    sb.append(line);
                }
                is.close();
                return sb.toString();
            }else {
                return "network error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }finally {
            if(connection!=null){
                connection.disconnect();
            }
        }
    }

    public static TitleBean getParsedLatestTitle(){
        Gson gson=new Gson();
        return gson.fromJson(sendHttpRequest(LATEST_TITLE),new TypeToken<TitleBean>(){}.getType());
    }

    public static TitleBean getParsedBeforeTitle(String date){
        Gson gson=new Gson();
        return gson.fromJson(sendHttpRequest(BEFORE_TITLE+date),new TypeToken<TitleBean>(){}.getType());
    }

    public static NewsBean getParsedNews(int id){
        Gson gson=new Gson();
        return gson.fromJson(sendHttpRequest(NEWS_DETAIL+id),new TypeToken<NewsBean>(){}.getType());
    }

    public static Boolean isNetworkConnected(Context context){
        ConnectivityManager manager=(ConnectivityManager)context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=manager.getActiveNetworkInfo();
        return networkInfo!=null&&networkInfo.isConnected();
    }
}
