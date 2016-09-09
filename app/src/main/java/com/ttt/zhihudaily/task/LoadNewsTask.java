package com.ttt.zhihudaily.task;

import android.os.AsyncTask;
import android.webkit.WebView;

import com.ttt.zhihudaily.entity.NewsBean;
import com.ttt.zhihudaily.util.HttpUtil;

public class LoadNewsTask extends AsyncTask<Integer,Void,NewsBean>{

    private WebView webView;

    public LoadNewsTask(WebView webView){
        this.webView=webView;
    }


    @Override
    protected NewsBean doInBackground(Integer... params) {
        return HttpUtil.getParsedNews(params[0]);
    }

    @Override
    protected void onPostExecute(NewsBean bean) {
        String headerImage;
        if (bean.getImage() == null || bean.getImage() == "") {
            headerImage = "file:///android_asset/news_detail_header_image.jpg";
        } else {
            headerImage = bean.getImage();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"img-wrap\">")
                .append("<h1 class=\"headline-title\">")
                .append(bean.getTitle()).append("</h1>")
                .append("<span class=\"img-source\">")
                .append(bean.getImage_source()).append("</span>")
                .append("<img src=\"").append(headerImage)
                .append("\" alt=\"\">")
                .append("<div class=\"img-mask\"></div>");
        String mNewsContent = "<link rel=\"stylesheet\" type=\"text/css\" href=\"news_content_style.css\"/>"
                + "<link rel=\"stylesheet\" type=\"text/css\" href=\"news_header_style.css\"/>"
                + bean.getBody().replace("<div class=\"img-place-holder\">", sb.toString());
        webView.loadDataWithBaseURL("file:///android_asset/", mNewsContent, "text/html", "UTF-8", null);
    }
}
