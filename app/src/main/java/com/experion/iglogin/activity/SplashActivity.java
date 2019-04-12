package com.experion.iglogin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.os.Handler;

import com.experion.iglogin.R;
import com.experion.iglogin.databinding.ActivitySplashBinding;
import com.experion.iglogin.util.Navigator;

public class SplashActivity extends AppCompatActivity {


    private ActivitySplashBinding mSplashBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        initTimerDelay();
    }

    private void initTimerDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSplashBinding.animationView.cancelAnimation();
                Navigator.getInstance().navigate(SplashActivity.this,
                        LoginActivity.class,
                        null,
                        true,
                        true);
            }
        }, 5000);
    }
}
