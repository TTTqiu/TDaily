package com.ttt.zhihudaily.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.ttt.zhihudaily.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imageView=(ImageView)findViewById(R.id.splash_image);
        TextView textView1=(TextView)findViewById(R.id.splash_text_1);
        TextView textView2=(TextView)findViewById(R.id.splash_text_2);
        AlphaAnimation imageAnimation=new AlphaAnimation(0.0f,1.0f);
        imageAnimation.setDuration(1000);
        imageView.setAnimation(imageAnimation);
        AlphaAnimation textAnimation=new AlphaAnimation(0.0f,1.0f);
        textAnimation.setDuration(300);
        textView1.setAnimation(textAnimation);
        textView2.setAnimation(textAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
            }
        },2000);
    }
}
