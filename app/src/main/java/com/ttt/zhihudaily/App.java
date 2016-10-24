package com.ttt.zhihudaily;

import android.app.Application;
import android.graphics.Bitmap;
import android.view.View;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

public class App extends Application{
    {
        PlatformConfig.setWeixin("wx6e3d9e32a7134e57", "483cfd5292e13b489b33edc6f3feee1c");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        UMShareAPI.get(this);
    }
}
