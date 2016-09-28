package com.ttt.zhihudaily.myView;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.ScrollView;

public class MyNestedScrollView extends NestedScrollView{

    private MyOnScrollChangedListener listener;

    public MyNestedScrollView(Context context) {
        super(context);
    }

    public MyNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
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
