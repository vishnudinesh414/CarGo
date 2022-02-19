package com.example.searchmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {
    private static int SPLASH_SCREEN =3000;
    Animation middle,bottom,bottom2;
    ImageView imageView;
    TextView textView,tt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);



        middle = AnimationUtils.loadAnimation(this,R.anim.middle);
        bottom=AnimationUtils.loadAnimation(this,R.anim.bottom);
        bottom2=AnimationUtils.loadAnimation(this,R.anim.bottom2);
        imageView=findViewById(R.id.imageView);
        textView=findViewById(R.id.textView);
        tt=findViewById(R.id.textView2);
        imageView.setAnimation(middle);
        textView.setAnimation(bottom);
        tt.setAnimation(bottom2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(Splash.this, Login.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);

    }
}
