package com.privilist.act;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.privilist.R;
import com.privilist.act.base.BaseAct;

public class SplashAct extends BaseAct {
    public static final int SPLASH_TIME = 2000;// ms
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFinishing()) {
            findViewById(R.id.act_container).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent startI = new Intent(SplashAct.this, IntroductionAct.class);
                    startActivity(startI);
                    finish();
                }
            }, SPLASH_TIME);
        }
    }

}
