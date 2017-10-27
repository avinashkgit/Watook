package com.watook.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.widget.ImageView;

import com.watook.R;
import com.watook.manager.DatabaseManager;
import com.watook.model.MyProfile;

public class SplashActivity extends BaseActivity {
    final Handler handler = new Handler();
    MyProfile myProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try {
            myProfile = DatabaseManager.getInstance(SplashActivity.this).getMyProfile();
        } catch (Exception e) {
        }
        final ImageView logoView = (ImageView) findViewById(R.id.img_logo);
//        Glide.with(this).load(R.drawable.logo).asBitmap().into(logoView);
        expand(logoView);
        if (myProfile == null) {
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
