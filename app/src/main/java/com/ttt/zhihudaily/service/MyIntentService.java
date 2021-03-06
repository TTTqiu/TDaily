package com.ttt.zhihudaily.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.ttt.zhihudaily.R;
import com.ttt.zhihudaily.activity.MainActivity;
import com.ttt.zhihudaily.activity.NewsActivity;
import com.ttt.zhihudaily.activity.SplashActivity;
import com.ttt.zhihudaily.entity.Title;
import com.ttt.zhihudaily.entity.TitleBean;
import com.ttt.zhihudaily.receiver.MyBroadcastReceiver;
import com.ttt.zhihudaily.util.HttpUtil;

public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (HttpUtil.isNetworkConnected(this)){
            TitleBean titleBean = HttpUtil.getParsedLatestTitle();
            String lastedName = titleBean.getStories()[0].getTitle();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String savedName = preferences.getString("lastedName", lastedName);
            if (!lastedName.equals(savedName)) {
                // 通知
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Notification.Builder builder = new Notification.Builder(MyIntentService.this);
                builder.setSmallIcon(R.mipmap.icon);
                builder.setContentTitle(lastedName);
                builder.setContentText("点击查看新内容");
                builder.setWhen(System.currentTimeMillis());

                Title title = new Title(lastedName, titleBean.getStories()[0].getImages()[0],
                        titleBean.getStories()[0].getId());
                Intent[] intents=new Intent[2];
                intents[0]=new Intent(MyIntentService.this, MainActivity.class);
                intents[1]=new Intent(MyIntentService.this, NewsActivity.class);
                intents[1].putExtra("title", title);
                PendingIntent nPi = PendingIntent.getActivities(MyIntentService.this, 0,
                        intents, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(nPi);
                Notification notification = builder.build();
                notification.vibrate = new long[]{0, 400};
                nm.notify(1, notification);
            }
            preferences.edit().putString("lastedName", lastedName).apply();
        }

        // 定时
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerAtTime = SystemClock.elapsedRealtime() + 10 * 60 * 1000;
        Intent aIntent = new Intent(MyIntentService.this, MyBroadcastReceiver.class);
        PendingIntent aPi = PendingIntent.getBroadcast(MyIntentService.this, 0, aIntent, 0);
        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, aPi);
    }
}
