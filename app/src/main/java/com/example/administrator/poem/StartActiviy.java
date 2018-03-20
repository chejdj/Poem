package com.example.administrator.poem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * Created by Administrator on 2018/3/20.
 */

public class StartActiviy extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view =View.inflate(this,R.layout.startlayout,null);
        setContentView(view);
        AlphaAnimation start=new AlphaAnimation(0.3f,1.0f);
        start.setDuration(2000);
        view.startAnimation(start);
        start.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                  Intent intent =new Intent();
                  intent.setClass(StartActiviy.this,MainActivity.class);
                   StartActiviy.this.startActivity(intent);
                   StartActiviy.this.finish();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
