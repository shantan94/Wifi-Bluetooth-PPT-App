package com.example.shantan.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by shantan on 7/27/2017.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.splash_activity);
        Thread logoTimer = new Thread() {
            public void run(){
                try{
                    int logoTimer = 0;
                    while(logoTimer < 5000){
                        sleep(100);
                        logoTimer = logoTimer +100;
                    };
                    startActivity(new Intent("com.tutorial.CLEARSCREEN"));
                }

                catch (InterruptedException e) {
                    e.printStackTrace();
                }

                finally{
                    finish();
                }
            }
        };

        logoTimer.start();
    }
}
