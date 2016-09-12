package com.ttt.zhihudaily.util;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView{

    private MyOnScrollChangedListener listener;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface MyOnScrollChangedListener{
        void myOnScrollChanged(int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }

    public void setMyOnScrollChangedListener(MyOnScrollChangedListener listener){
        this.listener=listener;
    }

    @Override
    protected void onScrollChanged(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        super.onScrollChanged(scrollX, scrollY, oldScrollX, oldScrollY);
        listener.myOnScrollChanged(scrollX,scrollY,oldScrollX,oldScrollY);
    }
}
