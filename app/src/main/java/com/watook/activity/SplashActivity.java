package com.watook.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.widget.ImageView;

import com.watook.R;
import com.watook.application.MySharedPreferences;
import com.watook.manager.DatabaseManager;
import com.watook.model.MyProfile;
import com.watook.util.Constant;

public class SplashActivity extends BaseActivity {
    final Handler handler = new Handler();
    boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try {
            isLoggedIn = (boolean) MySharedPreferences.getObject(Constant.IS_LOGGED_IN);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final ImageView logoView = (ImageView) findViewById(R.id.img_logo);
        expand(logoView);
        if (!isLoggedIn) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent login = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(login);
                    finish();
                }
            }, 2000);

        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent main = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(main);
                    finish();
                }
            }, 2000);

        }

    }

    @Override
    public void onBackPressed() {

    }
}
