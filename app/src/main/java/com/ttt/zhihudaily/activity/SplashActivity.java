package com.ttt.zhihudaily.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ttt.zhihudaily.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        ImageView splashImage = (ImageView) findViewById(R.id.splash_image);
        TextView splashText1 = (TextView) findViewById(R.id.splash_text_1);
        TextView splashText2 = (TextView) findViewById(R.id.splash_text_2);

        AlphaAnimation imageAnimation = new AlphaAnimation(0.0f, 1.0f);
        imageAnimation.setDuration(1000);
        splashImage.setAnimation(imageAnimation);

        AlphaAnimation textAnimation = new AlphaAnimation(0.0f, 1.0f);
        textAnimation.setDuration(1000);
        textAnimation.setStartOffset(800);
        splashText1.setAnimation(textAnimation);
        splashText2.setAnimation(textAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                // 去掉跳转动画
                overridePendingTransition(0, 0);
                finish();
            }
        }, 2500);
    }
}
