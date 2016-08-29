package com.ttt.zhihudaily.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtil {
    private static String TITLE_LIST="http://news-at.zhihu.com/api/4/news/latest";
    private static String NEWS_DETAIL="http://news-at.zhihu.com/api/4/news/3892357";

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

    public static TitleJSONBean getParsedTitle(){
        Gson gson=new Gson();
        return gson.fromJson(sendHttpRequest(TITLE_LIST),new TypeToken<TitleJSONBean>(){}.getType());
    }
}
