package com.ttt.zhihudaily.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ttt.zhihudaily.service.MyIntentService;

public class MyBroadcastReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i=new Intent(context, MyIntentService.class);
        context.startService(i);
    }
}
